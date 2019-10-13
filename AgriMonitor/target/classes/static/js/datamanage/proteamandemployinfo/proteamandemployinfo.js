layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-110,
			 toolbar: '#barDemo',
			 url: '/proteamandemployinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"village":$("#village").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [ 
		    	 //表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left',rowspan:2},
				     {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
				     {field: 'county', title: '名称',sort: true,rowspan:2, align:'center'},
				     {field: 'village', title: '行政村',rowspan:2, align:'center'},
				     {field: 'households', title: '户数（户）',rowspan:2, align:'center'},
				     {title: '人口数（人）',align:'center',colspan:2, align:'center'},
				     {title: '乡村从业人员',align:'center',colspan:4, align:'center'},
				     {field: 'remark', title: '备注',rowspan:2, align:'center'},
			     ],
			     [
			    	 {field: 'male', title: '男', align:'center'},
			    	 {field: 'female', title: '女', align:'center'},
			    	 {field: 'agriculture_employ', title: '农业', align:'center'},
			    	 {field: 'animal_employ', title: '畜牧业', align:'center'},
			    	 {field: 'forestry_employ', title: '林业', align:'center'},
			    	 {field: 'service_employ', title: '服务业', align:'center'}
			    ]]
		  });
		
		//文件上传
//		upload.render({
//		    elem: '#importBtn',
//		    url: '/environmentinfo/dataImport',
//		    accept: 'file',
//		    exts: 'xls|xlsx',
//		    done: function(res){
//		    	if(res){
//		    		if(res.code==0){
//		    			dataTable.reload({//表格数据重新加载
//		    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//		  				  	page: {curr: 1}
//		    			});
//		    			layer.msg(res.msg);
//				      }else{
//				    	layer.msg(res.msg);
//				      }
//		    	}
//		    }
//		});
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
//		    	  layer.open({
//              		    title: "新增农业生态环境信息",
//						type: 2,
//						area: ['980px', '510px'],
//						scrollbar: true,
//						content: '/environmentinfo/update'
//					}, function(a){
//						alert(a);
//					});
		      break;
		      case 'update':
//		        if(data.length === 0){
//		        	layer.msg('请选择一行');
//		        } else if(data.length > 1){
//		        	layer.msg('只能同时编辑一个');
//		        } else {
//		        	layer.open({
//              		    title: "修改农业生态环境信息",
//						type: 2,
//						area: ['980px', '510px'],
//						scrollbar: true,
//						content: '/environmentinfo/update?gid='+checkStatus.data[0].gid
//					});
//		        }
		      break;
		      case 'delete':
//		        if(data.length === 0){
//		        	layer.msg('至少选择一行数据删除');
//		        } else {
//		        	layer.confirm('确定要删除选择的信息吗？', function(index){
//	        	        layer.close(index);
//	        	        var gids=[];
//			        	$.each(data,function(index,item){
//			        		gids.push(item.gid);
//			        	});
//			        	$.ajax({
//			        		type:"post",
//			        		url:"/environmentinfo/delInfoByGid",
//			        		contentType:"application/json",
//			        		data: JSON.stringify(gids),
//			        		dataType:"json",
//			        		success:function(res){
//			        			if(res && res.code==0){
//			        				dataTable.reload({//表格数据重新加载
//					    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//					  				  	page: {curr: 1}
//					    			});
//			        				layer.msg('删除成功');
//							        obj.config.index;
//			        			}else{
//			        				layer.msg('删除失败');
//			        			}
//			        		},
//			        		error:function(){
//			        			layer.msg('删除失败');
//			        		}
//			        	});
//		        	});
//		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
//			dataTable.reload({//表格数据重新加载
//				  where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//				  page: {curr: 1}
//			});
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
