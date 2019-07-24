var chart1,chart2,chart3,chart4,chart5;
var option1,option2,option3,option4,option5;

var curyear=new Date().getFullYear();
var data = [{name: '沙柳河镇', size:100},{name: '伊克乌兰乡', size:100},{name: '泉吉乡', size:100},{name: '吉尔孟乡', size:100},{name: '黄玉农场', size:100},{name: '哈尔盖镇', size:100}];
var geoCoordMap = {'沙柳河镇':[100.15841,37.355063],'伊克乌兰乡':[100.09333,37.311518],'泉吉乡':[99.846799,37.294324],'吉尔孟乡':[99.619424,37.188703],'黄玉农场':[99.95554,37.250184],'哈尔盖镇':[100.417701,37.261455]};

var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var geoCoord = geoCoordMap[data[i].name];
        if (geoCoord) {
            res.push({
                name: data[i].name,value: geoCoord.concat(data[i].size),data:data[i].data
            });
        }
    }
    return res;
};
	
function initchart(){
	option1 = {
	    title: {text: '刚察县'+curyear+'养殖场畜种存栏数据统计图',left: 'center'},
	    tooltip : {
	        trigger: 'item',
	        formatter: function(param){
	        	if(param.data.data){
	        		var s=param.name;
	        		$.each(param.data.data,function(index,item){
	            		s+="<br/>"+item.type_name+":"+item.animals_size
	            	});
	        		return s;
	        	}else{
	        		return "无数据";
	        	}
			}
	    },
	    bmap: {center: [100.030020,37.235610],zoom:10,roam: true},
	    series : [
	        {
	            name: '各畜种存栏数据',
	            type: 'effectScatter',
	            coordinateSystem: 'bmap',
	            data: convertData(data.sort(function (a, b) {
	                return b.value - a.value;
	            }).slice(0, 6)),
	            symbolSize: function (val) {
	                return val[2] / 10;
	            },
	            showEffectOn: 'render',
	            rippleEffect: {brushType: 'stroke'},
	            hoverAnimation: true,
	            label: {
	                normal: {formatter: '{b}',position: 'right',show: true}
	            },
	            itemStyle: {
	                normal: {color: 'purple',shadowBlur: 10,shadowColor: '#333'}
	            },
	            zlevel: 1
	        }
	    ]
	};
      
	chart1 = echarts.init(document.getElementById('chart1')); 
	chart1.setOption(option1); 
	/**-----------------------------------------**/
	option2 = {
	    title : {text: '刚察县'+curyear+'养殖场各畜种存栏占比图',x:'center',subtext: '单位：头、只'},
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
	        data: ['']
	    },
	    series : [
	        {
	            name: '存栏数',
	            type: 'pie',
	            radius : '70%',
	            center: ['40%', '52%'],
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

	chart2 = echarts.init(document.getElementById('chart2')); 
	chart2.setOption(option2); 
	/**-----------------------------------------**/
	option3 = {
	    title : {text: '刚察县'+curyear+'各农作物播种面积占比图',x:'center',subtext: '单位：万亩'},
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
	        data: ['']
	    },
	    series : [
	        {
	            name: '面积',
	            type: 'pie',
	            radius : '70%',
	            center: ['40%', '52%'],
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

	chart3 = echarts.init(document.getElementById('chart3')); 
	chart3.setOption(option3); 
	/**-----------------------------------------**/
	option4 = {
	    title : {text: '刚察县'+curyear+'各农作物总产占比图',x:'center',subtext: '单位：万公斤'},
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
	        data: ['']
	    },
	    series : [
	        {
	            name: '总产',
	            type: 'pie',
	            radius : '70%',
	            center: ['40%', '52%'],
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

	chart4 = echarts.init(document.getElementById('chart4')); 
	chart4.setOption(option4); 
	/**-----------------------------------------**/
	option5 = {
			title : {text: '刚察县'+curyear+'畜牧业产量统计图',x:'center'},
		    tooltip : {trigger: 'axis',axisPointer : {type : 'shadow'}},
		    legend: {
		        data: ['肉产量', '奶产量','蛋产量','毛产量'],x:'right'},
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    yAxis:  {
		        type: 'value',
		        name: '产量(吨)'
		    },
		    xAxis: {
		        type: 'category',
		        data: [curyear+'01',curyear+'03',curyear+'03',curyear+'04',
		        	curyear+'05',curyear+'06',curyear+'07',curyear+'08',curyear+'09',
		        	curyear+'10',curyear+'11',curyear+'12']
		    },
		    series: [
		        {
		            name: '肉产量',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: { show: true,position: 'insideRight'}
		            },
		            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		        },
		        {
		            name: '奶产量',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		        },
		        {
		            name: '蛋产量',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		        },
		        {
		            name: '毛产量',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'insideRight'
		                }
		            },
		            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		        }
		    ]
	};

	chart5 = echarts.init(document.getElementById('chart5')); 
	chart5.setOption(option5); 
}

function farmdata(){
	$.post("/farminfo/findSumGroupTowns", {},function(res){
        if(res && res.mapdata && res.mapdata.length>0){
        	$.each(data,function(index,item){
        		$.each(res.mapdata,function(i,o){
            		if(item.name==o.name){
            			data[index]=o;
            			return false;
            		}
            	});
        	});
      	  option1.series[0].data=convertData(data.sort(function (a, b) {
              return b.value - a.value;
          }).slice(0, 6));
      	  chart1.dispose();
      	  chart1 = echarts.init(document.getElementById('chart1'));
      	  chart1.setOption(option1); 
        }
        if(res && res.piedata && res.piedata.length>0){
        	$.each(res.piedata,function(index,item){
        		option2.legend.data.push(item.name);
        	});
        	option2.series[0].data=res.piedata;
    	    chart2.dispose();
    	    chart2 = echarts.init(document.getElementById('chart2'));
    	    chart2.setOption(option2); 
        }
    });
}
function animalsBreedData(){
	$.post("/animalsBreed/getSumGroupYear", {},function(res){
        if(res){
        	option5.title.text='刚察县'+res.year+'畜牧业产量统计图';
      	    option5.series[0].data=res.meat_output;
        	option5.series[1].data=res.milk_output;
        	option5.series[2].data=res.egg_output;
        	option5.series[3].data=res.hair_output;
      	    chart5.dispose();
      	    chart5 = echarts.init(document.getElementById('chart5'));
      	    chart5.setOption(option5); 
        }
    });
}
function cropsplantData(){
	$.post("/cropsplant/findSumGroupType", {},function(res){
        if(res){
        	option3.title.text='刚察县'+res.year+'各农作物播种面积占比图';
        	option3.legend.data=res.names;
      	    option3.series[0].data=res.planted_area;
      	    chart3.dispose();
      	    chart3 = echarts.init(document.getElementById('chart3'));
      	    chart3.setOption(option3); 
      	    
      	    option4.title.text='刚察县'+res.year+'各农作物总产占比图';
      	    option4.legend.data=res.names;
    	    option4.series[0].data=res.planted_output;
    	    chart4.dispose();
    	    chart4 = echarts.init(document.getElementById('chart4'));
    	    chart4.setOption(option4); 
        }
    });
}
initchart();
farmdata();
animalsBreedData();
cropsplantData();