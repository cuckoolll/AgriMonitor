layui.use(['form','layer','table', 'laydate', 'upload'], function(form,layer,table,laydate,upload) {
	function init(){
		var ue = UE.getEditor('container');
		
		var gid = getUrlParam("gid");
		if(gid){//如果有值，为更新操作
			//查询数据并赋值到表单中
			$.post("/agrinews/newsedit", {gid:gid},function(res){
		          if(res){
		        	  $.each(res,function(key,val){
	        			  $("[name='"+key+"']").val(val);
		        	  });
		          }else{
		        	  layer.msg('加载农业信息失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function bindEvent(){
		
	}
	
	init();
	bindEvent();
});