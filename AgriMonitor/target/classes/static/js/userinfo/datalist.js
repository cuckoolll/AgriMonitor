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
		    url: '/userinfo/datalist', //数据接口，
		    height:winH-110,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'user_id', title: '登录名称',align:'center'},
		      {field: 'user_showname', title: '显示名',align:'center'},
		      {field: 'role_showname', title: '角色',align:'center'}
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
		    	  layer.open({
              		    title: "新增用户",
						type: 2,
						area: ['550px', '350px'],
						scrollbar: true,
						content: '/userinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改用户",
						type: 2,
						area: ['550px', '350px'],
						scrollbar: true,
						content: '/userinfo/update?gid='+checkStatus.data[0].user_id
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的用户吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.user_id);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/userinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除用户数据成功');
							        	  datatable.reload({//表格数据重新加载
											  where: {
												  user_role: $("#user_role").val()
											  },page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除用户数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除用户数据失败');
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
					  user_role: $("#user_role").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});