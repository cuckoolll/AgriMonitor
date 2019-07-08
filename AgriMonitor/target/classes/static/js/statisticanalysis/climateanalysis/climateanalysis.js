layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var timeControl;	  
	var winH=$(window).height();
	
	$("#charts").css("height",winH - 80);
	
    var charts = echarts.init(document.getElementById('charts'));
	
 // 指定图表的配置项和数据
    function setLineOption(map) {
    	var towns = param.towns;
    	var climateindex = param.climateindex;
    	var climateindexName = param.climateindexName;
    	
    	var param = {};
    	param.towns = towns;
    	param.climateindex = climateindex;
    	
    	title = towns + climateindexName + "分析图";
    	
    	$.post("/climateinfo/queryAnalysisData", param, function(data){
    		charts.setOption({
    			title: {
    	            text: title,
    	        },
    	        tooltip: {},
    	        legend: {
    	            data:[climateindexName]
    	        },
    	        xAxis: {
    	            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: '销量',
    	            type: 'bar',
    	            data: [5, 20, 36, 10, 10, 20]
    	        }]
        	});
    	});
    	
    };
    
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year'
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
