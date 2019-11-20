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
		    url: '/agriBaseinfo/datalist', //数据接口，
		    height:winH-105,
		    where: {date_year:curyear,date_year1:curyear},
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ //表头
		    	{type:'checkbox'},
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'county', title: '区（县、市）',align:'center',width:120},
		      {field: 'towns', title: '乡镇',align:'center',width:130},
		      {field: 'village', title: '下辖地',align:'center',width:150},
		      {field: 'date_year', title: '年份',align:'center',width:130},
		      {field: 'rksl', title: '人口数量（人）',align:'center',width:150},
		      {field: 'gtmj', title: '国土面积（平方千米）',align:'center',width:150},
		      {field: 'gdmj', title: '耕地面积（亩）',align:'center',width:150},
		      {field: 'gbzntmj', title: '高标准农田面积（亩）',align:'center',width:180},
		      {field: 'ccmj', title: '草场面积（亩）',align:'center',width:150},
		      {field: 'nzwzl', title: '农作物种类',align:'center',width:150},
		      {field: 'nzzmj', title: '年种植面积（亩）',align:'center',width:150},
		      {field: 'zzfs', title: '种植方式',align:'center',width:150}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/agriBaseinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			  layer.msg('导入数据成功');
				    	  datatable.reload({//表格数据重新加载
							  where: {date_year: $("#date_year").val(),date_year1: $("#date_year1").val(),towns: $("#towns").val()},page: {curr: 1}
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
              		    title: "新增农业基本信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/agriBaseinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农业基本信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/agriBaseinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农业基本信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/agriBaseinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农业基本数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {date_year: $("#date_year").val(),date_year1: $("#date_year1").val(),towns: $("#towns").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农业基本数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农业基本数据失败');
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
				where: {date_year: $("#date_year").val(),date_year1: $("#date_year1").val(),towns: $("#towns").val()},page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});