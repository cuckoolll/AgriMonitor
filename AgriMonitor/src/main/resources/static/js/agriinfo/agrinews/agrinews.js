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
			 url: '/policymaintain/queryInfo', //数据接口
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
		    	 {field: 'creator', title: '创建人'}
			 ]]
		  });
	}
	
	function doQuery() {
		dataTable.reload({//表格数据重新加载
			where: {"title":$("#title").val()},
			  	page: {curr: 1}
		});
	}
	
	function download(file_address){
	    //动态创建表单加到fbody中，最后删除表单
	    var queryForm = $("#queryForm");
	    var downloadForm = $("<form action='/policymaintain/download' method='post'></form>");
	    downloadForm.append("<input type='hidden' name='file_address' value='"+file_address+"'/>");
	    $(document.body).append(downloadForm);
	    downloadForm.submit();
	    downloadForm.remove(); 
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    var data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
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
