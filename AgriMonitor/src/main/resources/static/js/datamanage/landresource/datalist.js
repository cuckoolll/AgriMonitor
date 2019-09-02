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
			      {field: 'year', title: '年份',align:'center',rowspan:2,width:90},
			      {title: '土地资源',align:'center',colspan:3},
			      {title: '农作物种类及播种面积（（hm2））',align:'center',colspan:3},
			      {title: '土壤理化性质',align:'center',colspan:9}
			    ],[
		      {field: 'qhh_sw', title: '耕地面积（hm2）',align:'center',width:130},
		      {field: 'qhh_mj', title: '高标准农田面积（hm2）',align:'center',width:130},
		      {field: 'qhh_yd', title: '高标准农田比重（%）',align:'center',width:130},
		      {field: 'qhh_ph', title: '青稞',align:'center',width:100},
		      {field: 'll_zws', title: '油菜',align:'center',width:100},
		      {field: 'll_zzyl', title: '燕麦',align:'center',width:100},
		      {field: 'fyzw_md', title: '单产（kg）',align:'center',width:130},
		      {field: 'fyzw_pjmd', title: '总产（吨）',align:'center',width:130},
		      {field: 'fyzw_swl', title: '有机质（g/kg）',align:'center',width:130},
		      {field: 'fyzw_pjswl', title: '全氮（g/kg）',align:'center',width:130},
		      {field: 'fydw_md', title: '全磷含量(g/kg)',align:'center',width:130},
		      {field: 'fydw_pjmd', title: '有效磷含量(mg/kg)',align:'center',width:130},
		      {field: 'fydw_swl', title: '全钾含量(g/kg)',align:'center',width:130},
		      {field: 'fydw_pjswl', title: 'pH',align:'center',width:130},
		      {field: 'dxdw_md', title: '盐度（%）',align:'center',width:130},
		      {field: 'dxdw_swl', title: '速效钾含量(mg/kg)',align:'center',width:130},
		      {field: 'dxdw_pjmd', title: '缓效钾含量(mg/kg)',align:'center',width:130}
		    ]]
		});
	}
	
	render();
});