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
			 url: '/climateinfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'date_year', title: '年份', sort: true, width:100},
//		    	 {field: 'county', title: '区（县、市）', width:120},
//		    	 {field: 'towns', title: '乡镇', width:100},
		    	 {field: 'year_avg_temperature', title: '年平均气温（摄氏度）', width:180},
		    	 {field: 'high_temperature', title: '极端最高气温（摄氏度）', width:200}, 
		    	 {field: 'low_temperature', title: '极端最低气温（摄氏度）', width:200},
		    	 {field: 'year_precipitation', title: '年降水量（毫米）', width:180},
		    	 {field: 'mouth_high_precipitation', title: '月最大降水量（毫米）', width:180},
		    	 {field: 'day_high_precipitation', title: '日最大降水量（毫米）', width:180},
		    	 {field: 'year_avg_winds', title: '年平均最多风向（米每秒）', width:200},
		    	 {field: 'high_winds', title: '极大风速（米每秒）', width:180},
		    	 {field: 'year_high_winds_days', title: '年大风日数（天）', width:180},
		    	 {field: 'year_avg_pressure', title: '年平均气压（百帕）', width:150},
		    	 {field: 'year_thunderstorm_days', title: '年雷暴日数（次）', width:150},
		    	 {field: 'year_sandstorm_days', title: '年尘暴日数（次）', width:150}
			 ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/climateinfo/dataImport',
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
              		    title: "新增气候监测采样信息",
						type: 2,
						area: ['900px', '550px'],
						scrollbar: true,
						content: '/climateinfo/update'
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
              		    title: "修改气候监测采样信息",
						type: 2,
						area: ['900px', '550px'],
						scrollbar: true,
						content: '/climateinfo/update?gid='+checkStatus.data[0].gid
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
			        		url:"/climateinfo/delInfoByGid",
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
