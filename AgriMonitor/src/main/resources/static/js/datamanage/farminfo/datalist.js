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
		    url: '/farminfo/datalist', //数据接口，
		    height:winH-150,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'county', title: '区（县、市）',align:'center',width:120},
		      {field: 'towns', title: '乡镇',align:'center',width:130},
		      {field: 'farm_name', title: '养殖场名称',align:'center',width:200},
		      {field: 'farm_address', title: '地址',align:'center',width:260},
		      {field: 'legal_person', title: '法人',align:'center',width:150},
		      {field: 'phone_num', title: '联系电话',align:'center',width:150},
		      {field: 'animals_name', title: '认定畜种',align:'center',width:150},
		      {field: 'animals_size', title: '牲畜存栏（头、只）',align:'center',width:200},
		      {field: 'remarks', title: '备注',align:'center',width:200}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/farminfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			  layer.msg('导入数据成功');
				    	  datatable.reload({//表格数据重新加载
							  where: {
								  farmname: $("#farmname").val(),type: $("#type").val()
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
              		    title: "新增养殖场信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/farminfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改养殖场信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/farminfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的养殖场信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/farminfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除养殖场数据成功');
							        	  datatable.reload({//表格数据重新加载
											  where: {
												  farm_name: $("#farmname").val(),animals_type: $("#type").val()
											  },page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除养殖场数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除养殖场数据失败');
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
					  farm_name: $("#farmname").val(),animals_type: $("#type").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});