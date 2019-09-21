layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	var info_type;
	
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
			 height:winH-110,
			 toolbar: '#barDemo',
			 url: '/policymaintain/queryInfo', //数据接口
			 method: 'post',
			 where: {"create_time":$("#create_time").val(),"file_name":$("#file_name").val(),"info_type":info_type},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'file_name', title: '文件标题'},
		    	 {field: 'info_type', title: 'info_type', hide: true},
		    	 {field: 'info_type_name', title: '文件类别'},
		    	 {field: 'company', title:'单位'},
		    	 {field: 'create_time', title: '创建时间', sort: true},
		    	 {field: 'creator', title: '创建人'},
		    	 {templet: '#oper-col', title: '操作',align:'center'}
			 ]]
		  });
	}
	
	function doQuery() {
		dataTable.reload({//表格数据重新加载
			where: {"create_time":$("#create_time").val(),"file_name":$("#file_name").val(),"info_type":info_type},
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
	
	//获取url参数
	function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
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
		
		table.on('tool(datalist)', function(obj){
		    var data = obj.data; //获取选中的数据
		    switch(obj.event){
		      case 'download':
		    	  layer.msg('开始下载');
		    	  download(data.file_address);
		      break;
		    };
		  });
		
		//查询数据
		$("#queryBtn").click(function(){
			doQuery();
		});
		
		$("#uploadBtn").click(function(){
			layer.open({
      		    title: "上传农业政策文件",
				type: 2,
				area: ['700px', '360px'],
				scrollbar: true,
				content: '/policymaintain/upload?info_type=' + info_type,
				end: function(index, layero){
					var code = sessionStorage.getItem('code');
					if (code && code == 0) {
						doQuery();
						layer.msg(sessionStorage.getItem('msg'));
					} else if (code && code == -1) {
						layer.msg(sessionStorage.getItem('msg'));
					}
					sessionStorage.removeItem("code");
				  	return false; 
				}  
			});
		});
	}
	
	function init() {
		info_type = getUrlParam("info_type");
		render();
		bindEvent();
	}
	
	init();
});
