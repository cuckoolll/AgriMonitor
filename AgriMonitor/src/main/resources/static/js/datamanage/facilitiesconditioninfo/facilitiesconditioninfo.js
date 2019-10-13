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
			 url: '/facilitiesconditioninfo/queryInfo', //数据接口
			 method: 'post',
			 where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val(),"agri_address":$("#agri_address").val()},
		     page: true, //开启分页
		     limit:20,
			 limits:[20,40,60,100],
		     cols: [ 
		    	 //表头
		    	 [ 
		    		 {type: 'checkbox', fixed: 'left',rowspan:2},
				     {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
				     {field: 'date_year', title: '年份',sort: true,rowspan:2, align:'center',width:80},
				     {field: 'agri_address', title: '名称',sort: true,rowspan:2, align:'center',width:100},
				     {title: '主要农业机械和设备',align:'center',colspan:5, align:'center'},
				     {title: '主要电力设施及发电量',align:'center',colspan:4, align:'center'},
				     {title: '农田水利建设情况',align:'center',colspan:5, align:'center'},
				     {field: 'remark', title: '备注',rowspan:2, align:'center'},
			     ],
			     [
			    	 {field: 'large_medium_tractors', title: '大中型拖拉机（台）', align:'center',width:160},
			    	 {field: 'small_walking_tractors', title: '小型和手扶拖拉机（台）', align:'center',width:200},
			    	 {field: 'combine_harvester', title: '联合收割机（台）', align:'center',width:160},
			    	 {field: 'motor_thresher', title: '机动脱粒机（台）', align:'center',width:160},
			    	 {field: 'agricultural_carriage_car', title: '农用运输车（台）', align:'center',width:160},
			    	 
			    	 {field: 'rural_hydropower_stations', title: '乡村办水电站（个）', align:'center',width:160},
			    	 {field: 'generating_capacity', title: '发电能力（万千瓦）', align:'center',width:160},
			    	 {field: 'electric_energy', title: '发电量（万千瓦时）', align:'center',width:160},
			    	 {field: 'rural_power_consumption', title: '农村用电量（万千瓦时）', align:'center',width:160},
			    	 
			    	 {field: 'effective_irrigation_area', title: '有效灌溉面积（千公顷）', align:'center',width:160},
			    	 {field: 'waterlogging_drought_area', title: '旱涝保收面积（千公顷）', align:'center',width:160},
			    	 {field: 'mechanical_irrigation_area', title: '机电排灌面积（千公顷）', align:'center',width:160},
			    	 {field: 'reservoir', title: '水库（座）', align:'center',width:100},
			    	 {field: 'channel_area', title: '渠道面积', align:'center'},
			    ]]
		  });
		
		//文件上传
//		upload.render({
//		    elem: '#importBtn',
//		    url: '/environmentinfo/dataImport',
//		    accept: 'file',
//		    exts: 'xls|xlsx',
//		    done: function(res){
//		    	if(res){
//		    		if(res.code==0){
//		    			dataTable.reload({//表格数据重新加载
//		    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//		  				  	page: {curr: 1}
//		    			});
//		    			layer.msg(res.msg);
//				      }else{
//				    	layer.msg(res.msg);
//				      }
//		    	}
//		    }
//		});
	}
	
	function bindEvent() {
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
//		    	  layer.open({
//              		    title: "新增农业生态环境信息",
//						type: 2,
//						area: ['980px', '510px'],
//						scrollbar: true,
//						content: '/environmentinfo/update'
//					}, function(a){
//						alert(a);
//					});
		      break;
		      case 'update':
//		        if(data.length === 0){
//		        	layer.msg('请选择一行');
//		        } else if(data.length > 1){
//		        	layer.msg('只能同时编辑一个');
//		        } else {
//		        	layer.open({
//              		    title: "修改农业生态环境信息",
//						type: 2,
//						area: ['980px', '510px'],
//						scrollbar: true,
//						content: '/environmentinfo/update?gid='+checkStatus.data[0].gid
//					});
//		        }
		      break;
		      case 'delete':
//		        if(data.length === 0){
//		        	layer.msg('至少选择一行数据删除');
//		        } else {
//		        	layer.confirm('确定要删除选择的信息吗？', function(index){
//	        	        layer.close(index);
//	        	        var gids=[];
//			        	$.each(data,function(index,item){
//			        		gids.push(item.gid);
//			        	});
//			        	$.ajax({
//			        		type:"post",
//			        		url:"/environmentinfo/delInfoByGid",
//			        		contentType:"application/json",
//			        		data: JSON.stringify(gids),
//			        		dataType:"json",
//			        		success:function(res){
//			        			if(res && res.code==0){
//			        				dataTable.reload({//表格数据重新加载
//					    				where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//					  				  	page: {curr: 1}
//					    			});
//			        				layer.msg('删除成功');
//							        obj.config.index;
//			        			}else{
//			        				layer.msg('删除失败');
//			        			}
//			        		},
//			        		error:function(){
//			        			layer.msg('删除失败');
//			        		}
//			        	});
//		        	});
//		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
//			dataTable.reload({//表格数据重新加载
//				  where: {"date_year":$("#date_year").val(), "date_year1":$("#date_year1").val()},
//				  page: {curr: 1}
//			});
		});
	}
	
	function init() {
		render();
		bindEvent();
	}
	
	init();
});
