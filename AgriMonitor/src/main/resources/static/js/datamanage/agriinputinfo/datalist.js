layui.use(['form','layer','table','upload','laydate','util'], function(form,layer,table,upload,laydate,util) {
	var winH=$(window).height();
	var datatable;
	
	var curyear=util.toDateString(new Date(), 'yyyy');
	function render(){
		laydate.render({
		    elem: '#date_year',
		    type: 'year',
		    value:curyear
		 });
		laydate.render({
		    elem: '#date_year1',
		    type: 'year',
		    value:curyear
		 });
		//表格渲染
		datatable=table.render({
			id:"datalist",
		    elem: '#datalist',
		    method:'post',
		    toolbar: '#barDemo',
		    url: '/landresource/data', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:3},
			      {field: 'year', title: '年份',align:'center',rowspan:3,width:90},
			      {title: '农业投入品',align:'center',colspan:4},
			      {title: '农业废弃物',align:'center',colspan:7}
			    ],[ 
			      {title: '化肥施用强度（折纯量）',align:'center',colspan:3},
			      {title: '农药施用强度（折百量）',align:'center',rowspan:2,width:200},
			      {field: 'zzycl1', title: '秸秆总量',align:'center',rowspan:2,width:140},
			      {field: 'cmycl1', title: '综合利用的秸秆量',align:'center',rowspan:2,width:140},
			      {field: 'yycl1', title: '秸秆可收集资源量',align:'center',rowspan:2,width:140},
			      {field: 'zzycl1', title: '回收利用的农膜总量',align:'center',rowspan:2,width:140},
			      {field: 'cmycl1', title: '农膜使用总量',align:'center',rowspan:2,width:140},
			      {field: 'yycl1', title: '综合利用的畜禽粪污量',align:'center',rowspan:2,width:140},
			      {field: 'yycl1', title: '畜禽粪污总量',align:'center',rowspan:2,width:140}
			    ],[
		      {field: 'zzycl1', title: 'N',align:'center',width:100},
		      {field: 'cmycl1', title: 'P',align:'center',width:100},
		      {field: 'yycl1', title: '有机肥',align:'center',width:100}
		    ]]
		});
	}
	
	render();
});