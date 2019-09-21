layui.use(['form','layer','table'], function(form,layer,table) {
	function init(){}
	
	function bindEvent(){
		$("#exportData").click(function(){
			window.open("/animalsBreed/animalsBreedAnalysis/exportKHZB");
		});
	}
	init();
	bindEvent();
});