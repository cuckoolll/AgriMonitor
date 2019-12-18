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
		    url: '/agriinputinfo/datalist', //数据接口，
		    height:winH-150,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
		    	{type:'checkbox',rowspan:3},
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:3},
			      {field: 'year', title: '年份',align:'center',rowspan:3,width:90},
			      {title: '农业投入品',align:'center',colspan:11},
			      {title: '农业废弃物',align:'center',colspan:7},
			      {field: 'bz',title: '备注',align:'center',rowspan:3}
			    ],[ 
			      {title: '农用化肥施用量（折纯）',align:'center',colspan:6},
			      {title: '地膜使用量（公斤 ）及覆盖面积（公顷）',align:'center',colspan:2},
			      {field: 'lyqd',title: '农药施用强度（折百量）',align:'center',rowspan:2,width:190},
			      {field: 'lysyl',title: '农药使用量（公斤）',align:'center',rowspan:2,width:180},
			      {field: 'lycyl',title: '农用柴油使用量（吨）',align:'center',rowspan:2,width:190},
			      {field: 'jgzl', title: '秸秆总量',align:'center',rowspan:2,width:140},
			      {field: 'zhlyjg', title: '综合利用的秸秆量',align:'center',rowspan:2,width:180},
			      {field: 'jgksj', title: '秸秆可收集资源量',align:'center',rowspan:2,width:190},
			      {field: 'lmzl', title: '回收利用的农膜总量',align:'center',rowspan:2,width:190},
			      {field: 'lmsyl', title: '农膜使用总量',align:'center',rowspan:2,width:140},
			      {field: 'qcfl', title: '综合利用的畜禽粪污量',align:'center',rowspan:2,width:140},
			      {field: 'qcfzl', title: '畜禽粪污总量',align:'center',rowspan:2,width:140}
			    ],[
		      {field: 'n', title: '氮肥',align:'center',width:100},
		      {field: 'p', title: '磷肥',align:'center',width:100},
		      {field: 'k', title: '钾肥',align:'center',width:100},
		      {field: 'fhf', title: '复合肥',align:'center',width:100},
		      {field: 'yjf', title: '商品有机肥',align:'center',width:100},
		      {field: 'ljf', title: '农家肥',align:'center',width:100},
		      {field: 'dm_syl', title: '使用量',align:'center',width:140},
		      {field: 'dm_fgmj', title: '覆盖面积',align:'center',width:140}
		    ]]
		});
		
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/agriinputinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			datatable.reload({//表格数据重新加载
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
              		    title: "新增农业投入信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/agriinputinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农业投入信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/agriinputinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农业投入信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/agriinputinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农业投入数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农业投入数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农业投入数据失败');
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