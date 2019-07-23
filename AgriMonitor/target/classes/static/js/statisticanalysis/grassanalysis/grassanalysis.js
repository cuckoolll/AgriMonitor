layui.use(['table', 'form', 'laydate', 'layer', 'upload', 'util'], function(table, form, laydate, layer, upload, util) {
	var timeControl;	  
	var winH=$(window).height();
	
	$("#charts").css("height",winH - 80);
	
	var curyear=util.toDateString(new Date(), 'yyyy');
	var years=[curyear-9,curyear-8,curyear-7,curyear-6,curyear-5,curyear-4,curyear-3,curyear-2,curyear-1,curyear];
    var charts = echarts.init(document.getElementById('charts'));
	
    /**
     * 绘制折线图 .
     */
    function setLineOption(map) {
    	var towns = map.towns;
    	var grassindex = map.grassindex;
    	var grassindexName = map.grassindexName;
    	
    	var param = {};
    	param.towns = towns;
    	param.grassindex = grassindex;
    	
    	title = grassindexName + "近十年分析图";
    	
    	$.post("/grassinfo/queryAnalysisData", param, function(res){
    		charts.setOption({
    			title: {
    	            text: title,
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
    	        legend: {
    	            data:[grassindexName]
    	        },
    	        xAxis: {
    	            data: years
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: grassindexName,
    	            type: 'line',
    	            data: res.data
    	        }
    	        ]
        	});
    	});
    };
    
    /**
     * 绘制柱状图 .
     */
    function setBarOption(map) {
    	var date_year = map.date_year;
    	var grassindex = map.grassindex;
    	var grassindexName = map.grassindexName;
    	
    	var param = {};
    	param.date_year = date_year;
    	param.grassindex = grassindex;
    	
    	title = date_year + "年" + grassindexName + "分析图";
    	
    	$.post("/grassinfo/queryAnalysisData", param, function(res){
    		charts.setOption({
    			title: {
    	            text: title,
    	        },
    	        tooltip: {},
    	        legend: {
    	            data:[grassName]
    	        },
    	        xAxis: {
    	            data: res.towns
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: grassName,
    	            type: 'bar',
    	            data: res.data
    	        }]
        	});
    	});
    };
    
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year',
			value: new Date(),
			done: function(value, date, endDate) {
				var towns = $("#towns").val();
				if (towns != null && towns != '' && towns != 'undefined') {
					$("#towns").val('');
					form.render('select');
				}
			}
		}); 
		
		doQuery();
	}
	
	function doQuery() {
		var map = {};
//		map.date_year = $("#date_year").val();
//		map.towns = $("#towns").val();
		map.grassindex = $("#grassindex").val();
		map.grassindexName = $("#grassindex option:selected").text();
		
		setLineOption(map);
//		if (   (map.date_year == null || map.date_year == '' || map.date_year == 'undefined')
//			&& (map.towns == null || map.towns == '' || map.towns == 'undefined')) {
//			layer.msg("请至少选择一个查询条件（年份、乡镇）。");
//			return;
//		}
		
//		if (map.date_year == null || map.date_year == '' || map.date_year == 'undefined') {
//			setLineOption(map);
//		} else {
//			setBarOption(map);
//		}
		
	}
	
	function bindEvent() {
		//查询数据
//		$("#queryBtn").click(function(){
//			doQuery();
//		});
		
		$(window).resize(function(){
			$("#charts").css("height",winH - 80);
			charts.resize();
		});
		
		form.on('select(grassindex)', function(data) {
			doQuery();
		});
		
//		form.on('select(towns)', function(data) { 
//			var date_year = $("#date_year").val();
//			if (date_year != null && date_year != '' && date_year != 'undefined') {
//				$("#date_year").val('');
//			}
//		});
		
		
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
