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
		    url: '/farmerlifeinfo/datalist', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
		    	{type:'checkbox'},
			      {field: 'year',title: '年份',align:'center'},
			      {field: 'xzcs',title: '行政村总数',align:'center',width:120},
			      {field: 'rjsr', title: '农村居民人均可支配收入',align:'center',width:200},
			      {field: 'srzzl', title: '农村居民人均可支配收入年增长率',align:'center',width:250},
			      {field: 'ljsjxzcs', title: '实施生活垃圾集中收集处理的行政村数',align:'center',width:280},
			      {field: 'ljcll', title: '农村生活垃圾处理率',align:'center',width:200},
			      {field: 'wsclxzcs', title: '有生活污水处理设施的行政村数',align:'center',width:240},
			      {field: 'wscll', title: '农村生活污水处理率',align:'center',width:200}
			    ]]
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
              		    title: "新增农民生活信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农民生活信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农民生活信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/farmerlifeinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农民生活数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农民生活数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农民生活数据失败');
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