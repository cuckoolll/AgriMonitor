var chart1;

function chart1(){
	chart1 = echarts.init(document.getElementById('chart1')); 
	var data = [
	     {name: '沙柳河镇', value: 670,size:100},
	     {name: '伊克乌兰乡', value: 100,size:100},
	     {name: '泉吉乡', value: 100,size:100},
	     {name: '吉尔孟乡', value: 100,size:100},
	     {name: '黄玉农场', value: 100,size:100},
	     {name: '哈尔盖镇', value: 100,size:100}
	];
	var geoCoordMap = {
	    '沙柳河镇':[100.145841,37.335063],
	    '伊克乌兰乡':[100.09333,37.321518],
	    '泉吉乡':[99.886799,37.274324],
	    '吉尔孟乡':[99.579424,37.155703],
	    '黄玉农场':[99.95554,37.270184],
	    '哈尔盖镇':[100.417701,37.231455]
	};

	var convertData = function (data) {
	    var res = [];
	    for (var i = 0; i < data.length; i++) {
	        var geoCoord = geoCoordMap[data[i].name];
	        if (geoCoord) {
	            res.push({
	                name: data[i].name,
	                value: geoCoord.concat(data[i].size)
	            });
	        }
	    }
	    return res;
	};

	var option = {
	    title: {
	        text: '刚察县乡镇养殖场数据统计图',
	        left: 'center'
	    },
	    tooltip : {
	        trigger: 'item'
	    },
	    bmap: {
	        center: [100.030020,37.235610],
	        zoom:11,
	        roam: true
	    },
	    series : [
	        {
	            name: 'Top 5',
	            type: 'effectScatter',
	            coordinateSystem: 'bmap',
	            data: convertData(data.sort(function (a, b) {
	                return b.value - a.value;
	            }).slice(0, 6)),
	            symbolSize: function (val) {
	                return val[2] / 10;
	            },
	            showEffectOn: 'render',
	            rippleEffect: {
	                brushType: 'stroke'
	            },
	            hoverAnimation: true,
	            label: {
	                normal: {
	                    formatter: '{b}',
	                    position: 'right',
	                    show: true
	                }
	            },
	            itemStyle: {
	                normal: {
	                    color: 'purple',
	                    shadowBlur: 10,
	                    shadowColor: '#333'
	                }
	            },
	            zlevel: 1
	        }
	    ]
	};
      

	chart1.setOption(option); 

}

function chart2(){
	var option = {
	    title : {
	        text: '刚察县乡镇养殖场各畜种占比图',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        type: 'scroll',
	        orient: 'vertical',
	        right: 10,
	        top: 20,
	        bottom: 20,
	        data: ['访问','fsd','直gdf接访问','gdf','gd']
	    },
	    series : [
	        {
	            name: '姓名',
	            type: 'pie',
	            radius : '77%',
	            center: ['40%', '50%'],
	            data: [{value:33, name:'访问'},{value:35, name:'fsd'},{value:335, name:'fs'},{value:33, name:'直gdf接访问'},{value:35, name:'gdf'},{value:55, name:'gd'}],
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

	chart2 = echarts.init(document.getElementById('chart2')); 
	chart2.setOption(option); 
}
function chart3(){
	var option = {
	    title : {
	        text: '刚察县各农作物占比图',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        type: 'scroll',
	        orient: 'vertical',
	        right: 10,
	        top: 20,
	        bottom: 20,
	        data: ['访问','fsd','直gdf接访问','gdf','gd']
	    },
	    series : [
	        {
	            name: '姓名',
	            type: 'pie',
	            radius : '77%',
	            center: ['40%', '50%'],
	            data: [{value:33, name:'访问'},{value:35, name:'fsd'},{value:335, name:'fs'},{value:33, name:'直gdf接访问'},{value:35, name:'gdf'},{value:55, name:'gd'}],
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

	chart3 = echarts.init(document.getElementById('chart3')); 
	chart3.setOption(option); 
}
function chart4(){
	var option = {
	    title : {
	        text: '刚察县各农作物总产占比图',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        type: 'scroll',
	        orient: 'vertical',
	        right: 10,
	        top: 20,
	        bottom: 20,
	        data: ['访问','fsd','直gdf接访问','gdf','gd']
	    },
	    series : [
	        {
	            name: '姓名',
	            type: 'pie',
	            radius : '77%',
	            center: ['40%', '50%'],
	            data: [{value:33, name:'访问'},{value:35, name:'fsd'},{value:335, name:'fs'},{value:33, name:'直gdf接访问'},{value:35, name:'gdf'},{value:55, name:'gd'}],
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

	chart4 = echarts.init(document.getElementById('chart4')); 
	chart4.setOption(option); 
}
function chart5(){
	var option = {
			title : {
		        text: '刚察县畜牧业产量图',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {
		            type : 'shadow'
		        }
		    },
		    legend: {
		        data: ['直接访问', '邮件营销','联盟广告','视频广告'],
		        x:'right'
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    yAxis:  {
		        type: 'value'
		    },
		    xAxis: {
		        type: 'category',
		        data: ['周一','周二','周三','周四','周五','周六','周日','周一1','周二1','周三1','周四1','周五1']
		    },
		    series: [
		        {
		            name: '直接访问',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [320, 302, 301, 334, 390, 330, 320]
		        },
		        {
		            name: '邮件营销',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [120, 132, 101, 134, 90, 230, 210]
		        },
		        {
		            name: '联盟广告',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [220, 182, 191, 234, 290, 330, 310]
		        },
		        {
		            name: '视频广告',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [150, 212, 201, 154, 190, 330, 410]
		        }
		    ]
	};

	chart5 = echarts.init(document.getElementById('chart5')); 
	chart5.setOption(option); 
}
chart1();
chart2();
chart3();
chart4();
chart5();