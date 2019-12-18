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
			elem: '#quality_time'
		}); 
		
		timeControl1 = laydate.render({
			elem: '#quality_time1'
		}); 
		
		//表格渲染
		renderTable();
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/waterinfo/dataImportRowFixed',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val(),"quality_time1":$("#quality_time1").val()},
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
	
	function renderTable() {
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 height:winH-110,
			 toolbar: '#barDemo',
			 url: '/waterinfo/queryWaterInfoRowFixed', //数据接口
			 method: 'post',
			 where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val(),"quality_time1":$("#quality_time1").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [[ //表头
		       	 {type: 'checkbox', fixed: 'left'},
		       	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		       	 {field: 'quality_address', title: '采样地点', width:200},
		       	 {field: 'quality_time', title: '采样时间', sort: true, width:110},
		       	 {field: 'water_temperature', title: '水温(℃)', width:100}, 
		       	 {field: 'ph', title: 'ph值', width:80},
		       	 {field: 'dissolved_oxygen', title: '溶解氧',width:100},
		       	 {field: 'permanganate_index', title: '高锰酸盐指数', width:130},
		       	 {field: 'chemical_oxygen_demand', title: '化学需氧量',width:120},
		       	 {field: 'five_day_bod', title: '五日生化需氧量', width:130},
		       	 {field: 'ammonia_nitrogen', title: '氨氮',width:80},
		       	 {field: 'total_phosphorus', title: '总磷',width:80},
		       	 {field: 'total_nitrogen', title: '总氮',width:80},
		       	 {field: 'fluoride', title: '氟化物',width:100},
		       	 {field: 'copper', title: '铜',width:100},
		       	 {field: 'plumbum', title: '铅',width:100},
		       	 {field: 'cadmium', title: '镉',width:100},
		       	 {field: 'zinc', title: '锌',width:100},
		       	 {field: 'mercury', title: '汞',width:100},
		       	 {field: 'arsenic', title: '砷',width:100},
		       	 {field: 'selenium', title: '硒',width:100},
		       	 {field: 'hexavalent_chromium', title: '六价铬',width:100},
		       	 {field: 'volatile_penol', title: '挥发酚',width:100},
		       	 {field: 'cyanide', title: '氰化物',width:100},
		       	 {field: 'petroleum', title: '石油类',width:100},
		       	 {field: 'anionic_surfactant', title: '阴离子表面活性剂',width:180},
		       	 {field: 'sulfide', title: '硫化物',width:100},
		       	 {field: 'conductivity', title: '电导率',width:100},
		       	 {field: 'fecal_coliform', title: '粪大肠菌群',width:120}
		    	 ]]
		  });
	}
	

	
	function bindEvent() {
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增水质监测采样信息",
						type: 2,
						area: ['850px', '600px'],
						scrollbar: true,
						content: '/waterinfo/updateRowFixed'
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
              		    title: "修改水质监测采样信息",
						type: 2,
						area: ['850px', '600px'],
						scrollbar: true,
						content: '/waterinfo/updateRowFixed?gid='+checkStatus.data[0].gid
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
			        		url:"/waterinfo/delInfoByGidRowFixed",
			        		contentType:"application/json",
			        		data: JSON.stringify(gids),
			        		dataType:"json",
			        		success:function(res){
			        			if(res && res.code==0){
//			        				dataTable.reload({//表格数据重新加载
//					    				where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val()},
//					  				  	page: {curr: 1}
//					    			});
			        				renderTable();
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
//			dataTable.reload({//表格数据重新加载
//				  where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val()},
//				  page: {curr: 1}
//			});
			renderTable();
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
