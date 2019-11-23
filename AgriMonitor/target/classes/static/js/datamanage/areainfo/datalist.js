layui.use(['form','layer','table','upload','laydate','util'], function(form,layer,table,upload,laydate,util) {
	var winH=$(window).height();
	var datatable;
	
	function render(){
		//表格渲染
		datatable=table.render({
			id:"demo",
		    elem: '#demo',
		    method:'post',
		    url: '/areainfo/datalist', //数据接口，
		    height:winH-105,
		    page: false, //开启分页
		    cols: [[ //表头
		      {field: 'gid', title: 'ID',hide: true,align:'center'},
		      {field: 'xzmc', title: '行政名称',align:'center',width:150},
		      {field: 'xzms', title: '行政描述',align:'center'},
		      {width: 120, align:'center', toolbar: '#barDemo'}
		    ]]
		});
	}
	
	function bindEvent(){
		  //监听行工具事件
		  table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
		    var data = obj.data //获得当前行数据
		    ,layEvent = obj.event; //获得 lay-event 对应的值
		    if(layEvent === 'edit'){
		    	layer.open({
          		    title: "行政信息维护",
					type: 2,
					area: ['560px', '350px'],
					scrollbar: true,
					content: '/areainfo/update?gid='+data.gid
				});
		    }
		  });
	}
	render();
	bindEvent();
});