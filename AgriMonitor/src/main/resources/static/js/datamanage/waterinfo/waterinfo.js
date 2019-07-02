layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#time',
			value: new Date()
		}); 
		
		dataTable = table.render({
			 id:'datalist',
			 elem: '#datalist',
			 toolbar: '#barDemo',
			 url: '/waterinfo/queryWaterInfo', //数据接口
			 method: 'post',
			 where: {"county":$("#county").val(),"time":$("#time").val()},
		     page: true, //开启分页
		     cols: [[ //表头
		    	 {type: 'checkbox', fixed: 'left'},
		    	 {field: 'gid', title: 'gid',hide: true,align:'center'},
		    	 {field: 'quality_address', title: '采样地点'},
		    	 {field: 'quality_time', title: '采样时间', sort: true},
		    	 {field: 'quality_type', title: '分析项目'}, 
		    	 {field: 'quality_result', title: '分析结果（mg/L）'},
		    	 {field: 'remarks', title: '备注'}
			 ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/waterinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		    				where: {"county":$("#county").val(),"time":$("#time").val()},
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
              		    title: "新增水质监测采样信息",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/waterinfo/add'/*,
						btn: ['保存','关闭'],
				        yes: function(){
				            layer.closeAll();
				        },
				        btn2: function(){
				            layer.close();
				        }*/
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		          layer.alert('编辑 [id]：'+ checkStatus.data[0].gid);
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		          layer.msg('删除');
		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
			dataTable.reload({//表格数据重新加载
				  where: {"county":$("#county").val(),"time":$("#time").val()},
				  page: {curr: 1}
			});
		});
	}
	
	function init() {
		$("#county").val("刚察县");
		
		render();
		bindEvent();
	}
	
	init();
});
