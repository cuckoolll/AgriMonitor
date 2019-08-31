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
		    url: '/monitorManage/loglist', //数据接口，
		    height:winH-110,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'monitor_type', title: '监控指标',align:'center', templet: '#titleTpl'},
		      /*{field: 'condition_showname', title: '数据监控条件',align:'center'}*/
		      {field: 'log', title: '预警信息',align:'center'},
		      {field: 'ratio', title: '预警级别',align:'center', templet: '#titleTpl1'},
		      {field: 'create_time', title: '预警时间',align:'center',width:200},
		      {toolbar: '#barDemo1', title: '操作',align:'center',width:100}
		    ]]
		});
	}
	
	function bindEvent(){
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的预警信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/monitorManage/delLog",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除预警信息数据成功');
							        	  datatable.reload({//表格数据重新加载
											  where: {
												  monitor_type: $("#monitor_type").val()
											  },page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除预警信息数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除预警信息数据失败');
				        		}
				        	});
		        	});
		        }
		      break;
		    };
		  });
		  //监听行工具事件
		  table.on('tool(datalist)', function(obj){
		    var data = obj.data
		    ,layEvent = obj.event;
		    if(layEvent === 'ty'){
		    	$.post("/monitorManage/updatestatus", {stopflag:0,gid:data.gid},function(res){
	  		          if(res && res.code==0){
	  		        	  layer.msg('停用预警信息成功');
			        	  datatable.reload({//表格数据重新加载
							  where: {
								  monitor_type: $("#monitor_type").val()
							  },page: {curr: 1}
			        	  });
	  		          }else{
	  		        	  layer.msg('停用预警信息失败');
	  		          }
	  	        });
		    }
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
	
	function initDatamonitor(){
		$.ajax({
    		type:"get",
    		url:"/monitorManage/datamonitor",
    		contentType:"application/json",
    		dataType:"json",
    		success:function(res){
    			render();
    			$("#preloaderDiv").hide();
				bindEvent();
    		},
    		error:function(){
    			render();
    			$("#preloaderDiv").hide();
				bindEvent();
    		}
    	});
		
	}
	initDatamonitor();
});