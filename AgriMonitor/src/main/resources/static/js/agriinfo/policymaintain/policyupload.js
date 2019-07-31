layui.use(['form','layer','table', 'laydate', 'upload'], function(form,layer,table,laydate,upload) {

	function init(){
		
	}
	
	function bindEvent(){
		//监听提交
//			upload.render({
//			    elem: '#chosefile',				
//			    url: '/policymaintain/fileUpload',
//			    data: {"file_name":function(){return $("#file_name").val()}},		
//			    auto: false,			
//		    	accept: 'file',
//		    	size: 10000, 						//10M
//			    bindAction: '#uploadBtn',		
//			    done: function(res, index, upload){
//			    	if (res && res.code==0) {
////			    		layer.msg("上传成功");
//			    	} else {
//			    		layer.msg("上传失败");
//			    	}
//			    }
//		  });
		
		 $("#chosefile").change(function(){
			 var files = $('#chosefile')[0].files[0];
			 var filename = files.name;
			 $("#file_name").val(filename);
		 });
		
		 $("#uploadBtn").click(function(){
		    var formData = new FormData();

		    formData.append("file",$('#chosefile')[0].files[0]);
		    formData.append("file_name", $("#file_name").val());

		    var size = $('#chosefile')[0].files[0].size;
            var filesize = (size / 1024 / 1024).toFixed(2);				//MB
		    
            if (filesize > 20) {
            	sessionStorage.setItem('code', "-1");
            	sessionStorage.setItem('msg', "上传文件大小不能大于20MB");
            	var index=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
	            parent.layer.close(index);		//关闭窗口
            	return;
            }
            
		    $.ajax({
		        url:'/policymaintain/fileUpload',
		        dataType:'json',
		        type:'POST',
		        async: false,
		        data: formData,
		        processData : false, // 使数据不做处理
		        contentType : false, // 不要设置Content-Type请求头
		        success: function(res){
		        	sessionStorage.setItem('code', res.code);
	        		sessionStorage.setItem('msg', res.msg);
		        	var index=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
		            parent.layer.close(index);		//关闭窗口
		        },
		        error:function(response){
		        	sessionStorage.setItem('msg', response);
		        	var index=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
		            parent.layer.close(index);		//关闭窗口
		        }
		    }); 
		 });
		 
	}
	
	init();
	bindEvent();
});