layui.use(['form','layer','table','upload'], function(form,layer,table,upload) {
	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/monitorManage/setlist', //数据接口，
		    height:winH-110,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'monitor_type', title: '监控指标',align:'center', templet: '#titleTpl'},
		      {field: 'condition_showname', title: '数据监控条件',align:'center'},
		      {field: 'stopflag', title: '是否停用',align:'center', templet: '#titleTpl1'}
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
              		    title: "新增监控信息",
						type: 2,
						area: ['700px', '450px'],
						scrollbar: true,
						content: '/monitorManage/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改监控信息",
						type: 2,
						area: ['700px', '450px'],
						scrollbar: true,
						content: '/monitorManage/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的监控信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/monitorManage/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除监控信息数据成功');
							        	  datatable.reload({//表格数据重新加载
											  where: {
												  monitor_type: $("#monitor_type").val()
											  },page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除监控信息数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除监控信息数据失败');
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
					  monitor_type: $("#monitor_type").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});