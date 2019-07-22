layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year'
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-80,
			 toolbar: '#barDemo',
			 url: '/grassinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"date_year":$("#date_year").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [ 
		    	 //表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left',rowspan:2},
				     {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
				     {field: 'date_year', title: '年份',sort: true,rowspan:2,width:100},
				     {title: '草场面积（万亩）',align:'center',colspan:2},
				     {field: 'grass_retain_area', title: '多年生人工种草保留面积（万亩）',rowspan:2, width:240},
				     {title: '牧草产量（kg/hm2）',align:'center',colspan:2},
				     {title: '鼠虫害危害面积（万亩）',align:'center',colspan:4}
			     ],
			     [
			    	 {field: 'grass_area', title: '总面积（万亩）', width:140},
			    	 {field: 'grass_usable_area', title: '可利用面积（万亩）', width:160},
			    	 {field: 'grass_unforage', title: '禁牧（kg/hm2）', width:140},
			    	 {field: 'grass_animal_balance', title: '草畜平衡（kg/hm2）', width:160},
			    	 {field: 'plateau_pika_area', title: '高原鼠兔危害面积（万亩）', width:200},
			    	 {field: 'plateau_zokor_area', title: '高原鼢鼠危害面积（万亩）', width:200},
			    	 {field: 'grass_worm_area', title: '草原毛虫危害面积（万亩）', width:200},
			    	 {field: 'grasshopper_area', title: '蝗虫危害面积（万亩）', width:200}
			    ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/grassinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {"date_year":$("#date_year").val()},
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
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增草原生态监测采样信息",
						type: 2,
						area: ['900px', '520px'],
						scrollbar: true,
						content: '/grassinfo/update'
					}, function(a){
						alert(a);
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		        	layer.msg('请选择一行');
		        } else if(data.length > 1){
		        	layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改草原生态监测采样信息",
						type: 2,
						area: ['900px', '520px'],
						scrollbar: true,
						content: '/grassinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		        	layer.msg('至少选择一行数据删除');
		        } else {
		        	layer.confirm('确定要删除选择的信息吗？', function(index){
	        	        layer.close(index);
	        	        var gids=[];
			        	$.each(data,function(index,item){
			        		gids.push(item.gid);
			        	});
			        	$.ajax({
			        		type:"post",
			        		url:"/grassinfo/delInfoByGid",
			        		contentType:"application/json",
			        		data: JSON.stringify(gids),
			        		dataType:"json",
			        		success:function(res){
			        			if(res && res.code==0){
			        				dataTable.reload({//表格数据重新加载
					    				where: {"date_year":$("#date_year").val()},
					  				  	page: {curr: 1}
					    			});
			        				layer.msg('删除成功');
							        obj.config.index;
			        			}else{
			        				layer.msg('删除失败');
			        			}
			        		},
			        		error:function(){
			        			layer.msg('删除失败');
			        		}
			        	});
		        	});
		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
			dataTable.reload({//表格数据重新加载
				  where: {"date_year":$("#date_year").val()},
				  page: {curr: 1}
			});
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
