layui.use(['form','layer','table','laydate','util'], function(form,layer,table,laydate,util) {
	var curyear=util.toDateString(new Date(), 'yyyy');
	laydate.render({
	    elem: '#year',
	    type: 'year',
	    value:curyear
	 });
	function init(){
		var gid = getUrlParam("gid");
		if(gid){//如果有值，为更新操作
			//查询数据并赋值到表单中
			$("[name=date_year]").attr("readonly","readonly");
			$.post("/farmerlifeinfo/findById", {gid:gid},function(res){
		          if(res){
		        	  $.each(res,function(key,val){
		        		  if(key=='towns'){
		        			  $("[name='"+key+"'] option[value='"+val+"']").attr("selected","true");
		        			  form.render('select');
		        		  }else{
		        			  $("[name='"+key+"']").val(val);
		        		  }
		        	  });
		          }else{
		        	  layer.msg('加载农民生活信息失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			$.post("/farmerlifeinfo/doSave", data.field,function(res){
		          if(res && res.code==0){
		        	  parent.layer.msg('保存农民生活数据成功');
		        	  parent.layui.table.reload('datalist');
		        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					  parent.layer.close(index); //再执行关闭
		          }else{
		        	  layer.msg('保存农民生活数据失败');
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