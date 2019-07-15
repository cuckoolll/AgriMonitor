layui.use(['form','layer','table'], function(form,layer,table) {
	var gid = getUrlParam("gid");
	function init(){
		if(gid){//如果有值，为更新操作
			$("#user_id").attr("readonly","readonly");
			//查询数据并赋值到表单中
			$.post("/userinfo/findById", {gid:gid},function(res){
		          if(res){
		        	  $.each(res,function(key,val){
		        		  if(key=='user_role'){
		        			  $("[name='"+key+"'] option[value='"+val+"']").attr("selected","true");
		        			  form.render('select');
		        		  }else{
		        			  $("[name='"+key+"']").val(val);
		        		  }
		        	  });
		          }else{
		        	  layer.msg('加载用户失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			if(gid){
				$.post("/userinfo/doUpdate", data.field,function(res){
			          if(res){
			        	  if(res.code==0){
			        		  parent.layer.msg('保存用户数据成功');
				        	  parent.layui.table.reload('datalist');
				        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							  parent.layer.close(index); //再执行关闭
			        	  }else{
			        		  layer.msg(res.msg);
			        	  }
			          }else{
			        	  layer.msg('保存用户数据失败');
			          }
		        });
			}else{
				$.post("/userinfo/doAdd", data.field,function(res){
			          if(res){
			        	  if(res.code==0){
			        		  parent.layer.msg('保存用户数据成功');
				        	  parent.layui.table.reload('datalist');
				        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							  parent.layer.close(index); //再执行关闭
			        	  }else{
			        		  layer.msg(res.msg);
			        	  }
			          }else{
			        	  layer.msg('保存用户数据失败');
			          }
		        });
			}
			return false;
		});
	}
	//获取url参数
	function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
	
	init();
	bindEvent();
});