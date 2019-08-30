layui.use(['table'], function(table) {
	
	var chart1,chart2,chart3,chart4,chart5,chart10;
	var option1,option2,option3,option4,option5,option10;
	
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
		    /*tooltip : {
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
		    },*/
		    bmap: {center: [100.030020,37.281610],zoom:11,roam: false},
		    series : [
		        {
		            name: '各畜种存栏数据',
		            type: 'effectScatter',
		            coordinateSystem: 'bmap',
		            data: convertData(data.sort(function (a, b) {
		                return b.value - a.value;
		            }).slice(0, 6)),
		            symbolSize: function (val) {
		                return 30;
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
		    title : {text: '刚察县养殖场各畜种存栏占比图',x:'center',subtext: '单位：头、只'},
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
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 50,
		        bottom: 20,
		        data: ['']
		    },
		    series : [
		        {
		            name: '存栏数',
		            type: 'pie',
		            radius : '50%',
		            center: ['45%', '55%'],
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
		    toolbox: {
		        feature: {
		            saveAsImage: {show: true}
		        }
		    },
		    legend: {
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 50,
		        bottom: 20,
		        data: ['']
		    },
		    series : [
		        {
		            name: '面积',
		            type: 'pie',
		            radius : '50%',
		            center: ['45%', '55%'],
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
		    toolbox: {
		        feature: {
		            saveAsImage: {show: true}
		        }
		    },
		    legend: {
		        type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 50,
		        bottom: 20,
		        data: ['']
		    },
		    series : [
		        {
		            name: '总产',
		            type: 'pie',
		            radius : '50%',
		            center: ['45%', '55%'],
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
				title : {text: '刚察县'+curyear+'畜牧业产量统计图',x:'left'},
			    tooltip : {trigger: 'axis',axisPointer : {type : 'shadow'}},
			    toolbox: {
			        feature: {
			            saveAsImage: {show: true}
			        }
			    },
			    legend: {
			        data: ['肉产量', '奶产量','蛋产量','毛产量'],x:'center'},
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    yAxis:  {
			        type: 'value',
			        name: '单位(吨)'
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
		
		option10 = {
				title : {text: '畜种存栏数统计图',x:'center'},
				yAxis:  {type: 'value',name: '单位(万头)'},
			    xAxis: {
			        type: 'category',
			        data: ['']
			    },
			    series: [{
			        data: [''],
			        type: 'bar'
			    }]
		};
		chart10 = echarts.init(document.getElementById('chart10')); 
		chart10.setOption(option10); 
	}
	function mousemove(params){
		if($("#chartDiv").is(':hidden')){
			option10.title.text=params.name+'畜种存栏数统计图';
			$("#chartDiv").css("top",params.event.offsetY-230<0?params.event.offsetY+90:params.event.offsetY-230);
			$("#chartDiv").css("left",params.event.offsetX-160);
			$("#chartDiv").show();
			
			if(params.data.data&&params.data.data.length>0){
				var fl=option10.xAxis.data;
				var cl=[];
				for (var i = 0; i < fl.length; i++) {
					cl.push('');
					for (var j = 0; j < params.data.data.length; j++) {
						if(fl[i]==params.data.data[j].type_name){
							cl[i]=params.data.data[j].animals_size;
							break;
						}
					}
				}
				option10.series[0].data=cl;
			}
			chart10.dispose();
		    chart10 = echarts.init(document.getElementById('chart10'));
		    chart10.setOption(option10);
		}
	}
	function mouseout(params){
		if(!$("#chartDiv").is(':hidden')){
			$("#chartDiv").hide();
		}
	}
	function farmdata(){
		$.post("/farminfo/getTypes", {},function(res){
			if(res&&res.length>0){
				var d=[];
				for (var i = 0; i < res.length; i++) {
					d.push(res[i].type_name);
				}
				option10.xAxis.data=d;
			}
		});
		
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
	      	  chart1.on('mousemove', function (params) {
	      		  mousemove(params);
		      });
	          chart1.on('mouseout', function (params) {
	        	  mouseout(params);
		      });
	        }
	        if(res && res.piedata && res.piedata.length>0){
	        	var tabledata=[];
	        	$.each(res.piedata,function(index,item){
	        		option2.legend.data.push(item.name);
	        		tabledata.push({zbmc:item.name,cls:item.value});
	        	});
	        	option2.series[0].data=res.piedata;
	    	    chart2.dispose();
	    	    chart2 = echarts.init(document.getElementById('chart2'));
	    	    chart2.setOption(option2); 
	    	    inittable2(tabledata);
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
	      	    
	      	    var tabledata=[];
	      	    for (var i = 0; i < 12; i++) {
	      	    	tabledata.push({ym:option5.xAxis.data[i],
	      	    		meat_output:option5.series[0].data[i],milk_output:option5.series[1].data[i],
	      	    		egg_output:option5.series[2].data[i],hair_output:option5.series[3].data[i]});
				}
	      	    inittable5(tabledata);
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
	    	    
	    	    inittable3(res.planted_area);
	    	    inittable4(res.planted_output);
	        }
	    });
	}
	function inittable2(data){
		//表格渲染
		datatable=table.render({
			id:"datalist2",
		    elem: '#datalist2',
		    page:false,
		    limit:200,
		    height:360,
		    cols: [[ //表头
		      {field: 'zbmc', title: '指标名称',align:'center'},
			  {field: 'cls', title: '存栏数',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable3(data){
		//表格渲染
		datatable=table.render({
			id:"datalist3",
		    elem: '#datalist3',
		    page:false,
		    limit:200,
		    height:360,
		    cols: [[ //表头
		      {field: 'name', title: '农作物名称',align:'center'},
			  {field: 'value', title: '播种面积',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable4(data){
		//表格渲染
		datatable=table.render({
			id:"datalist4",
		    elem: '#datalist4',
		    page:false,
		    limit:200,
		    height:360,
		    cols: [[ //表头
		      {field: 'name', title: '农作物名称',align:'center'},
			  {field: 'value', title: '总产(万公斤)',align:'center'}
		    ]],
		    data:data
		});
	}
	function inittable5(data){
		//表格渲染
		datatable=table.render({
			id:"datalist5",
		    elem: '#datalist5',
		    page:false,
		    limit:200,
		    height:360,
		    cols: [[ //表头
		      {field: 'ym', title: '月份',align:'center'},
		      {field: 'meat_output', title: '肉产量',align:'center'},
			  {field: 'milk_output', title: '奶产量',align:'center'},
			  {field: 'egg_output', title: '蛋产量',align:'center'},
			  {field: 'hair_output', title: '毛产量',align:'center'}
		    ]],
		    data:data
		});
	}
	
	function bindEvent(){
		$("div.layui-card-header a").click(function(){
			var ind=$(this).attr("ind");
			if($("#chart"+ind).is(':hidden')){
				$(this).text("数据详情");
				$("#chart"+ind).show();
				$("#datalistdiv"+ind).hide();
			}else{
				$(this).text("<返回");
				$("#chart"+ind).hide();
				$("#datalistdiv"+ind).show();
			}
		});
	}
	
	function monitorinfo(){
		 $.post("/monitorManage/findmonitorinfo", {},function(res){
		        if(res && res.length>0){
		        	$("#monitorSpan",parent.document).show();
		        	$(".box",parent.document).show();
		        	var str="";
		        	$.each(res,function(index,item){
		        		str+="<p class='item'>"+item+"</p>";
		        	});
		        	$("#monitorinfo",parent.document).html(str);
		        } else {
		        	$("#monitorinfo",parent.document).html("");
		        	$("#monitorSpan",parent.document).hide();
		        	$(".box",parent.document).hide();
		        }
		    });
	}
	
	monitorinfo();
	initchart();
	farmdata();
	animalsBreedData();
	cropsplantData();
	bindEvent();
});