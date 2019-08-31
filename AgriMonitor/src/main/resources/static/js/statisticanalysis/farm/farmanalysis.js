layui.use(['form','layer','util','table'], function(form,layer,util,table) {
	var winH=$(window).height();
	$("#chart").height(winH/2-100);

	var chart,option;
	
	function inittable(data){
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    height:winH/2-20,
		    page:false,
		    cols: [[ //表头
		      {field: 'zbmc', title: '认定畜种',align:'center'},
			  {field: 'cls', title: '存栏数',align:'center'}
		    ]],
		    data:data
		});
	}
	
	function init(){
		chart = echarts.init(document.getElementById('chart')); 
		option = {
		    title : {text: '刚察县养殖场各畜种存栏占比图',x:'left',subtext: '单位：头、只'},
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {show: true}
		        }
		    },
		    legend: {
		    	orient: 'vertical',
		        left: 10,
		        top: 50,
		        data: ['']
		    },
		    series : [
		        {
		            name: '存栏数',
		            type: 'pie',
		            data: [{value:0, name:''}],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};
	
		chart = echarts.init(document.getElementById('chart')); 
		chart.setOption(option); 

		$.post("/farminfo/findSumGroupTowns", {},function(res){
	        if(res && res.piedata && res.piedata.length>0){
	        	var tabledata=[];
	        	$.each(res.piedata,function(index,item){
	        		option.legend.data.push(item.name);
	        		tabledata.push({zbmc:item.name,cls:item.value});
	        	});
	        	option.series[0].data=res.piedata;
	    	    chart.dispose();
	    	    chart = echarts.init(document.getElementById('chart'));
	    	    chart.setOption(option); 
	    	    inittable(tabledata);
	        }
	    });
	}
	
	init();
});
