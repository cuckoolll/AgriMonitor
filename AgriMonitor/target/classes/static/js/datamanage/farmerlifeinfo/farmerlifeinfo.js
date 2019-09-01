layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;
	var timeControl1;	
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year'
		}); 
		
		timeControl1 = laydate.render({
			elem: '#date_year1',
			type: 'year'
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-110,
			 toolbar: '#barDemo',
			 url: '/farmerlifeinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [ 
		    	//表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left'},
				     {field: 'gid', title: 'ID',hide: true,align:'center'},
				     {field: 'date_year', title: '年份',sort: true, width:80},
				     {field: 'p1', title: '行政村总数', align:'center', },
			    	 {field: 'p2', title: '农村居民人均可支配收入', align:'center', },
			    	 {field: 'p3', title: '农村居民人均可支配收入年增长率', align:'center', },
			    	 {field: 'p4', title: '实施生活垃圾集中收集处理的行政村数', align:'center', },
			    	 {field: 'p5', title: '农村生活垃圾处理率', align:'center', },
			    	 {field: 'p6', title: '有生活污水处理设施的行政村数', align:'center', },
			    	 {field: 'p7', title: '农村生活污水处理率', align:'center', }
			     ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/farmerlifeinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
		  				  	page: {curr: 1}
		    			});
		    			layer.msg(res.msg);
				      }else{
				    	layer.msg(res.msg);
				      }
		    	}
		    }
		});
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增草地生态监测采样信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update'
					}, function(a){
						alert(a);
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		        	layer.msg('请选择一行');
		        } else if(data.length > 1){
		        	layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改草地生态监测采样信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update?gid='+checkStatus.data[0].gid
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
			        		url:"/farmerlifeinfo/delInfoByGid",
			        		contentType:"application/json",
			        		data: JSON.stringify(gids),
			        		dataType:"json",
			        		success:function(res){
			        			if(res && res.code==0){
			        				dataTable.reload({//表格数据重新加载
					    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
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
				  where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
				  page: {curr: 1}
			});
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
