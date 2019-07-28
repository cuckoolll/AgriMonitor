layui.use(['form','layer','table', 'laydate', 'upload'], function(form,layer,table,laydate,upload) {
	var winW = $(window).width();
	var winH = $(window).height();
	var ue;
	function init(){
		var gid = getUrlParam("gid");
		var isShow = getUrlParam("show") == '1' ? true : false;
		
		ue = UE.getEditor('container');
		if (!isShow) {
			$("#title").removeAttr("readonly");
			$("#container").removeAttr("readonly");
			$("#saveBtn").css("display", "block");
		}
		
		
		ue.ready(function() {
			ue.setHeight(winH - 125);
		}); 
		
		if(gid){//如果有值，为更新操作
			//查询数据并赋值到表单中
			$.post("/agrinews/findById", {gid:gid},function(res){
		          if(res){
		        	  $.each(res,function(key,val){
	        			  $("[name='"+key+"']").val(val);
	        			  if ("content" == key) {
        					  ue.ready(function() {
	        					  //设置编辑器的内容
	        					  ue.setContent(val);
	        					  if (isShow) {
	        						  ue.setDisabled();
	        					  }
	        				  }); 
	        			  }
		        	  });
		          }else{
		        	  layer.msg('加载农业信息失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function windowAlign() {
		$(window).resize(function(){
		    ue.ready(function() {
				ue.setHeight(winH - 125);
			}); 
		});
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			$.post("/agrinews/save", data.field,function(res){
		          if(res && res.code==0){
		        	  parent.layer.msg('农业信息保存成功');
		        	  parent.layui.table.reload('datalist');
		        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					  parent.layer.close(index); //再执行关闭
		          }else{
		        	  layer.msg('农业信息保存失败');
		          }
	        });
			return false;
		});
		
		windowAlign();
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