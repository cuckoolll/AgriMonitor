layui.use(['form','layer','table','upload'], function(form,layer,table,upload) {
	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=table.render({
		    elem: '#datalist',
		    method:'post',
		    url: '/farminfo/datalist', //数据接口，
		    height:winH-80,
		    page: true, //开启分页
		    cols: [[ //表头
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'user_name', title: '区（县、市）',align:'center',width:120},
		      {field: 'farm_name', title: '养殖场名称',align:'center'},
		      {field: 'farm_address', title: '地址',align:'center'},
		      {field: 'legal_person', title: '法人',align:'center',width:100},
		      {field: 'phone_num', title: '联系电话',align:'center',width:150},
		      {field: 'animals_name', title: '认定畜种',align:'center'},
		      {field: 'animals_size', title: '牲畜存栏（头、只）',align:'center'},
		      {field: 'remarks', title: '备注',align:'center'},
		      {title: '操作', align:'center', toolbar: '#barDemo'}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/farminfo/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		      console.log(res)
		    }
		});
	}
	
	function bindEvent(){
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
			datatable.reload({//表格数据重新加载
				  where: {
					  farmname: $("#farmname").val(),type: $("#type").val()
				  },page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});