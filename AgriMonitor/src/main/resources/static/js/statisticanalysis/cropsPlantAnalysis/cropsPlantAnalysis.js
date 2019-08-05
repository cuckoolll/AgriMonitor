layui.use(['form','layer','util','table'], function(form,layer,util,table) {
	var winH=$(window).height();
	$("#main").height(winH/2-110);
	$("#main1").height(winH/2-110);

	var curyear=util.toDateString(new Date(), 'yyyy');
	var years=[curyear-9,curyear-8,curyear-7,curyear-6,curyear-5,curyear-4,curyear-3,curyear-2,curyear-1,curyear];
	var myChart, myChart1;
	
	function inittable(data){
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    height:winH/2-70,
		    cols: [[ //表头
		      {field: 'date_year', title: '年份',align:'center'},
		      {field: 'planted_area', title: '农作物播种面积（万亩）',align:'center'},
		      {field: 'planted_output', title: '农作物总产（万公斤）',align:'center'},
		      {field: 'dc', title: '农作物单产（万斤）',align:'center'}
		    ]],
		    data:data
		});
	}
	
	function init(){
		myChart = echarts.init(document.getElementById('main')); 
		myChart1 = echarts.init(document.getElementById('main1')); 
		option = {
				title: {
			        text: '近十年产量统计图'
			    },
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: {
			            type: 'cross',
			            crossStyle: {
			                color: '#999'
			            }
			        }
			    },
			    toolbox: {
			        feature: {
			            saveAsImage: {show: true}
			        }
			    },
			    legend: {
			        data:['总产','单产']
			    },
			    xAxis: [
			        {
			            type: 'category',
			            data: years,
			            axisPointer: {
			                type: 'shadow'
			            }
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value',
			            name: '总产(万公斤)',
			            min: 0,
			            axisLabel: {
			                formatter: '{value}'
			            }
			        },
			        {
			            type: 'value',
			            name: '单产(万斤)',
			            min: 0,
			            axisLabel: {
			                formatter: '{value}'
			            }
			        }
			    ],
			    series: [
			        {
			            name:'总产',
			            type:'bar',
			            data:['', '','','','','','','','','']
			        },
			        {
			            name:'单产',
			            type:'line',
			            yAxisIndex: 1,
			            data:['', '','','','','','','','','']
			        }
			    ]
		};       
		option1 = {
				title: {
			        text: '近十年播种面积统计图'
			    },
			    toolbox: {
			        feature: {
			            saveAsImage: {show: true}
			        }
			    },
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: {
			            type: 'cross',
			            crossStyle: {
			                color: '#999'
			            }
			        }
			    },
			    xAxis: {
			        type: 'category',
			        data: years
			    },
			    yAxis: {
			        type: 'value',
			        name: '面积(万亩)',
		            min: 0,
		            axisLabel: {
		                formatter: '{value}'
		            }
			    },
			    series: [{
			        data: ['', '','','','','','','','',''],
			        type: 'bar'
			    }]
			};

		option.title.text="近十年"+$("#crops_type").find("option:selected").text()+"产量统计图";
		myChart.setOption(option); 
		option1.title.text="近十年"+$("#crops_type").find("option:selected").text()+"播种面积统计图";
		myChart1.setOption(option1); 
		$("#tabletitle").text("近十年"+$("#crops_type").find("option:selected").text()+"产量面积表");

		$.post("/cropsplant/cropsPlantAnalysis/getdata", {type: $("#crops_type").val(),year: curyear},function(data){
			if(data && data.srcdata && data.data){
				for (var i = 0; i < years.length; i++) {
					if(data.data[years[i]]){
						option.series[0].data[i]=data.data[years[i]].planted_output;
						option.series[1].data[i]=data.data[years[i]].dc;
						option1.series[0].data[i]=data.data[years[i]].planted_area;
					}
				}
				
				myChart.dispose();
				myChart = echarts.init(document.getElementById('main')); 
				myChart.setOption(option); 
				
				myChart1.dispose();
				myChart1 = echarts.init(document.getElementById('main1')); 
				myChart1.setOption(option1); 
				
				inittable(data.srcdata);
			}
		});
	}
	
	form.on('select(crops_type)', function(data){
		init();
	});  
	init();
});
