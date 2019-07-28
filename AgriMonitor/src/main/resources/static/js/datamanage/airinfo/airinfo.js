layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#quality_time',
			type: 'month',
			value: new Date()
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-80,
			 toolbar: '#barDemo',
			 url: '/airinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"city":$("#city").val(),"quality_time":$("#quality_time").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'city', title: '城市'},
		    	 {field: 'station_name', title: '站点名称'},
		    	 {field: 'quality_time', title: '时间', sort: true},
		    	 {field: 'so2', title: 'SO2'}, 
		    	 {field: 'no2', title: 'NO2'},
		    	 {field: 'co', title: 'CO(mg/m3)'},
		    	 {field: 'o3_8h', title: 'O3-8h'},
		    	 {field: 'pm10', title: 'PM10'},
		    	 {field: 'pm2_5', title: 'PM2.5'}
			 ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/airinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {"city":$("#city").val(),"quality_time":$("#quality_time").val()},
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
              		    title: "新增空气站监测信息",
						type: 2,
						area: ['800px', '560px'],
						scrollbar: true,
						content: '/airinfo/update'
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
              		    title: "修改空气站监测信息",
						type: 2,
						area: ['800px', '560px'],
						scrollbar: true,
						content: '/airinfo/update?gid='+checkStatus.data[0].gid
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
			        		url:"/airinfo/delInfoByGid",
			        		contentType:"application/json",
			        		data: JSON.stringify(gids),
			        		dataType:"json",
			        		success:function(res){
			        			if(res && res.code==0){
			        				dataTable.reload({//表格数据重新加载
					    				where: {"city":$("#city").val(),"quality_time":$("#quality_time").val()},
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
				  where: {"city":$("#city").val(),"quality_time":$("#quality_time").val()},
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
