layui.use(['form','layer'], function(form,layer,table) {
	//监听提交
	form.on('submit(submitBut)', function(data) {
		if($("#user_id").val()){
			var pw=$("#pw").val();
			var old_password=$("#old_password").val();
			var new_password1=$("#new_password1").val();
			var new_password2=$("#new_password2").val();
			if(pw!=old_password){
				layer.msg('旧密码输入错误');
				return false;
			}
			if(new_password1!=new_password2){
				layer.msg('新密码两次输入不一致，请重新输入');
				return false;
			}
			$.post("/userinfo/savepw", {user_id:$("#user_id").val(),user_password:new_password1},function(res){
		          if(res){
		        	  window.location.href="/login.html"; 
		          }else{
		        	  layer.msg('修改密码失败');
		          }
	        });
		}else{
			layer.msg('请重新登录系统');
		}
		return false;
	});
});