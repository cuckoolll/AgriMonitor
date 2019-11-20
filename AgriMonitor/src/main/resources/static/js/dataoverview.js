layui.use(['table','util'], function(table,util) {
	var curyear=util.toDateString(new Date(), 'yyyy');
	
	var chart1,option1;
	
	var xzdata=[{name: '沙柳河镇', size:100},{name: '伊克乌兰乡', size:100},{name: '泉吉乡', size:100},
		{name: '吉尔孟乡', size:100},{name: '哈尔盖镇', size:100},{name: '黄玉农场', size:100}];
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
		{name: '秀脑贡麻', size:40},{name: '秀脑麻', size:40}];
	var xzCoordMap={'沙柳河镇':[100.15841,37.355063],'伊克乌兰乡':[100.09333,37.311518],'泉吉乡':[99.846799,37.294324],
			'吉尔孟乡':[99.619424,37.188703],'哈尔盖镇':[100.417701,37.261455],'黄玉农场':[99.95554,37.250184]};
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
			'秀脑贡麻':[99.554105,37.31428],'秀脑麻':[99.590886,37.168757]};
		var markPointData = [
    {
        "name": "刚察县",
        "coord": [100.152458,37.331048]
    }];
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
	function renderItem(params, api) {
	    var coords = [
	    	[100.631032, 37.294185],[100.62214, 37.303692],[100.618052, 37.314682],[100.615752, 37.349137],[100.605237, 37.378551],[100.609597, 37.383427],[100.599896, 37.405704],[100.588455, 37.412409],[100.585735, 37.419929],[100.595118, 37.429268],[100.582807, 37.447935],[100.583329, 37.457489],[100.571083, 37.470989],[100.565401, 37.491453],[100.558245, 37.49941],[100.564685, 37.508085],[100.563774, 37.514547],[100.556513, 37.51797],[100.545646, 37.538693],[100.528781, 37.554696],[100.535368, 37.571118],[100.548392, 37.575117],[100.548118, 37.583121],[100.559992, 37.591954],[100.561468, 37.596426],[100.542157, 37.615277],[100.517129, 37.622242],[100.500348, 37.623183],[100.488699, 37.638898],[100.457768, 37.656719],[100.461165, 37.671166],[100.458502, 37.676005],[100.474809, 37.678649],[100.483627, 37.68432],[100.491769, 37.680403],[100.511624, 37.682861],[100.525187, 37.706694],[100.52593, 37.721106],[100.517135, 37.729795],[100.515875, 37.742188],[100.520806, 37.751104],[100.513508, 37.759021],[100.511977, 37.770161],[100.501032, 37.779219],[100.489707, 37.783007],[100.492894, 37.790813],[100.490088, 37.794917],[100.46511, 37.804686],[100.466061, 37.816138],[100.457284, 37.822374],[100.457074, 37.831379],[100.445454, 37.834867],[100.442112, 37.843198],[100.435035, 37.843864],[100.414357, 37.835348],[100.405355, 37.841994],[100.394029, 37.842599],[100.388993, 37.856538],[100.374745, 37.856851],[100.364485, 37.865087],[100.323064, 37.875686],[100.313713, 37.883285],[100.299828, 37.884016],[100.284795, 37.877063],[100.268045, 37.874273],[100.250414, 37.88296],[100.225841, 37.881149],[100.216164, 37.884661],[100.190369, 37.885365],[100.173214, 37.900476],[100.16363, 37.902847],[100.158569, 37.910238],[100.137587, 37.913197],[100.119381, 37.931016],[100.089132, 37.944889],[100.084016, 37.948715],[100.082323, 37.955461],[100.063871, 37.959989],[100.056219, 37.967966],[100.042023, 37.970215],[100.033487, 37.96742],[100.021492, 37.968731],[100.000269, 37.977237],[99.986762, 37.984365],[99.980155, 37.99163],[99.97099, 37.993017],[99.945505, 38.008164],[99.903, 38.012125],[99.891937, 38.022389],[99.883368, 38.023897],[99.881109, 38.028444],[99.871377, 38.028201],[99.867981, 38.032767],[99.857359, 38.034628],[99.845744, 38.032389],[99.837258, 38.037135],[99.824813, 38.037296],[99.811033, 38.048197],[99.798599, 38.051711],[99.791546, 38.057549],[99.750104, 38.058989],[99.741546, 38.066332],[99.73011, 38.067566],[99.708718, 38.06276],[99.705914, 38.071058],[99.694561, 38.074039],[99.686614, 38.073711],[99.658303, 38.056705],[99.650009, 38.060842],[99.642822, 38.058471],[99.623026, 38.060807],[99.59076, 38.046231],[99.578559, 38.03776],[99.556201, 38.035052],[99.527209, 38.041802],[99.486117, 38.029805],[99.476478, 38.030479],[99.474497, 38.027088],[99.456684, 38.024203],[99.447353, 38.01935],[99.432619, 38.028241],[99.415519, 38.028507],[99.405784, 38.01939],[99.387143, 38.010874],[99.381273, 37.999628],[99.366204, 37.989726],[99.354302, 37.947979],[99.362604, 37.94202],[99.377466, 37.939593],[99.38486, 37.941365],[99.380426, 37.930099],[99.395163, 37.921384],[99.404782, 37.919824],[99.417904, 37.909084],[99.45584, 37.902484],[99.457043, 37.892722],[99.480971, 37.87762],[99.485618, 37.846823],[99.50345, 37.844718],[99.516756, 37.856022],[99.520557, 37.864607],[99.533513, 37.857117],[99.548323, 37.854992],[99.556017, 37.848199],[99.562577, 37.85155],[99.582774, 37.853089],[99.589216, 37.847836],[99.611055, 37.840339],[99.612821, 37.830679],[99.628763, 37.8309],[99.653608, 37.82426],[99.66917, 37.809723],[99.67937, 37.806365],[99.657219, 37.795068],[99.648805, 37.779981],[99.657626, 37.754901],[99.671251, 37.745017],[99.665813, 37.740564],[99.668641, 37.733495],[99.69418, 37.72656],[99.701961, 37.716498],[99.697741, 37.68263],[99.703214, 37.672336],[99.665734, 37.660445],[99.65417, 37.660306],[99.635487, 37.65138],[99.630544, 37.639749],[99.612731, 37.635245],[99.608443, 37.625679],[99.596326, 37.62328],[99.589505, 37.612356],[99.57138, 37.616117],[99.565084, 37.605241],[99.556462, 37.602939],[99.546309, 37.59513],[99.551299, 37.591199],[99.552197, 37.581143],[99.543576, 37.569583],[99.542288, 37.562259],[99.521912, 37.557797],[99.493908, 37.534179],[99.488548, 37.526849],[99.484066, 37.508227],[99.466858, 37.486551],[99.477441, 37.473204],[99.480761, 37.45228],[99.478419, 37.432672],[99.483929, 37.425906],[99.483859, 37.414163],[99.456028, 37.395002],[99.430241, 37.387011],[99.417545, 37.366627],[99.41519, 37.35483],[99.41583, 37.344472],[99.439945, 37.319369],[99.446205, 37.296624],[99.45248, 37.290169],[99.443812, 37.284775],[99.433318, 37.262598],[99.434206, 37.256973],[99.427226, 37.249952],[99.439983, 37.235441],[99.438623, 37.228673],[99.442309, 37.221965],[99.439829, 37.215182],[99.444084, 37.206229],[99.455013, 37.201466],[99.455046, 37.195844],[99.462888, 37.193501],[99.460114, 37.189857],[99.45965, 37.162429],[99.48137, 37.156815],[99.493992, 37.158162],[99.518298, 37.149557],[99.524285, 37.14115],[99.541789, 37.134913],[99.56011, 37.119695],[99.58361, 37.116026],[99.590656, 37.107137],[99.613165, 37.101646],[99.635555, 37.091776],[99.652892, 37.074956],[99.674902, 37.06982],[99.686433, 37.057158],[99.695097, 37.053726],[99.70402, 37.045121],[99.710707, 37.04771],[99.720165, 37.046918],[99.730674, 37.040664],[99.744033, 37.044186],[99.748864, 37.037376],[99.744835, 37.029323],[99.746956, 37.025931],[99.757774, 37.022188],[99.778398, 37.023988],[99.797608, 36.997973],[99.813139, 36.997767],[99.824835, 36.992551],[99.840689, 36.973584],[99.848068, 36.984821],[99.853114, 36.984319],[99.849793, 36.989987],[99.842227, 36.989813],[99.860578, 37.002464],[99.86345, 37.01114],[99.856136, 37.011437],[99.862023, 37.016557],[99.863707, 37.023734],[99.854363, 37.030358],[99.854961, 37.020906],[99.847074, 37.01509],[99.852701, 37.020693],[99.853508, 37.031261],[99.850107, 37.031733],[99.850972, 37.027226],[99.846451, 37.023414],[99.846985, 37.030386],[99.838979, 37.038926],[99.839008, 37.055579],[99.82164, 37.070924],[99.80181, 37.07672],[99.785988, 37.099146],[99.770796, 37.10891],[99.763145, 37.122956],[99.76299, 37.165935],[99.775353, 37.192127],[99.788285, 37.203346],[99.81182, 37.202443],[99.824663, 37.206685],[99.837819, 37.203225],[99.848475, 37.212249],[99.859739, 37.214642],[99.870464, 37.222159],[99.892237, 37.224538],[99.906624, 37.22198],[99.931063, 37.222889],[99.954081, 37.229551],[99.966483, 37.237518],[99.983201, 37.243303],[99.998838, 37.240422],[100.021532, 37.221313],[100.047446, 37.207339],[100.097199, 37.196497],[100.127764, 37.19716],[100.185585, 37.208304],[100.202628, 37.198946],[100.217521, 37.194795],[100.222049, 37.196987],[100.233912, 37.1952],[100.238445, 37.196695],[100.239351, 37.20658],[100.24586, 37.20669],[100.245323, 37.212784],[100.280629, 37.222069],[100.308832, 37.197886],[100.387276, 37.103882],[100.398741, 37.094001],[100.399049, 37.097472],[100.389253, 37.105095],[100.388447, 37.112978],[100.402502, 37.114828],[100.421838, 37.122027],[100.42942, 37.129648],[100.42891, 37.140221],[100.457719, 37.169829],[100.463867, 37.200115],[100.473339, 37.218149],[100.48868, 37.228833],[100.494322, 37.229406],[100.503149, 37.242118],[100.510866, 37.243781],[100.516135, 37.25103],[100.554776, 37.2449],[100.573038, 37.245677],[100.60946, 37.2591],[100.629378, 37.272121],[100.631032, 37.294185]
	    ];
	    var points = [];
	    for (var i = 0; i < coords.length; i++) {
	        points.push(api.coord(coords[i]));
	    }

	    return {
	        type: 'polygon',
	        shape: {
	            points: echarts.graphic.clipPointsByRect(points, {
	                x: params.coordSys.x,
	                y: params.coordSys.y,
	                width: params.coordSys.width,
	                height: params.coordSys.height
	            })
	        },
	        style: api.style({
	            fill: '#E6E6FA',
	            stroke: '#580000'
	        })
	    };
	}
	
	function initchart(){
		$("#chart1").height($(window).height());
		option1 = {
			title: {
		        text: '刚察县概况',
		        left: 'center',
		        top:10,
		        textStyle:{color:'red',fontSize:24,fontFamily:'楷体'}
		    },
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
	        	    }else if(param.data.name=='沙柳河镇'){
		        		return "沙柳河镇位于刚察县县境南部，县府驻地。人口13393人（2017），以汉族为主，还有藏、回等少数民族，<br/>"
	        		      +"面积1237.82平方千米。1957年设沙柳河乡，1958年同伊克乌兰区合并为伊克乌兰公社，1962年改为沙柳<br/>"
	        		      +"河乡，1969年改为沙柳河公社，1984年改为沙柳河乡。2001年3月5日青海省人民政府青政函[2001]15号<br/>"
	        		      +"文批复：撤销沙柳河乡，设立沙柳河镇。下辖新海、思乃、果洛仓贡麻、潘保、红山、尕曲、河东7个<br/>村（牧）委会。";
	        	    }else if(param.data.name=='刚察县'){
		        		return "刚察县为青海省海北藏族自治州辖县，省环湖重点牧业县之一，位于州境西部，青海湖北岸。<br/>" 
		        		+ "县政府驻沙柳河镇，距州府驻地海晏县西海镇110公里。截止2012年，全县总人口4.2万人 ，<br/>其中少数民族" 
		        		+ "占72.5%，藏族占63.38%，还有汉、蒙古、回、东乡等民族。面积1.2万平方公里<br/>。辖3乡2镇1场。古为羌地。" 
		        		+ "境内主要河流有默勒河、克克塞河、江仓河、沙柳河、巴哈乌兰河<br/>、布哈河、哈尔盖河、吉尔孟河。属高原大陆性气候，" 
		        		+ "年均温-0.6℃，年降水量370毫米。主要<br/>矿藏有煤、铁、铜、银、铀等。纯牧业县，牧养藏系绵羊，半细毛羊、牦牛、" 
		        		+ "马等。境内青海<br/>湖盛产湟鱼。青（海）藏（西藏）铁路横贯全境，青（海）新（疆）公路、湟"
		        		+"（源）嘉（峪关）<br/>公路通过境内，交通便利。 第一批国家农业可持续发展试验示范区。";
	        	    }else{
		        		return "无数据";
		        	}
				}
		    },
		    bmap: {center: [100.0000020,37.521610],zoom:10,roam: true},
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
		            itemStyle: {normal: {color: '#580000'}},
		            zlevel: 1,
					markPoint: { //图表标注。
		                "symbol": 'path://M512 39.384615l169.353846 295.384615 342.646154 63.015385-240.246154 248.123077L827.076923 984.615385l-315.076923-145.723077L196.923077 984.615385l43.323077-334.769231L0 401.723077l342.646154-63.015385L512 39.384615',
		                "symbolSize": 30,//图形大小
		                "label": {
		                    "normal": {
		                        "show": true,
		                    },
		                    "emphasis": {
		                        show: true,
		                    }
		                },
		                "itemStyle": {
		                    "normal": {
		                        "color": 'red'
		                    }
		                },
		                "data": markPointData
		            }
		        },
		        {
		            name: '农业基本信息',
		            type: 'scatter',
		            coordinateSystem: 'bmap',
		            data: convertData1(xzdata),
		            symbolSize: function (val) {return 15;},
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
		            type: 'custom',
		            coordinateSystem: 'bmap',
		            renderItem: renderItem,
		            itemStyle: {
		                normal: {
		                    opacity: 0.5
		                }
		            },
		            animation: false,
		            silent: true,
		            data: [0],
		            z: -10
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
		$.post("/agriBaseinfo/datalist", {date_year:curyear,date_year1:curyear,page:1,limit:500},function(res){
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
	        	$.each(res,function(i,o){
	        		if(o.name=='黄玉农场'){
	        			xzdata[5]=o;
	        		}
	        		$.each(data,function(index,item){
	        			if(item.name==o.name){
	            			data[index]=o;
	            		}
		        	});
        		});
	        	
	      	  option1.series[0].data=convertData(data);
	          option1.series[1].data=convertData1(xzdata);
	          
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