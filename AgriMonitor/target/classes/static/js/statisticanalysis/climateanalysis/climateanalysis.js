layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var timeControl;	  
	var winH=$(window).height();
	
	$("#charts").css("height",winH - 80);
	
    var charts = echarts.init(document.getElementById('charts'));
	
 // 指定图表的配置项和数据
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
    
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year',
			value: new Date()
		}); 
		
		var map = {};
		map.towns = $("#towns").val();
		map.climateindex = $("#climateindex").val();
		map.climateindexName = $("#climateindex option:selected").text();
		setLineOption(map);
	}
	
	function bindEvent() {
		//查询数据
		$("#queryBtn").click(function(){
			render();
		});
		
		$(window).resize(function(){
			$("#charts").css("height",winH - 80);
			charts.resize();
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
