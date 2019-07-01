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
			 elem: '#grid',
			 url: '/waterinfo/queryWaterInfo', //数据接口
			 method: 'post',
			 where: {"county":$("#county").val(),"time":$("#time").val()},
		     page: true, //开启分页
		     cols: [[ //表头
		    	 {field: 'quality_address', title: '采样地点', width:'20%'},
		    	 {field: 'quality_time', title: '采样时间', width:'20%', sort: true},
		    	 {field: 'quality_type', title: '分析项目', width:'20%'}, 
		    	 {field: 'quality_result', title: '分析结果（mg/L）', width:'20%'},
		    	 {field: 'remarks', title: '备注', width:'20%'}
			 ]]
		  });
		
		//文件上传
		upload.render({
		    elem: '#importBtn',
		    url: '/waterinfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	debugger;
		    	if(res){
		    		if(res.code==0){
		    			dataTable.reload({//表格数据重新加载
		  				  where: {"county":$("#county").val(),"time":$("#time").val()},
		  				  page: {curr: 1}
		    			});
				      }else{
				    	  layer.msg(res.msg);
				      }
		    	}
		    }
		});
	}
	
	function bindEvent() {
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
