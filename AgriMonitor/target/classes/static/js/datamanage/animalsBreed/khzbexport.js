layui.use(['form','layer','table'], function(form,layer,table) {
	var winH=$(window).height();
	$('#datatable').height(winH-100);
	
	function init(){
		$("#preloaderDiv").show();
		$.post("/animalsBreed/animalsBreedAnalysis/getKHZBData", {},function(res){
			$("#preloaderDiv").hide();
	        if(res){
	        	$('th[name=year_1]').html(res.year_1);
	        	$('th[name=year_2]').html(res.year_2);
	        	$('th[name=year_3]').html(res.year_3);
	        	$.each(res,function(p,v){
	        		if(p=='bzmj_1'||p=='bzmj_2'||p=='bzmj_3'){
	        			$('td[name='+p+']').html(v?v.toFixed(3):'');
	        		}else{
	        			$('td[name='+p+']').html(v);
	        		}
	        	});
	        }
	    });
	}
	
	function bindEvent(){
		$("#exportData").click(function(){
			//window.open("/animalsBreed/animalsBreedAnalysis/exportKHZB");
			var form = document.getElementById('addForm');
			form.submit();
		});
	}
	init();
	bindEvent();
});