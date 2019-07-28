layui.use(['form','layer','table'], function(form,layer,table) {
	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/syslog/datalist', //数据接口，
		    height:winH-80,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'operation_type', title: '操作类型',align:'center', templet: '#titleTpl',width:120},
		      {field: 'operation_status', title: '操作状态',align:'center', templet: '#titleTpl1',width:120},
		      {field: 'user_showname', title: '操作用户',align:'center',width:220},
		      {field: 'create_time', title: '操作时间',align:'center',width:200},
		      {field: 'operation_log', title: '操作日志',align:'center'}
		    ]]
		});
	}
	
	function bindEvent(){
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  
		      break;
		      case 'update':
		        
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的操作日志吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/syslog/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res){
							        	  if(res.code==0){
							        		  layer.msg('删除数据成功');
							        		  datatable.reload({//表格数据重新加载
							    				  where: {
							    					  operation_type: $("#type").val(),operation_status: $("#status").val()
							    				  },page: {curr: 1}
							        		  });
							        	  }else{
							        		  layer.msg(res.msg);
							        	  }
							          }else{
							        	  layer.msg('删除数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除失败');
				        		}
				        	});
		        	});
		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
			datatable.reload({//表格数据重新加载
				  where: {
					  operation_type: $("#type").val(),operation_status: $("#status").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});