layui.use(['form','layer','util','table','laydate'], function(form,layer,util,table,laydate) {
	var curyear=util.toDateString(new Date(), 'yyyy');
	var curmonth=util.toDateString(new Date(), 'yyyyMM');
	var months=['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
	var chart1, chart2;
	var option1,option2;
	
	laydate.render({
	    elem: '#year',
	    type: 'year',
	    value:curyear,
	    done: function(value, date, endDate){
	    	loadData(value,$("#target_type").val());
	    }
	 });
	
	function init(){
		option1 = {
		    title: {text: '',x:'center'},
		    tooltip: {trigger: 'axis'},
		    legend: {data:['月末存栏数', '能繁殖母畜'],top:30},
		    toolbox: {
		        feature: {saveAsImage: {}}
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: months
		    },
		    yAxis: {
	            type: 'value',name: '单位(万头)',min: 0,axisLabel: {formatter: '{value}'}
	        },
		    series: [
		        {
		            name:'月末存栏数',
		            type:'line',
		            data:[]
		        },
		        {
		            name:'能繁殖母畜',
		            type:'line',
		            data:[]
		        }
		    ]
		};
		option1.title.text= $("#target_type").find("option:selected").text()+"存栏情况统计图";
		chart1 = echarts.init(document.getElementById('chart1')); 
		chart1.setOption(option1); 
		/**产仔情况图***/
		option2 = {
			title: {text: '',x:'center'},
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {type: 'cross',crossStyle: {color: '#999'}}
		    },
		    toolbox: {
		        feature: {saveAsImage: {}}
		    },
		    legend: {
		        data:['产仔数','成活数','成活率','去年同期成活率'],top:30
		    },
		    xAxis: [
		        {type: 'category',data: months}
		    ],
		    yAxis: [
		        {
		            type: 'value',
		            name: '单位(万头)',
		            axisLabel: {formatter: '{value}'}
		        },
		        {
		            type: 'value',
		            name: '成活率',
		            axisLabel: {formatter: '{value}'}
		        }
		    ],
		    series: [
		        {
		            name:'产仔数',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'成活数',
		            type:'bar',
		            data:[]
		        },
		        {
		            name:'成活率',
		            type:'line',
		            yAxisIndex: 1,
		            data:[]
		        },
		        {
		            name:'去年同期成活率',
		            type:'line',
		            yAxisIndex: 1,
		            data:[]
		        }
		    ]
		};
		option2.title.text= $("#target_type").find("option:selected").text()+"产仔情况统计图";
		chart2 = echarts.init(document.getElementById('chart2')); 
		chart2.setOption(option2); 
		
		loadData(curyear,$("#target_type").val());
	}
	
	function loadData(year,target_type){
		$.post("/animalsBreed/monthDataAnalysis", {year: year,target_type: target_type},function(data){
			if(data){
				var index=0;
				for (var i = curyear*100+1; i <= curmonth; i++) {
					if(data[i] && data[i].length>0){
						for(var j=0;j<data[i].length;j++){
							if(target_type==data[i][j].fgid){
								option1.series[0].data[index]=data[i][j].surplus_size;
								option1.series[1].data[index]=data[i][j].female_size;
								
								
								break;
							}
						}
					}
					index++;
				}
				chart1.dispose();
				chart1 = echarts.init(document.getElementById('chart1')); 
				chart1.setOption(option1); 
			}
		});
	}
	
	form.on('select(target_type)', function(data){
		loadData($("#year").val(),$("#target_type").val());
	});  
	
	init();
});
