layui.use(['table', 'form'], function(table, form) {
		  var table;
		  
	function render(obj) {
		 table.render({
			 elem: '#grid',
			 url: '/queryWaterInfo', //数据接口
			 method: 'post',
			 where: obj,
		     page: true, //开启分页
		     cols: [[ //表头
		    	 {field: 'quality_address', title: '采样地点', width:'20%'},
		    	 {field: 'quality_time', title: '采样时间', width:'20%', sort: true},
		    	 {field: 'quality_type', title: '分析项目', width:'20%'}, 
		    	 {field: 'quality_result', title: '分析结果（mg/L）', width:'20%'},
		    	 {field: 'remarks', title: '备注', width:'20%'}
			 ]]
		  });
	}
	
	function bindEvent() {
		$("#query").click(function(){
			gridRender({"county":$("#county").val(),"time":"2019-06-29"});
		});
	}
	
	function init() {
		$("#county").val("刚察县");
		render({"county":$("#county").val(),"time":"2019-06-29"});
		bindEvent();
	}
	
	init();
});
