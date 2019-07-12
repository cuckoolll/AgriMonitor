layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var timeControl;	  
	var winH=$(window).height();
	
	$("#charts").css("height",winH - 80);
	
    var charts = echarts.init(document.getElementById('charts'));
	
    /**
     * 绘制折线图 .
     */
    function setLineOption(map) {
    	var towns = map.towns;
    	var climateindex = map.climateindex;
    	var climateindexName = map.climateindexName;
    	
    	var param = {};
    	param.towns = towns;
    	param.climateindex = climateindex;
    	
    	title = towns + climateindexName + "分析图";
    	
    	$.post("/climateinfo/queryAnalysisData", param, function(res){
    		charts.setOption({
    			title: {
    	            text: title,
    	        },
    	        tooltip: {},
    	        legend: {
    	            data:[climateindexName]
    	        },
    	        xAxis: {
    	            data: res.date_year
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: climateindexName,
    	            type: 'line',
    	            data: res.data
    	        }]
        	});
    	});
    };
    
    /**
     * 绘制柱状图 .
     */
    function setBarOption(map) {
    	var date_year = map.date_year;
    	var climateindex = map.climateindex;
    	var climateindexName = map.climateindexName;
    	
    	var param = {};
    	param.date_year = date_year;
    	param.climateindex = climateindex;
    	
    	title = date_year + "年" + climateindexName + "分析图";
    	
    	$.post("/climateinfo/queryAnalysisData", param, function(res){
    		charts.setOption({
    			title: {
    	            text: title,
    	        },
    	        tooltip: {},
    	        legend: {
    	            data:[climateindexName]
    	        },
    	        xAxis: {
    	            data: res.towns
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: climateindexName,
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
		map.date_year = $("#date_year").val();
		map.towns = $("#towns").val();
		map.climateindex = $("#climateindex").val();
		map.climateindexName = $("#climateindex option:selected").text();
		if (   (map.date_year == null || map.date_year == '' || map.date_year == 'undefined')
			&& (map.towns == null || map.towns == '' || map.towns == 'undefined')) {
			layer.msg("请至少选择一个查询条件（年份、乡镇）。");
			return;
		}
		
		if (map.date_year == null || map.date_year == '' || map.date_year == 'undefined') {
			setLineOption(map);
		} else {
			setBarOption(map);
		}
		
	}
	
	function bindEvent() {
		//查询数据
		$("#queryBtn").click(function(){
			doQuery();
		});
		
		$(window).resize(function(){
			$("#charts").css("height",winH - 80);
			charts.resize();
		});
		
		form.on('select(towns)', function(data) { 
			var date_year = $("#date_year").val();
			if (date_year != null && date_year != '' && date_year != 'undefined') {
				$("#date_year").val('');
			}
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
