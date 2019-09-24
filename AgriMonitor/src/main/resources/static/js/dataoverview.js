layui.use(['table'], function(table) {
	
	var chart1,option1;
	
	var xzdata=[{name: '沙柳河镇', size:100},{name: '伊克乌兰乡', size:100},{name: '泉吉乡', size:100},
		{name: '吉尔孟乡', size:100},{name: '哈尔盖镇', size:100}];
	var curyear=new Date().getFullYear();
	var data = [{name: '新海', size:40},{name: '思乃', size:40},{name: '果洛仓贡麻', size:40},
		{name: '潘保', size:40},{name: '红山', size:40},{name: '尕曲', size:40},
		{name: '河东', size:40},{name: '公贡麻', size:40},{name: '亚秀麻', size:40},
		{name: '环仓秀麻', size:40},{name: '果洛藏秀麻', size:40},
		{name: '切察', size:40},{name: '察拉', size:40},{name: '塘渠', size:40},
		{name: '贡麻', size:40},{name: '亚秀', size:40},{name: '亚贡麻', size:40},
		{name: '尚木多', size:40},{name: '角什科贡麻', size:40},{name: '角什科秀麻', size:40},
		{name: '合茂', size:40},{name: '扎苏合', size:40},{name: '年乃索麻', size:40},
		{name: '宁夏', size:40},{name: '切吉', size:40},{name: '新泉', size:40},
		{name: '日芒', size:40},{name: '环仓贡麻', size:40},{name: '向阳', size:40},
		{name: '秀脑贡麻', size:40},{name: '秀脑麻', size:40},{name: '黄玉农场', size:40}];
	var xzCoordMap={'沙柳河镇':[100.15841,37.355063],'伊克乌兰乡':[100.09333,37.311518],'泉吉乡':[99.846799,37.294324],
			'吉尔孟乡':[99.619424,37.188703],'哈尔盖镇':[100.417701,37.261455]};
	var geoCoordMap = {'新海':[100.143628,37.311171],'思乃':[100.129783,37.313673],'果洛仓贡麻':[100.137907,37.331978],
			'潘保':[100.15874,37.3371],'红山':[100.157499,37.317058],'尕曲':[100.109484,37.325127],
			'河东':[100.132599,37.32628],'公贡麻':[100.468444,37.243889],'亚秀麻':[100.417294,37.241782],
			'环仓秀麻':[100.499189,37.311767],'果洛藏秀麻':[100.411308,37.245003],
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
	var convertData1 = function (data) {
	    var res = [];
	    for (var i = 0; i < data.length; i++) {
	        var geoCoord = xzCoordMap[data[i].name];
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
		        		+"人口数量(人)："+(param.data.data.rksl||'-')+"人<br/>"
		        		+"国土面积："+(param.data.data.gtmj||'-')+"平方千米<br/>"
		        		+"耕地面积："+(param.data.data.gdmj||'-')+"亩<br/>"
		        		+"高标准农田面积："+(param.data.data.rksl||'-')+"亩<br/>"
		        		+"草场面积："+(param.data.data.gbzntmj||'-')+"亩<br/>"
		        		+"农作物种类："+(param.data.data.nzwzl||'-')+"<br/>"
		        		+"年种植面积："+(param.data.data.nzzmj||'-')+"亩<br/>"
		        		+"种植方式："+(param.data.data.zzfs||'-');
		        		return s;
		        	}else if(param.data.name=='哈尔盖镇'){
		        		return "哈尔盖镇是环青海湖地区最大的一个畜牧业镇，全镇土地总面积为1688平方公里，<br/>"
		        			  +"拥有可利用草场224.24万亩，耕地6.44万亩，经营各类牲畜278044头"
		        			  +"只、匹）。（<br/>全镇辖7个行政村、1个社区、24个生产合作社，共2630户10108"
		        			  +"人，其中藏族人口<br/>占总人口的91.6%，是一个以藏族为主的少数民族聚居地。境内"
		        			  +"交通便捷，青藏铁路、<br/>哈尔盖至木里地方铁路、国道315线、省道204线贯穿全境，"
		        			  +"经济、文化、信息交流<br/>联系便利，区位优势明显。境内有普氏原羚（中华对角羚）、"
		        			  +"岩羊、鹿、麝、冬虫<br/>夏草、黄芪、大黄、羌活、柴胡等野生动植物资源；有煤、铁、"
		        			  +"铜、银、铀等矿产<br/>资源。镇机关下属兽医站、学校、卫生院、社会发展中心、经济发展"
		        			  +"中心、水管所、<br/>公安派出所、森林公安派出所、司法所等9个镇属单位。全镇共有藏传"
		        			  +"佛教寺院3所<br/>，僧侣73人。全镇共有干部职工73名，其中女干部职工29人。镇党委"
		        			  +"下设9个党支<br/>部，党员总数共302名，女党员44名，占党员总数的15%；少数民族党员259名，<br/>占党员总数的86%。";
		        	}else if(param.data.name=='吉尔孟乡'){
		        		return "吉尔孟乡坐落于全国最美五大湖之首的“青海湖”西北岸，位于刚察县以西60公里处，<br/>"
		        		      +"地处海北州刚察县、海西州天峻县、海南州共和县三州三县交界处，地区总面积<br/>1463.6平方"
		        		      +"公里，境内有岩羊、鹿、麝、冬虫夏草、柴胡等野生动植物资源和煤、<br/>铁、铜等矿产资源。"
		        		      +"全乡辖5个行政村（牧业村：日芒村、环仓贡麻村、秀脑贡<br/>麻村、秀脑秀麻村；农业村：向阳村），"
		        		      +"15个生产合作社，有农牧户1072户<br/>3895人，其中：牧业1038户3809人，农业34户86人，"
		        		      +"是以藏族为主的少数民族聚居<br/>区，有藏、汉、回、蒙、土五个民族。全乡可利用草场188.43万亩，"
		        		      +"冬春草场100.48<br/>万亩，夏秋草场87.95万亩，存栏各类牲畜29.34万头，2018年全乡农牧民人"
		        		      +"均可支配<br/>收入13933元。全乡有党支部6个，共有党员188名（其中，1个机关党支部，5个村党<br/>支部），"
		        		      +"农牧民党员158名，机关党支部党员30名。全乡有干部职工36名（其中领导<br/>干部7名，经济发展服务中"
		        		      +"心干部7名，选调生行政编4人，党建信息员6名，寺院指导员<br/>3名，见习岗位4名，三支一扶2名，青南计划1名，"
		        		      +"公益性岗位2名）；乡属站所6个：<br/>乡兽医站、乡派出所、乡司法所、乡中心卫生院、乡幼儿园、乡水利工作站。"
		        		      +"乡辖藏传<br/>佛教寺院3座（日芒寺、秀脑寺、环仓寺），有活佛1名，僧侣96名。";
	        	    }else if(param.data.name=='泉吉乡'){
		        		return "泉吉乡辖6个行政村（其中1个农业村、5个牧业村），1个社区，1438户5696人，劳动力2936人，<br/>"
		        		      +"有藏、汉、蒙、回、土、满、东乡、撒拉8个民族，藏族占总人口的90％。乡属站所8个，其中学<br/>校3所"
		        		      +"（小学1所、幼儿园2所）、畜牧兽医工作站、卫生院、农村信用合作社、派出所、水管所各<br/>1所。可利用草场"
		        		      +"195.69万亩（其中冬春草场93.16万亩，夏秋草场102.53万亩），农作物耕地<br/>3148亩。经营各类牲畜"
		        		      +"18.86万只（头、匹），其中羊15.83万只、牛2.71万头、马3204匹，畜牧<br/>业为主导产业。农村常住居民"
		        		      +"人均可支配收入达16749.56元（其中牧区人均可支配收入达<br/>18025.55元，农区人均可支配收入达9652.25元）。";
	        	    }else if(param.data.name=='伊克乌兰乡'){
		        		return "伊克乌兰系蒙语，意为:“红色的河”，建政于1958年。位于县境中部偏西，东南与青海湖相连，西以泉吉<br/>"
		        		      +"河为界与泉吉乡毗邻，北以大通河为界与祁连县默勒镇相望。境内山脉走向北西，北部高山连绵，南部低<br/>缓，"
		        		      +"自北西向南倾斜，绝大部分地区海拔在3300－3800米以上。最高点4700米，位于瓦颜山峰，最低点<br/>"
		        		      +"3195米，位于青海湖滨湖地带的沙柳河（伊克乌兰河）入湖处，海拔相对高差1580米。境内高山、丘陵<br/>大致"
		        		      +"成北、中、南排列。北部为高山地段，中部丘陵，南部为地势较缓地带。气候属典型的高原大陆性<br/>气候，日照时间长，"
		        		      +"昼夜温差大；年降水量370.5毫米，年蒸发量1500.6—1847.8毫米。冬季寒冷，夏<br/>秋温凉，1月平均气温"
		        		      +"-17.5℃，7月平均气温11℃，年平均气温-0.6℃。系全县唯一的纯牧业乡，下辖<br/>6个行政村，16个社，1754"
		        		      +"户，6186人，以藏族为主，占总人口的98%。全乡土地总面积1778.7平方<br/>公里，占全县土地总面积的23.3％。"
		        		      +"草场总面积266.798万亩，可利用草场面积194.96万亩，占草场总<br/>面积的81.09%。畜均占有草场10.04"
		        		      +"亩，全乡牲畜存栏21.436万头只，母畜比例、仔畜繁活率、商品率<br/>分别达到61.54%、88.55%、44.58%。"
		        		      +"全乡出栏各类牲畜10.5万（头只匹），全年出售各类牲畜9.6万<br/>头（只）。村民收入主要依靠传统种养殖、外出务工、"
		        		      +"转移性收入为主，2018年农牧民人均收入达<br/>13286.67元，农牧业增加值6310.66万元，2017年全乡提前实现全面脱贫。";
	        	    }else{
		        		return "无数据";
		        	}
				}
		    },
		    bmap: {center: [100.0000020,37.221610],zoom:11,roam: true},
		    series : [
		        {
		            name: '农业基本信息',
		            type: 'scatter',
		            coordinateSystem: 'bmap',
		            data: convertData(data),
		            symbolSize: function (val) {return 10;},
		            showEffectOn: 'render',
		            rippleEffect: {brushType: 'stroke'},
		            hoverAnimation: true,
		            label: {
		                normal: {formatter: '{b}',position: 'right',show: true}
		            },
		            itemStyle: {normal: {color: 'blue'}},
		            zlevel: 1
		        },
		        {
		            name: '农业基本信息',
		            type: 'effectScatter',
		            coordinateSystem: 'bmap',
		            data: convertData1(xzdata),
		            symbolSize: function (val) {return 15;},
		            showEffectOn: 'render',
		            rippleEffect: {brushType: 'stroke'},
		            hoverAnimation: true,
		            label: {
		                normal: {formatter: '{b}',position: 'right',show: true}
		            },
		            itemStyle: {normal: {color: 'purple'}},
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