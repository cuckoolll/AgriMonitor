layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year'
		}); 
		
	}
	
	function bindEvent() {
		//查询数据
		$("#queryBtn").click(function(){
			
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
