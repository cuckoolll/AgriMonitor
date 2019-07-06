layui.use(['form','layer','table'], function(form,layer,table) {
	function init(){
		var gid = getUrlParam("gid");
		if(gid){//如果有值，为更新操作
			$("name='parent_id'").attr("disabled","disabled");
			//查询数据并赋值到表单中
			$.post("/farminfo/animalstype/findById", {gid:gid},function(res){
		          if(res){
		        	  $.each(res,function(key,val){
		        		  if(key=='stopflag' || key=='parent_id'){
		        			  $("[name='"+key+"'] option[value='"+val+"']").attr("selected","true");
		        			  form.render('select');
		        		  }else{
		        			  $("[name='"+key+"']").val(val);
		        		  }
		        	  });
		          }else{
		        	  layer.msg('加载认定畜种信息失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			$.post("/farminfo/animalstype/doSave", data.field,function(res){
		          if(res && res.code==0){
		        	  parent.layer.msg('保存认定畜种数据成功');
		        	  parent.layui.table.reload('datalist');
		        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					  parent.layer.close(index); //再执行关闭
		          }else{
		        	  layer.msg('保存认定畜种数据失败');
		          }
	        });
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