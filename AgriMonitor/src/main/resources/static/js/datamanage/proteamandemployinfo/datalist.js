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
		    url: '/proteamandemployinfo/datalist', //数据接口，
		    height:winH-105,
		    where: {date_year:curyear,date_year1:curyear},
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [ 
		    	 //表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left',rowspan:2},
				     {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
				     {field: 'county', title: '区（县、市）',width:120,rowspan:2, align:'center'},
				     {field: 'towns', title: '乡镇',rowspan:2, align:'center',width:130},
				     {field: 'village', title: '下辖地',rowspan:2, align:'center',width:150},
				     {field: 'date_year', title: '年份',rowspan:2, align:'center',width:100},
				     {field: 'hs', title: '户数（户）',rowspan:2, align:'center'},
				     {title: '人口数（人）',align:'center',colspan:2, align:'center'},
				     {title: '乡村从业人员',align:'center',colspan:4, align:'center'},
				     {field: 'bz', title: '备注',rowspan:2, align:'center'},
			     ],
			     [
			    	 {field: 'rks_n', title: '男', align:'center'},
			    	 {field: 'rks_v', title: '女', align:'center'},
			    	 {field: 'nyrs', title: '农业', align:'center'},
			    	 {field: 'cmyrs', title: '畜牧业', align:'center'},
			    	 {field: 'lyrs', title: '林业', align:'center'},
			    	 {field: 'fwyrs', title: '服务业', align:'center'}
			    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/proteamandemployinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			datatable.reload({//表格数据重新加载
		    				where: {date_year: $("#date_year").val(),date_year1: $("#date_year1").val(),towns: $("#towns").val()},
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
              		    title: "新增农业生产组织及从业人员信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/proteamandemployinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农业生产组织及从业人员信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/proteamandemployinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农业生产组织及从业人员信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/proteamandemployinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农业生产组织及从业人员数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {date_year: $("#date_year").val(),date_year1: $("#date_year1").val(),towns: $("#towns").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农业生产组织及从业人员数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农业生产组织及从业人员数据失败');
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