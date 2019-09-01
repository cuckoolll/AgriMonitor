layui.use(['table'], function(table) {
	
	var chart1,option1;
	
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
		$("#chart1").height($(window).height());
		option1 = {
		   /* tooltip : {
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
		    bmap: {center: [100.030020,37.221610],zoom:11,roam: false},
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
		chart1.on('mousemove', function (params) {
    		  mousemove(params);
	    });
        chart1.on('mouseout', function (params) {
      	  	mouseout(params);
	    });
	}

	function mousemove(params){
		if($("#chartDiv").is(':hidden')){
			$("#chartDiv p").hide();
			$("#chartDiv").css("top",params.event.offsetY-380<0?params.event.offsetY+50:params.event.offsetY-380);
			$("#chartDiv").css("left",params.event.offsetX-160);
			$("#chartDiv").show();
			
			if(params.data.data&&params.data.data.length>0){
				if(params.name=='沙柳河镇'){
					$("#slhz").show();
				}else if(params.name=='哈尔盖镇'){
					$("#hrgz").show();
				}else if(params.name=='伊克乌兰乡'){
					$("#ykwlx").show();
				}else if(params.name=='泉吉乡'){
					$("#qjx").show();
				}else if(params.name=='吉尔孟乡'){
					$("#jrmx").show();
				}else if(params.name=='黄玉农场'){
					$("#hync").show();
				}
			}
		}
	}
	
	function mouseout(params){
		if(!$("#chartDiv").is(':hidden')){
			$("#chartDiv").hide();
		}
	}
	
	function agridata(){
		$.post("/agriBaseinfo/datalist", {page:1,limit:500},function(res){
	        if(res && res.data && res.data.length>0){
	        	$.each(res.data,function(index,item){
	        		if(item.towns=='沙柳河镇'){
	        			if($("#slhz").text()=='无数据...'){
	        				$("#slhz").text('');
	        			}
	        			/*var str="下辖地"+item.village+"人口"+item.rksl+"人，国土面积"
								+item.gtmj+"平方千米，耕地面积"+item.gdmj+"亩，高标准农田面积"
								+item.gbzntmj+"亩，草场面积"+item.ccmj+"亩，农作物种类有"
								+item.nzwzl+"，年种植面积"+item.nzzmj+"亩，种植方式为"+item.zzfs+"。<br>";*/
	        			var str="下辖地"+item.village+"人口"+item.rksl+"人，国土面积"
								+item.gtmj+"平方千米，耕地面积"+item.gdmj+"亩。<br>";
						$("#slhz").html($("#slhz").html()+str);
					}else if(item.towns=='哈尔盖镇'){
						if($("#hrgz").text()=='无数据...'){
	        				$("#hrgz").text('');
	        			}
						$("#hrgz").html( $("#hrgz").html()+str);
					}else if(item.towns=='伊克乌兰乡'){
						if($("#ykwlx").text()=='无数据...'){
	        				$("#ykwlx").text('');
	        			}
						$("#ykwlx").html( $("#ykwlx").html()+str);
					}else if(item.towns=='泉吉乡'){
						if($("#qjx").text()=='无数据...'){
	        				$("#qjx").text('');
	        			}
						$("#qjx").html( $("#qjx").html()+str);
					}else if(item.towns=='吉尔孟乡'){
						if($("#jrmx").text()=='无数据...'){
	        				$("jrmx").text('');
	        			}
						$("#jrmx").html( $("#jrmx").html()+str);
					}else if(item.towns=='黄玉农场'){
						if($("#hync").text()=='无数据...'){
	        				$("#hync").text('');
	        			}
						$("#hync").html( $("#hync").html()+str);
					}
	        	});
	        }
	    });
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
	      	  
		      	chart1.on('mousemove', function (params) {
		    		  mousemove(params);
			    });
		        chart1.on('mouseout', function (params) {
		      	  	mouseout(params);
			    });
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
		        		if(item.ratio==0 || item.ratio>=1){
		        			str+="<p class='item'>"+item.log+"</p>";
		        		}else if(item.ratio>=0.5 && item.ratio<1){
		        			str+="<p class='item' style='color: orange;'>"+item.log+"</p>";
		        		}else if(item.ratio>=0.1 && item.ratio<0.5){
		        			str+="<p class='item' style='color: yellow;'>"+item.log+"</p>";
		        		}else if(item.ratio<0.1){
		        			str+="<p class='item' style='color: blue;'>"+item.log+"</p>";
		        		}else{
		        			str+="<p class='item'>"+item.log+"</p>";
		        		}
		        	});
		        	$("#monitorinfo",parent.document).html(str);
		        } else {
		        	$("#monitorinfo",parent.document).html("");
		        	$("#monitorSpan",parent.document).hide();
		        	$(".box",parent.document).hide();
		        }
		    });
	}
	agridata();
	monitorinfo();
	initchart();
	farmdata();
});