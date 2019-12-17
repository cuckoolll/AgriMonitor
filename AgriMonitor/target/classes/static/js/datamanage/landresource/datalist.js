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
		datatable = table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/landresource/datalist', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
		    	{type:'checkbox',rowspan:2},
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
			      {field: 'year', title: '年份',align:'center',rowspan:2,width:90},
			      {title: '土地资源',align:'center',colspan:4},
			      {title: '农作物种类及播种面积（hm2）',align:'center',colspan:3},
			      {title: '产量',align:'center',colspan:2},
//			      {title: '土壤理化性质',align:'center',colspan:9}
			    ],[
		      {field: 'gdmj', title: '耕地面积（hm2）',align:'center',width:150},
		      {field: 'gbzltmj', title: '高标准农田面积（hm2）',align:'center',width:150},
		      {field: 'gbzltbz', title: '高标准农田比重（%）',align:'center',width:150},
		      {field: 'ggslyxs', title: '农田灌溉水有效利用系数',align:'center',width:150},
		      {field: 'qkmj', title: '青稞',align:'center',width:100},
		      {field: 'ycmj', title: '油菜',align:'center',width:100},
		      {field: 'ymmj', title: '燕麦',align:'center',width:100},
		      {field: 'dc', title: '单产（kg）',align:'center',width:130},
		      {field: 'zc', title: '总产（吨）',align:'center',width:130},
//		      {field: 'yjz', title: '有机质（g/kg）',align:'center',width:130},
//		      {field: 'qd', title: '全氮（g/kg）',align:'center',width:130},
//		      {field: 'qlhl', title: '全磷含量(g/kg)',align:'center',width:130},
//		      {field: 'yxlhl', title: '有效磷含量(mg/kg)',align:'center',width:130},
//		      {field: 'qjhl', title: '全钾含量(g/kg)',align:'center',width:130},
//		      {field: 'ph', title: 'pH',align:'center',width:130},
//		      {field: 'yd', title: '盐度（%）',align:'center',width:130},
//		      {field: 'sxjhl', title: '速效钾含量(mg/kg)',align:'center',width:130},
//		      {field: 'hxjhl', title: '缓效钾含量(mg/kg)',align:'center',width:130}
		    ]]
		});
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/landresource/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			datatable.reload({//表格数据重新加载
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
	
	function bindEvent(){
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增土地资源信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/landresource/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改土地资源信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/landresource/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的土地资源信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/landresource/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除土地资源数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除土地资源数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除土地资源数据失败');
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