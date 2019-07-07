layui.config({
    base: '../../temp/treetable-lay/'
}).extend({
    treetable: 'treetable'
}).use(['form','layer','treetable','upload','laydate','util'], function(form,layer,treetable,upload,laydate,util) {
	var winH=$(window).height();
	var datatable;
	
	var curdate=util.toDateString(new Date(), 'yyyy-MM');
	laydate.render({
	    elem: '#date_month',
	    type: 'month',
	    value:curdate
	 });
	
	function render(date){
		//表格渲染
		datatable=treetable.render({
			id:"datalist",
		    elem: '#datalist',
		    url: '/animalsBreed/datalist', //数据接口，
		    height:winH-80,
		    where: {date_month: date,towns:$("#towns").val()},
		    toolbar: true,
		    treeColIndex: 1,
	        treeSpid: 0,
	        treeIdName: 'fgid',
	        treePidName: 'parent_id',
	        treeDefaultClose: false,
	        treeLinkage: false,
		    cols: [[ 
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
			      {field: 'target_name', title: '指标名称',align:'left',rowspan:2},
			      {field: 'county', title: '区（县、市）',align:'center',width:120,rowspan:2},
			      {field: 'towns', title: '乡镇',align:'center',width:120,rowspan:2},
			      {field: 'surplus_size', title: '月末存栏数',align:'center',rowspan:2},
			      {field: 'female_size', title: '能繁殖母畜',align:'center',rowspan:2},
			      {title: '增加',align:'center',colspan:2},
			      {title: '减少',align:'center',colspan:3},
			      {title: '产量',align:'center',colspan:4},
			      {templet: '#oper-col', title: '操作',align:'center',rowspan:2}
			    ],[
		      {field: 'child_size', title: '产仔数',align:'center'},
		      {field: 'survival_size', title: '成活数',align:'center'},
		      {field: 'death_size', title: '损亡数',align:'center'},
		      {field: 'maturity_size', title: '出栏数',align:'center'},
		      {field: 'sell_size', title: '出售数',align:'center'},
		      {field: 'meat_output', title: '肉产量',align:'center'},
		      {field: 'milk_output', title: '奶产量',align:'center'},
		      {field: 'egg_output', title: '蛋产量',align:'center'},
		      {field: 'hair_output', title: '毛产量',align:'center'}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/animalsBreed/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			layer.msg('导入数据成功');
		    			render($("#date_month").val().replace('-',''));
				      }else{
				    	  layer.msg(res.msg);
				      }
		    	}
		    }
		});
	}
	
	function bindEvent(){
		//查询数据
		$("#queryBtn").click(function(){
			render($("#date_month").val().replace('-',''));
		});
		//监听工具条
		table.on('tool(datalist)', function(obj){
			var data = obj.data;
		    switch(obj.event){
		      case 'edit':
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
		      case 'del':
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
	}
	render(curdate.replace('-',''));
	bindEvent();
});