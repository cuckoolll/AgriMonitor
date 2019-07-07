layui.use(['form','layer','table'], function(form,layer,table) {
	function init(){
		var gid = getUrlParam("gid");
		if(gid){//如果有值，为更新操作
			$("[name=parent_id]").attr("disabled","disabled");
			//查询数据并赋值到表单中
			$.post("/animalsBreed/animalsTarget/findById", {gid:gid},function(res){
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
		        	  layer.msg('加载畜牧业指标失败');
		        	  $("#saveBtn").attr('disabled',true);
		          }
	        });
		}
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			$.post("/animalsBreed/animalsTarget/doSave", data.field,function(res){
		          if(res && res.code==0){
		        	  parent.layer.msg('保存畜牧业指标成功');
		        	  parent.layui.treetable.render({
		      			id:"datalist",
		    		    elem: '#datalist',
		    		    url: '/animalsBreed/animalsTarget/datalist', //数据接口，
		    		    height:$(parent.window).height()-80,
		    		    treeColIndex: 1,
		    	        treeSpid: 0,
		    	        treeIdName: 'gid',
		    	        treePidName: 'parent_id',
		    	        treeDefaultClose: false,
		    	        treeLinkage: false,
		    		    cols: [[ //表头
		    		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		    		      {field: 'target_name', title: '指标名称',align:'left'},
		    		      {field: 'user_showname', title: '创建人',align:'center'},
		    		      {field: 'create_time', title: '创建时间',align:'center'},
		    		      {field: 'stopflag', title: '是否停用',align:'center', templet: '#titleTpl'},
		    		      {templet: '#oper-col', title: '操作',align:'center'}
		    		    ]]
		    		  });
		        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					  parent.layer.close(index); //再执行关闭
		          }else{
		        	  layer.msg('保存畜牧业指标失败');
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