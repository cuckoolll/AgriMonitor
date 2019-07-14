layui.use(['form','layer','util'], function(form,layer,util) {
	var winH=$(window).height();
	$("#main").height(winH/2-20);
	$("#main1").height(winH/2-20);

	var curyear=util.toDateString(new Date(), 'yyyy');
	var years=[curyear-9,curyear-8,curyear-7,curyear-6,curyear-5,curyear-4,curyear-3,curyear-2,curyear-1,curyear];
	var myChart, myChart1;
	function init(){
		myChart = echarts.init(document.getElementById('main')); 
		myChart1 = echarts.init(document.getElementById('main1')); 
		option = {
				title: {
			        text: '近十年产量情况'
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
			            dataView: {show: true, readOnly: false},
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
			            data:[0.0, 0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
			        },
			        {
			            name:'单产',
			            type:'line',
			            yAxisIndex: 1,
			            data:[0.0, 0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
			        }
			    ]
		};       
		option1 = {
				title: {
			        text: '近十年播种面积情况'
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
			        data: [0.0, 0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0],
			        type: 'bar'
			    }]
			};

		option.title.text=$("#crops_type").find("option:selected").text()+"近十年产量情况";
		myChart.setOption(option); 
		option1.title.text=$("#crops_type").find("option:selected").text()+"近十年播种面积情况";
		myChart1.setOption(option1); 

		$.post("/cropsplant/cropsPlantAnalysis/getdata", {type: $("#crops_type").val()},function(data){
			if(data){
				if(data.error){
					layer.msg("查询数据失败");
				}else{
					myChart.dispose();
					myChart = echarts.init(document.getElementById('main')); 
					option.title.text=$("#crops_type").find("option:selected").text()+"近十年产量情况";
					option.series[0].data=data.zc;
					option.series[1].data=data.dc;
					myChart.setOption(option); 
					myChart1.dispose();
					myChart1 = echarts.init(document.getElementById('main1')); 
					option1.title.text=$("#crops_type").find("option:selected").text()+"近十年播种面积情况";
					option1.series[0].data=data.mj;
					myChart1.setOption(option1); 
				}
			}
			
		});
	}
	
	form.on('select(crops_type)', function(data){
		init();
	});  
	
	init();
});
