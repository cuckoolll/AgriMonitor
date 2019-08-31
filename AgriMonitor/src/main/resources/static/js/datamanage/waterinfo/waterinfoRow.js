layui.use(['table', 'form', 'laydate', 'layer', 'upload'], function(table, form, laydate, layer, upload) {
	var dataTable;
	var timeControl;	  
	var winH=$(window).height();
	
	/**
	 * 表格渲染 .
	 */
	function render() {
		timeControl = laydate.render({
			elem: '#quality_time'
		}); 
		
		//表格渲染
		renderTable();
		
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
		    				where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val()},
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
		var param = {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val()};
		$.post("/waterinfo/queryQualityType", param, function(res){
			var cols = [];
			cols.push({type: 'checkbox', fixed: 'left'});
			cols.push({field: 'quality_address', title: '采样地点', width:200});
			cols.push({field: 'quality_time', title: '采样时间' ,width:120 , sort: true});
			for (var i = 0; i < res.length; i++) {
				cols.push(res[i]);
			}
			dataTable = table.render({
				 id:'datalist',
				 elem: '#datalist',
				 height:winH-110,
				 toolbar: '#barDemo',
				 url: '/waterinfo/queryWaterInfoOnLine', //数据接口
				 method: 'post',
				 where: {"quality_address":$("#quality_address").val(),"quality_time":$("#quality_time").val()},
			     page: true, //开启分页
			     limit:20,
				 limits:[20,40,60,100],
			     cols: [cols]
			  });
		});
	}
	

	
	function bindEvent() {
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
