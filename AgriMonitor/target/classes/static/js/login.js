layui.use(['form', 'layer'], function(form, layer){
	
	//自定义验证规则
//	form.verify({
//		vercodes: function(value){
//			//获取验证码
//			var zylVerCode = $(".zylVerCode").html();
//			if (value == zylVerCode) {
//				verCodeTrue = true; 
//			} else {
//				layer.msg("验证码错误（区分大小写）");
//			}
//		}
//	});
	
//	//监听提交
//	form.on('submit(login)', function(data){
//		login()
//		return false;
//	});
	
	$("#loginBtn").click(function(){
		login();
	})
	
	if (window != top){
		top.location.href = location.href;
	}
	function login() {
		var user_id=$("#user_id").val();
		var user_password=$("#user_password").val();
		if (user_id && user_password) {
			var zylVerCode = $(".zylVerCode").html();
			var vercode = $("#vercode").val();
//			if (!vercode) {
//				layer.msg("请输入验证码");
//				return;
//			}
//			if (vercode != zylVerCode) {
//				layer.msg("验证码错误（区分大小写）");
//				return;
//			}
			$.post("/doLogin", {userid: user_id, pw: user_password},function(data){
		          if(data && data.user_id){
		        	  window.location.href="/workbench"; 
		          }else{
		        	  layer.msg("用户名或密码错误");
		          }
	        });
		}else{
			layer.msg("请输入用户名和密码");
		}
	}
	
	$("body").bind("keydown", function(event) {
		if (event.keyCode == "13") {
　　　　		login();
	　　 }
	});
});