layui.use(['form','layer','table', 'laydate', 'upload'], function(form,layer,table,laydate,upload) {
	function init(){
		
	}
	
	function bindEvent(){
		//监听提交
		 upload.render({
			    elem: '#chosefile',				
			    url: '/policymaintain/fileUpload',
			    data: {file_name:$("#file_name").val()},		
			    auto: false,			
		    	accept: 'file',
		    	size: 10000, 				//10M
			    bindAction: '#uploadBtn',			//“上传”按钮的ID
			    done: function(res){
			    	if (res && res.code==0) {
			    		layer.msg("上传成功");
			    	} else {
			    		layer.msg("上传失败");
			    	}
			    }
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