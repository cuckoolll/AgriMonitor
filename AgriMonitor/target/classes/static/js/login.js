layui.use(['form', 'layer'], function(form){
	//监听提交
	form.on('submit(login)', function(data){
		login()
		return false;
	});
	
	if (window != top){
		top.location.href = location.href;
	}
	function login() {
		var user_id=$("#user_id").val();
		var user_password=$("#user_password").val();
		if(user_id && user_password){
			$.post("/doLogin", {userid: user_id, pw: user_password},function(data){
		          if(data && data.user_id){
		        	  window.location.href="/workbench"; 
		          }else{
		        	  layer.msg("用户名或密码错误！");
		          }
	        });
		}else{
			layer.msg("请输入用户名和密码！");
		}
	}
	
	$("body").bind("keydown", function(event) {
		if (event.keyCode == "13") {
　　　　		login();
	　　 }
	});
});