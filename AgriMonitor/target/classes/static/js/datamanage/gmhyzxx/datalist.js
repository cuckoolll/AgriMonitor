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
		laydate.render({
		    elem: '#date_year1',
		    type: 'year',
		    value:curyear
		 });
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/gmhyzxx/datalist', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[
		    	{type:'checkbox'},
			      {field: 'gid', title: 'ID',hide: true,align:'center'},
			      {field: 'year', title: '年份',align:'center',width:90},
		      {field: 'szcls', title: '出栏的生猪数量',align:'center'},
		      {field: 'rncls', title: '出栏肉牛数量',align:'center'},
		      {field: 'nncls', title: '存栏的奶牛数量',align:'center'},
		      {field: 'rycls', title: '出栏的肉羊数量',align:'center'},
		      {field: 'djcls', title: '存栏的蛋鸡数量',align:'center'},
		      {field: 'rjcls', title: '出栏的肉鸡数量',align:'center'},
		      {field: 'qt', title: '其他畜禽数量',align:'center'}]]
		});
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/gmhyzxx/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},
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
	
	function bindEvent(){
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增规模化养殖信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/gmhyzxx/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改规模化养殖信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/gmhyzxx/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的规模化养殖信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/gmhyzxx/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除规模化养殖数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除规模化养殖数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除规模化养殖数据失败');
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
				where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});