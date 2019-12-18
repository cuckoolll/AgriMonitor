layui.config({
    base: '../../temp/treetable-lay/'
}).extend({
    treetable: 'treetable'
}).use(['form','layer','table','treetable','laydate','util','element'], function(form,layer,table,treetable,laydate,util,element) {
	var winH=$(window).height();

	var curyear=util.toDateString(new Date(), 'yyyy');
	var curmonth=util.toDateString(new Date(), 'yyyy-MM');
	laydate.render({
	    elem: '#year',
	    type: 'year',
	    value:curyear,
	    done: function(value, date, endDate){
	    	render(value);
	    }
	 });
	laydate.render({
	    elem: '#month',
	    type: 'month',
	    value:curmonth,
	    done: function(value, date, endDate){
	    	rendermonthdata(value);
	    }
	 });
	function render(date){
		//表格渲染
		datatable=treetable.render({
			id:"datalist",
		    elem: '#datalist',
		    url: '/animalsBreed/animalsBreedAnalysis/getYearData', //数据接口，
		    height:winH-190,
		    where: {year: date},
		    /*toolbar: true,*/
		    treeColIndex: 0,
	        treeSpid: 0,
	        treeIdName: 'fgid',
	        treePidName: 'parent_id',
	        treeDefaultClose: false,
	        treeLinkage: false,
		    cols: [[ 
			      {field: 'target_name', title: '指标名称',align:'left',rowspan:2,width:220},
			      {field: 'nccl', title: '年初存栏数',align:'center',rowspan:2,width:150},
			      {field: 'nfmc', title: '其中:能繁殖母畜',align:'center',rowspan:2,width:150},
			      {title: '增加',align:'center',colspan:2},
			      {title: '减少',align:'center',colspan:3},
			      {title: '产量',align:'center',colspan:4}
			    ],[
		      {field: 'child_size', title: '产仔数',align:'center',width:150},
		      {field: 'survival_size', title: '成活数',align:'center',width:150},
		      {field: 'death_size', title: '损亡数',align:'center',width:150},
		      {field: 'maturity_size', title: '出栏数',align:'center',width:150},
		      {field: 'sell_size', title: '出售数',align:'center',width:150},
		      {field: 'meat_output', title: '肉产量',align:'center',width:150},
		      {field: 'milk_output', title: '奶产量',align:'center',width:150},
		      {field: 'egg_output', title: '蛋产量',align:'center',width:150},
		      {field: 'hair_output', title: '毛产量',align:'center',width:150}
		    ]]
		});
	}

	function rendermonthdata(date){
		//表格渲染
		datatable=treetable.render({
			id:"datalist1",
		    elem: '#datalist1',
		    url: '/animalsBreed/animalsBreedAnalysis/getMonthData', //数据接口，
		    height:winH-190,
		    where: {month: date.replace('-','')},
		    /*toolbar: true,*/
		    treeColIndex: 0,
	        treeSpid: 0,
	        treeIdName: 'fgid',
	        treePidName: 'parent_id',
	        treeDefaultClose: false,
	        treeLinkage: false,
		    cols: [[ 
			      {field: 'target_name', title: '指标名称',align:'left',rowspan:2,width:220},
			      {title: '存栏情况',align:'center',colspan:3},
			      {title: '产仔情况',align:'center',colspan:6},
			      {title: '成畜损亡情况',align:'center',colspan:4},
			      {title: '出栏情况',align:'center',colspan:2},
			      {title: '畜产品生产情况',align:'center',colspan:4}
			    ],[
			  {field: 'nccl', title: '年初存栏',align:'center',width:150},
			  {field: 'surplus_size', title: '月末存栏数',align:'center',width:150},
			  {field: 'female_size', title: '能繁殖母畜',align:'center',width:150},
		      {field: 'child_size', title: '产仔数',align:'center',width:150},
		      {field: 'czs_tq', title: '去年同期',align:'center',width:150},
		      {field: 'survival_size', title: '成活数',align:'center',width:150},
		      {field: 'chs_tq', title: '去年同期',align:'center',width:150},
		      {field: 'chl', title: '成活率',align:'center',width:150},
		      {field: 'chl_tq', title: '去年同期',align:'center',width:150},
		      {field: 'death_size', title: '损亡数',align:'center',width:150},
		      {field: 'sws_tq', title: '去年同期',align:'center',width:150},
		      {field: 'swl', title: '损亡率',align:'center',width:150},
		      {field: 'swl_tq', title: '去年同期',align:'center',width:150},
		      {field: 'maturity_size', title: '出栏数',align:'center',width:150},
		      {field: 'sell_size', title: '出售数',align:'center',width:150},
		      {field: 'meat_output', title: '肉产量',align:'center',width:150},
		      {field: 'milk_output', title: '奶产量',align:'center',width:150},
		      {field: 'egg_output', title: '蛋产量',align:'center',width:150},
		      {field: 'hair_output', title: '毛产量',align:'center',width:150}
		    ]]
		});
	}
	function bindEvent(){
		$("#exportYearData").click(function(){
			window.open("/animalsBreed/animalsBreedAnalysis/exportYearData?year="+$("#year").val());
		});
		$("#exportMonthData").click(function(){
			window.open("/animalsBreed/animalsBreedAnalysis/exportMonthData?month="+$("#month").val().replace('-',''));
		});
	}
	
	render(curyear);
	rendermonthdata(curmonth);
	bindEvent();
});
