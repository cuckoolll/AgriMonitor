layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;
	var timeControl1;	
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#date_year',
			type: 'year'
		}); 
		
		timeControl1 = laydate.render({
			elem: '#date_year1',
			type: 'year'
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-150,
			 toolbar: '#barDemo',
			 url: '/environmentinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [ 
		    	 //表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left',rowspan:2},
				     {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
				     {field: 'date_year', title: '年份',sort: true,rowspan:2,width:80},
				     {field: 'grass_vegetation_cover', title: '草原综合植被盖度', align:'center', rowspan:2,width:150},
				     {title: '天然草原草畜平衡率',align:'center',colspan:2},
				     {title: '农田林网控制率',align:'center',colspan:2},
				     {title: '森林覆盖率',align:'center',colspan:2},
				     {field: 'wetland_area', title: '湿地面积', align:'center',rowspan:2, width:120},
				     {title: '青海湖',align:'center',colspan:4}
			     ],
			     [
			    	 {field: 'grass_grazing_capacity', title: '天然草原合理载畜量', align:'center',width:150},
			    	 {field: 'grass_grazing_capacity_reality', title: '天然草原全年实际载畜量', align:'center',width:150},
			    	 {field: 'agroforestry_farm_area_built', title: '已建成农田林网的农田面积', align:'center',width:150},
			    	 {field: 'agroforestry_farm_area_predict', title: '应建农田林网的农田面积', align:'center',width:150},
			    	 {field: 'forestry_area', title: '森林面积', align:'center', width:120},
			    	 {field: 'land_area', title: '国土面积', align:'center', width:120},
			    	 {field: 'water_level', title: '水位', align:'center', },
			    	 {field: 'water_area', title: '面积', align:'center', },
			    	 {field: 'water_salinity', title: '盐度', align:'center', },
			    	 {field: 'water_ph', title: 'PH', align:'center', }
			    ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/environmentinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
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
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增农业生态环境信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/environmentinfo/update'
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
              		    title: "修改农业生态环境信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/environmentinfo/update?gid='+checkStatus.data[0].gid
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
			        		url:"/environmentinfo/delInfoByGid",
			        		contentType:"application/json",
			        		data: JSON.stringify(gids),
			        		dataType:"json",
			        		success:function(res){
			        			if(res && res.code==0){
			        				dataTable.reload({//表格数据重新加载
					    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
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
				  where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
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
