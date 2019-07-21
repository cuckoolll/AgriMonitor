layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-80,
			 toolbar: '#barDemo',
			 url: '/agrinews/queryInfo', //数据接口
			 method: 'post',
			 where: {"title":$("#title").val()},
			 skin : 'line',
			 page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'title', title: '标题'},
		    	 {field: 'author', title: '作者'},
		    	 {field: 'create_time', title: '创建时间', sort: true},
		    	 {field: 'creator', title: '创建人'},
		    	 {templet: '#oper-col', title: '操作',align:'center'}
			 ]]
		  });
	}
	
	function doQuery() {
		dataTable.reload({//表格数据重新加载
			where: {"title":$("#title").val()},
			  	page: {curr: 1}
		});
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    var data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		    	case 'add':
		    		layer.open({
		      		    title: "新建农业信息",
						type: 2,
						area: ['800px', '600px'],
						scrollbar: true,
						content: '/agrinews/newsedit',
						end: function(index, layero){ 
							doQuery();
							layer.msg(sessionStorage.getItem('msg'));
						  	return false; 
						}  
					});
	    		break;
		    	case 'update':
		    		if(data.length === 0){
			        	layer.msg('请选择一行');
			        } else if(data.length > 1){
			        	layer.msg('只能同时编辑一个');
			        } else {
			        	layer.open({
			      		    title: "修改农业信息",
							type: 2,
							area: ['800px', '600px'],
							scrollbar: true,
							content: '/agrinews/newsedit?gid='+checkStatus.data[0].gid,
							end: function(index, layero){ 
								doQuery();
								layer.msg(sessionStorage.getItem('msg'));
							  	return false; 
							}  
						});
			        }
	    		break;
		      	case 'delete':
			        if(data.length === 0){
			        	layer.msg('至少选择一行数据删除');
			        } else {
			        	layer.confirm('确定要删除选择的信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/agrinews/delInfoByGid",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
				        				doQuery();
				        				layer.msg('删除成功');
								        obj.config.index;
				        			}else{
				        				layer.msg('删除失败');
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
		
		table.on('tool(datalist)', function(obj){
		    var data = obj.data; //获取选中的数据
		    switch(obj.event){
		      case 'show':
		    	  layer.open({
		      		    title: "查看农业信息",
						type: 2,
						area: ['800px', '600px'],
						scrollbar: true,
						content: '/agrinews/newsedit?show=1&gid='+data.gid
					});
		      break;
		    };
		  });
		
		//查询数据
		$("#queryBtn").click(function(){
			doQuery();
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
