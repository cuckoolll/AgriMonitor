layui.use(['form','layer','table'], function(form,layer,table,upload) {
	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/farminfo/animalstype/datalist', //数据接口，
		    height:winH-80,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'type_name', title: '认定畜种名称',align:'center'},
		      {field: 'user_showname', title: '创建人',align:'center'},
		      {field: 'create_time', title: '创建时间',align:'center'},
		      {field: 'stopflag', title: '是否停用',align:'center', templet: '#titleTpl'}
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
              		    title: "新增认定畜种",
						type: 2,
						area: ['400px', '280px'],
						scrollbar: true,
						content: '/farminfo/animalstype/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改认定畜种",
						type: 2,
						area: ['400px', '250px'],
						scrollbar: true,
						content: '/farminfo/animalstype/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的认定畜种信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/farminfo/animalstype/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res){
							        	  if(res.code==0){
							        		  layer.msg('删除认定畜种数据成功');
							        		  datatable.reload({//表格数据重新加载
							    				  where: {
							    					  stopflag: $("#stopflag").val()
							    				  },page: {curr: 1}
							    			  });
							        	  }else{
							        		  layer.msg(res.msg);
							        	  }
							          }else{
							        	  layer.msg('删除认定畜种数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除认定畜种失败');
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
					  stopflag: $("#stopflag").val()
				  },page: {curr: 1}
			});
		});
		$("#qyBtn").click(function(){
			updateStatus(1);
		});
		$("#tyBtn").click(function(){
			updateStatus(0);
		});
	}
	
	function updateStatus(status){
		var checkStatus = table.checkStatus('datalist');
		debugger;
		var data = checkStatus.data;
		if(data.length === 0){
	          layer.msg('请选择一行');
	    }else{
    	    var gids=[];
        	$.each(data,function(index,item){
        		gids.push(item.gid);
        	});
        	$.ajax({
        		type:"post",
        		url:status==1?"/farminfo/animalstype/qy":"/farminfo/animalstype/ty",
        		contentType:"application/json",
        		data: JSON.stringify(gids),
        		dataType:"json",
        		success:function(res){
        			  if(res && res.code==0){
        				layer.msg((status==1?'启用':'停用')+'认定畜种成功');
        				datatable.reload({//表格数据重新加载
        					  where: {
        						  stopflag: $("#stopflag").val()
        					  },page: {curr: $(".layui-laypage-curr em:last").text()}
        				});
			          }else{
			        	  layer.msg((status==1?'启用':'停用')+'认定畜种失败');
			          }
        		},
        		error:function(){
        			layer.msg('删除认定畜种数据失败');
        		}
        	});
	    }
	}
	
	render();
	bindEvent();
});