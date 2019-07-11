layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var timeControl;	  
	var winH=$(window).height();
	
	$("#charts").css("height",winH - 80);
	
    var charts = echarts.init(document.getElementById('charts'));
	
    /**
     * 绘制柱状图 .
     */
    function setBarOption(map) {
    	var date_year = map.date_year;
    	var soilindex = map.soilindex;
    	var soilindexName = map.soilindexName;
    	
    	var param = {};
    	param.date_year = date_year;
    	param.soilindex = soilindex;
    	
    	title = date_year + "年" + soilindexName + "分析图";
    	
    	$.post("/soilinfo/queryAnalysisData", param, function(res){
    		charts.setOption({
    			title: {
    	            text: title,
    	        },
    	        tooltip: {},
    	        legend: {
    	            data:[soilindexName]
    	        },
    	        xAxis: {
    	            data: res.code_number
    	        },
    	        yAxis: {},
    	        series: [{
    	            name: soilindexName,
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
			value: new Date()
		}); 
		
		doQuery();
	}
	
	function doQuery() {
		var map = {};
		map.date_year = $("#date_year").val();
		map.soilindex = $("#soilindex").val();
		map.soilindexName = $("#soilindex option:selected").text();

		setBarOption(map);
		
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
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
