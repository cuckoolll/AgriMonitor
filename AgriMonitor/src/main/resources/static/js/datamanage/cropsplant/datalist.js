layui.use(['form','layer','table','upload','laydate','util'], function(form,layer,table,upload,laydate,util) {
	var winH=$(window).height();
	var datatable;
	
	var curyear=util.toDateString(new Date(), 'yyyy');
	
	function render(){
		laydate.render({
		    elem: '#date_year',
		    type: 'year',
		    value:curyear
		 });
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/cropsplant/datalist', //数据接口，
		    height:winH-80,
		    where: {date_year:$("#date_year").val()},
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'county', title: '区（县、市）',align:'center',width:120},
		      {field: 'towns', title: '乡镇',align:'center',width:120},
		      {field: 'date_year', title: '年份',align:'center'},
		      {field: 'crops_name', title: '农作物类型',align:'center'},
		      {field: 'planted_area', title: '农作物播种面积（万亩）',align:'center'},
		      {field: 'planted_output', title: '农作物单产（公斤）',align:'center'}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/cropsplant/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			  layer.msg('导入数据成功');
				    	  datatable.reload({//表格数据重新加载
							  where: {
								  date_year: $("#date_year").val(),crops_type: $("#crops_type").val(),towns: $("#towns").val()
							  },page: {curr: 1}
				    	  });
				      }else{
				    	  layer.msg(res.msg);
				      }
		    	}
		    }
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
              		    title: "新增农作物产量信息",
						type: 2,
						area: ['700px', '450px'],
						scrollbar: true,
						content: '/cropsplant/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农作物产量信息",
						type: 2,
						area: ['700px', '450px'],
						scrollbar: true,
						content: '/cropsplant/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农作物产量信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/cropsplant/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农作物产量数据成功');
							        	  datatable.reload({//表格数据重新加载
											  where: {
												  date_year: $("#date_year").val(),crops_type: $("#crops_type").val(),towns: $("#towns").val()
											  },page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农作物产量数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农作物产量数据失败');
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
					  date_year: $("#date_year").val(),crops_type: $("#crops_type").val(),towns: $("#towns").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});