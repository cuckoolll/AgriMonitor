layui.use(['table'], function(table) {
	
	var chart1,option1;
	
	var curyear=new Date().getFullYear();
	var data = [{name: '新海', size:40},{name: '思乃', size:40},{name: '果洛仓贡麻', size:40},
		{name: '潘保', size:40},{name: '红山', size:40},{name: '尕曲', size:40},
		{name: '河东', size:40},{name: '公贡麻', size:40},{name: '亚秀麻', size:40},
		{name: '环仓秀麻', size:40},{name: '果洛', size:40},{name: '藏秀麻', size:40},
		{name: '切察', size:40},{name: '察拉', size:40},{name: '塘渠', size:40},
		{name: '贡麻', size:40},{name: '亚秀', size:40},{name: '亚贡麻', size:40},
		{name: '尚木多', size:40},{name: '角什科贡麻', size:40},{name: '角什科秀麻', size:40},
		{name: '合茂', size:40},{name: '扎苏合', size:40},{name: '年乃索麻', size:40},
		{name: '宁夏', size:40},{name: '切吉', size:40},{name: '新泉', size:40},
		{name: '日芒', size:40},{name: '环仓贡麻', size:40},{name: '向阳', size:40},
		{name: '秀脑贡麻', size:40},{name: '秀脑麻', size:40},{name: '黄玉农场', size:40}];
	var geoCoordMap = {'新海':[100.143628,37.311171],'思乃':[100.129783,37.313673],'果洛仓贡麻':[100.137907,37.331978],
			'潘保':[100.15874,37.3371],'红山':[100.157499,37.317058],'尕曲':[100.109484,37.325127],
			'河东':[100.132599,37.32628],'公贡麻':[100.468444,37.243889],'亚秀麻':[100.417294,37.241782],
			'环仓秀麻':[100.499189,37.311767],'果洛':[100.428021,37.246597],'藏秀麻':[100.411308,37.245003],
			'切察':[100.424016,37.237044],'察拉':[100.440293,37.249555],'塘渠':[100.424751,37.230313],
			'贡麻':[100.096395,37.329193],'亚秀':[100.009094,37.322635],'亚贡麻':[100.090446,37.32198],
			'尚木多':[100.088397,37.313331],'角什科贡麻':[100.015093,37.300997],'角什科秀麻':[99.986676,37.259197],
			'合茂':[99.875591,37.281508],'扎苏合':[99.799627,37.255552],'年乃索麻':[99.726555,37.054731],
			'宁夏':[99.88031,37.259212],'切吉':[99.873005,37.277013],'新泉':[99.893959,37.274791],
			'日芒':[99.634181,37.237279],'环仓贡麻':[99.578011,37.15639],'向阳':[99.551822,37.159719],
			'秀脑贡麻':[99.554105,37.31428],'秀脑麻':[99.590886,37.168757],'黄玉农场':[99.95554,37.250184]};
	
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
		    tooltip : {
		        trigger: 'item',
		        formatter: function(param){
		        	if(param.data.data){
		        		var s=param.name+"<br/>"
		        		+"人口数量(人)："+(param.data.data.rksl||'-')+"<br/>"
		        		+"国土面积："+(param.data.data.gtmj||'-')+"<br/>"
		        		+"耕地面积："+(param.data.data.gdmj||'-')+"<br/>"
		        		+"高标准农田面积："+(param.data.data.rksl||'-')+"<br/>"
		        		+"草场面积："+(param.data.data.gbzntmj||'-')+"<br/>"
		        		+"农作物种类："+(param.data.data.nzwzl||'-')+"<br/>"
		        		+"年种植面积："+(param.data.data.nzzmj||'-')+"<br/>"
		        		+"种植方式："+(param.data.data.zzfs||'-');
		        		return s;
		        	}else{
		        		return "无数据";
		        	}
				}
		    },
		    bmap: {center: [100.080020,37.221610],zoom:11,roam: true},
		    series : [
		        {
		            name: '农业基本信息',
		            type: 'effectScatter',
		            coordinateSystem: 'bmap',
		            data: convertData(data),
		            symbolSize: function (val) {
		                return 10;
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
		/*chart1.on('mousemove', function (params) {
    		  mousemove(params);
	    });
        chart1.on('mouseout', function (params) {
      	  	mouseout(params);
	    });*/
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
		$.post("/agriBaseinfo/find4Maps", {},function(res){
	        if(res && res.length>0){
	        	$.each(data,function(index,item){
	        		$.each(res,function(i,o){
	        			if(item.name==o.name){
	            			data[index]=o;
	            			return false;
	            		}
	        		});
	        	});
	      	  option1.series[0].data=convertData(data);
	      	  chart1.dispose();
	      	  chart1 = echarts.init(document.getElementById('chart1'));
	      	  chart1.setOption(option1); 
	      	  
		      	/*chart1.on('mousemove', function (params) {
		    		  mousemove(params);
			    });
		        chart1.on('mouseout', function (params) {
		      	  	mouseout(params);
			    });*/
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
	//agridata();
	monitorinfo();
	initchart();
	farmdata();
});