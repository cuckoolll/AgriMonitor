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
			 height:winH-110,
			 toolbar: '#barDemo',
			 url: '/fishinfo/queryInfo', //数据接口
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
				     {field: 'date_year', title: '年份',sort: true,rowspan:2, width:80},
//				     {title: '青海湖',align:'center',colspan:4},
				     {title: '裸鲤资源蕴藏总量',align:'center',colspan:2},
				     {title: '浮游植物的定性和定量',align:'center',colspan:4},
				     {title: '浮游动物的定性和定量',align:'center',colspan:4},
				     {title: '底栖动物',align:'center',colspan:3},
				     {title: '青海湖裸鲤卵苗分布密度数据',align:'center',colspan:5},
				     {field: 'scyzmj', title: '水产养殖总面积',sort: true,rowspan:2, width:150},
				     {field: 'scbzhyzmj', title: '水产标准化健康养殖示范场（区）养殖面积',sort: true,rowspan:2, width:200}
			     ],
			     [
//			    	 {field: 'water_level', title: '水位', align:'center', },
//			    	 {field: 'water_area', title: '面积', align:'center', },
//			    	 {field: 'water_salinity', title: '盐度', align:'center', },
//			    	 {field: 'water_ph', title: 'PH', align:'center', },
			    	 
			    	 {field: 'naked_carp_count', title: '总尾数', align:'center', width:100},
			    	 {field: 'naked_carp_quality', title: '总资源量', align:'center', width:100},
			    	 
			    	 {field: 'phytoplankton_density', title: '浮游植物密度', align:'center', width:120},
			    	 {field: 'phytoplankton_avg_density', title: '平均密度', align:'center', width:100},
			    	 {field: 'phytoplankton_count', title: '浮游植物生物量', align:'center', width:140},
			    	 {field: 'phytoplankton_avg_count', title: '平均生物量', align:'center', width:100},
			    	 
			    	 {field: 'zooplankter_density', title: '浮游动物密度', align:'center', width:120},
			    	 {field: 'zooplankter_avg_density', title: '平均密度', align:'center', width:100},
			    	 {field: 'zooplankter_count', title: '浮游动物生物量', align:'center', width:140},
			    	 {field: 'zooplankter_avg_count', title: '平均生物量', align:'center', width:100},
			    	 
			    	 {field: 'zoobenthos_density', title: '密度', align:'center',},
			    	 {field: 'zoobenthos_avg_count', title: '单位面积生物量', align:'center', width:140},
			    	 {field: 'zoobenthos_avg_density', title: '平均密度', align:'center', width:100},
			    	 
			    	 {field: 'naked_carp_seed_in_bhh', title: '布哈河', align:'center', width:100},
			    	 {field: 'naked_carp_seed_in_slh', title: '沙柳河', align:'center', width:100},
			    	 {field: 'naked_carp_seed_in_qjh', title: '泉吉河', align:'center', width:100},
			    	 {field: 'naked_carp_seed_in_hmh', title: '黑马河', align:'center', width:100},
			    	 {field: 'naked_carp_seed_in_hegh', title: '哈尔盖河', align:'center', width:100}
			    ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/fishinfo/dataImport',
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
              		    title: "新增渔业生产信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/fishinfo/update'
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
              		    title: "修改渔业生产信息",
						type: 2,
						area: ['980px', '510px'],
						scrollbar: true,
						content: '/fishinfo/update?gid='+checkStatus.data[0].gid
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
			        		url:"/fishinfo/delInfoByGid",
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
