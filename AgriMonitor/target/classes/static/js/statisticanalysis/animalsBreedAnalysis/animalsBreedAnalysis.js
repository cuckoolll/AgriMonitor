layui.use(['form','layer','util','table','laydate'], function(form,layer,util,table,laydate) {
	var curyear=util.toDateString(new Date(), 'yyyy');
	var curmonth=util.toDateString(new Date(), 'yyyymm');
	var months=['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
	var chart1, chart2;
	var option1;
	
	laydate.render({
	    elem: '#year',
	    type: 'year',
	    value:curyear,
	    done: function(value, date, endDate){
	    	render(value);
	    }
	 });
	
	function init(){
		option1 = {
		    title: {
		        text: ''
		    },
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: ['月末存栏数', '能繁殖母畜']
		    },
		    yAxis: {
	            type: 'value',
	            name: '单位(万头)',
	            min: 0,
	            axisLabel: {
	                formatter: '{value}'
	            }
	        },
		    xAxis: {
		        type: 'category',
		        data: months
		    },
		    series: [
		        {
		            name: '月末存栏数',
		            type: 'bar',
		            data: ['', '','','','','','','','','','','']
		        },
		        {
		            name: '能繁殖母畜',
		            type: 'bar',
		            data: ['', '','','','','','','','','','','']
		        }
		    ]
		};
		option1.title.text= $("#target_type").find("option:selected").text()+"存栏情况统计图";
		chart1 = echarts.init(document.getElementById('chart1')); 
		chart1.setOption(option1); 
		
		loadData(curyear,$("#target_type").val());
	}
	
	function loadData(year,target_type){
		$.post("/animalsBreed/monthDataAnalysis", {year: year,target_type: target_type},function(data){
			if(data){
				for (var i = curyear*100+1; i <= curmonth; i++) {
					if(data[i] && data[i].length>0){
						for(var j=0,j<data[i].length;j++){
							if(target_type==data[i].fgid){
								option1.series[0].data[j]=data[i].surplus_size;
								option1.series[1].data[j]=data[i].female_size;
								break;
							}
						}
					}
				}
				chart1.dispose();
				chart1 = echarts.init(document.getElementById('chart1')); 
				chart1.setOption(option1); 
			}
		});
	}
	form.on('select(crops_type)', function(data){
		init();
	});  
	
	init();
});
