layui.config({
    base: '../../../temp/treetable-lay/'
}).extend({
    treetable: 'treetable'
}).use(['form','layer','table','treetable'], function (form,layer,table,treetable) {

	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=treetable.render({
			id:"datalist",
		    elem: '#datalist',
		    url: '/animalsBreed/animalsTarget/datalist', //数据接口，
		    height:winH-80,
		    treeColIndex: 1,
	        treeSpid: 0,
	        treeIdName: 'gid',
	        treePidName: 'parent_id',
	        treeDefaultClose: false,
	        treeLinkage: false,
		    cols: [[ //表头
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'target_name', title: '指标名称',align:'left'},
		      {field: 'user_showname', title: '创建人',align:'center'},
		      {field: 'create_time', title: '创建时间',align:'center'},
		      {field: 'stopflag', title: '是否停用',align:'center', templet: '#titleTpl'},
		      {templet: '#oper-col', title: '操作',align:'center'}
		    ]]
		});
	}
	
	function bindEvent(){
		//监听工具条
		table.on('tool(datalist)', function(obj){
			var data = obj.data;
		    switch(obj.event){
		      case 'edit':
		    	  layer.open({
            		    title: "修改指标",
						type: 2,
						area: ['400px', '500px'],
						scrollbar: true,
						content: '/animalsBreed/animalsTarget/update?gid='+data.gid
					});
		      break;
		      case 'del':
		    	  if(data.isleaf==0){
		    		  layer.msg('请先删除子节点数据');
		    	  }else{
		    		  layer.confirm('确定要删除该指标吗？', function(index){
		        	        layer.close(index);
				        	$.ajax({
				        		type:"post",
				        		url:"/animalsBreed/animalsTarget/doDel",
				        		data: {gid:data.gid,pid:data.parent_id},
				        		success:function(res){
				        			if(res){
							        	  if(res.code==0){
							        		  layer.msg('删除指标数据成功');
							        		  render();
							        	  }else{
							        		  layer.msg(res.msg);
							        	  }
							          }else{
							        	  layer.msg('删除指标数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除指标数据失败');
				        		}
				        	});
			    	  });
		    	  }
		      break;
		    };
		  });
		$("#addBtn").click(function(){
			layer.open({
      		    title: "新增畜牧业指标",
				type: 2,
				area: ['400px', '500px'],
				scrollbar: true,
				content: '/animalsBreed/animalsTarget/update'
			});
		});
	}
	
	function updateStatus(status){
		var checkStatus = table.checkStatus('datalist');
		debugger;
		var data = checkStatus.data;
		if(data.length === 0){
	          layer.msg('请选择一行');
	    }else{
    	    var gids=[];
        	$.each(data,function(index,item){
        		gids.push(item.gid);
        	});
        	$.ajax({
        		type:"post",
        		url:status==1?"/farminfo/animalstype/qy":"/farminfo/animalstype/ty",
        		contentType:"application/json",
        		data: JSON.stringify(gids),
        		dataType:"json",
        		success:function(res){
        			  if(res && res.code==0){
        				layer.msg((status==1?'启用':'停用')+'畜牧业指标成功');
        				datatable.reload({//表格数据重新加载
        					  where: {
        						  stopflag: $("#stopflag").val()
        					  },page: {curr: $(".layui-laypage-curr em:last").text()}
        				});
			          }else{
			        	  layer.msg((status==1?'启用':'停用')+'畜牧业指标失败');
			          }
        		},
        		error:function(){
        			layer.msg('删除畜牧业指标失败');
        		}
        	});
	    }
	}
	
	render();
	bindEvent();
});
