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
		    	 {field: 'quality_address', title: '采样地点'},
		    	 {field: 'quality_time', title: '采样时间', sort: true},
		    	 {field: 'quality_type', title: '分析项目'}, 
		    	 {field: 'quality_result', title: '分析结果（mg/L）'},
		    	 {field: 'remarks', title: '备注'},
		    	 {title: '操作', align:'center', toolbar: '#barDemo'}
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
		table.on('tool(datalist)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		  var data = obj.data; //获得当前行数据
		  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		  var tr = obj.tr; //获得当前行 tr 的DOM对象
		  if(layEvent === 'del'){ //删除
		    layer.confirm('真的删除行么', function(index){
		      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
		      layer.close(index);
		      //向服务端发送删除指令
		    });
		  } else if(layEvent === 'edit'){ //编辑
		    //do something
		    //同步更新缓存对应的值
		    obj.update({
		      username: '123'
		      ,title: 'xxx'
		    });
		  }
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
