layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#create_time',
			type: 'month',
			value: new Date()
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-80,
			 toolbar: '#barDemo',
			 url: '/policymaintain/queryInfo', //数据接口
			 method: 'post',
			 where: {"create_time":$("#create_time").val(),"file_name":$("#file_name").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'file_name', title: '文件标题'},
		    	 {field: 'create_time', title: '创建时间', sort: true},
		    	 {field: 'creator', title: '创建人'},
		    	 {templet: '#oper-col', title: '操作',align:'center'}
			 ]]
		  });
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'download':
		    	  
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
				        		url:"/policymaintain/delInfoByGid",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
				        				dataTable.reload({//表格数据重新加载
						    				where: {"create_time":$("#create_time").val(),"file_name":$("#file_name").val()},
						  				  	page: {curr: 1}
						    			});
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
		//查询数据
		$("#queryBtn").click(function(){
			dataTable.reload({//表格数据重新加载
				  where: {"create_time":$("#create_time").val(),"file_name":$("#file_name").val()},
				  page: {curr: 1}
			});
		});
		
		$("#uploadBtn").click(function(){
			layer.open({
      		    title: "上传农业政策文件",
				type: 2,
				area: ['700px', '230px'],
				scrollbar: true,
				content: '/policymaintain/upload'
			}, function(a){
				alert(a);
			});
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
