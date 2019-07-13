layui.config({
    base: '../../temp/treetable-lay/'
}).extend({
    treetable: 'treetable'
}).use(['form','layer','table','treetable','upload','laydate','util'], function(form,layer,table,treetable,upload,laydate,util) {
	var winH=$(window).height();
	var datatable;
	var curdate;
	
	var rq=getUrlParam("rq");//,towns=unescape(getUrlParam("towns"));
	if(rq){
		curdate=rq;
	}else{
		curdate=util.toDateString(new Date(), 'yyyy-MM');
	}
	/*if(towns){
		$("[id='towns'] option[value='"+towns+"']").attr("selected","true");
		form.render('select');
	}*/
	laydate.render({
	    elem: '#date_month',
	    type: 'month',
	    value:curdate
	 });
	
	function render(date){
		//表格渲染
		datatable=treetable.render({
			id:"datalist",
		    elem: '#datalist',
		    url: '/animalsBreed/datalist', //数据接口，
		    height:winH-80,
		    //where: {date_month: date,towns:$("#towns").val()},
		    where: {date_month: date},
		    /*toolbar: true,*/
		    treeColIndex: 1,
	        treeSpid: 0,
	        treeIdName: 'fgid',
	        treePidName: 'parent_id',
	        treeDefaultClose: false,
	        treeLinkage: false,
		    cols: [[ 
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:2},
			      {field: 'target_name', title: '指标名称',align:'left',rowspan:2,width:220},
			      /*{field: 'county', title: '区（县、市）',align:'center',width:120,rowspan:2},*/
			      /*{field: 'towns', title: '乡镇',align:'center',width:120,rowspan:2},*/
			      {field: 'surplus_size', title: '月末存栏数',align:'center',rowspan:2},
			      {field: 'female_size', title: '能繁殖母畜',align:'center',rowspan:2},
			      {title: '增加',align:'center',colspan:2},
			      {title: '减少',align:'center',colspan:3},
			      {title: '产量',align:'center',colspan:4},
			      {templet: '#oper-col', title: '操作',align:'center',rowspan:2,width:70}
			    ],[
		      {field: 'child_size', title: '产仔数',align:'center'},
		      {field: 'survival_size', title: '成活数',align:'center'},
		      {field: 'death_size', title: '损亡数',align:'center'},
		      {field: 'maturity_size', title: '出栏数',align:'center'},
		      {field: 'sell_size', title: '出售数',align:'center'},
		      {field: 'meat_output', title: '肉产量',align:'center'},
		      {field: 'milk_output', title: '奶产量',align:'center'},
		      {field: 'egg_output', title: '蛋产量',align:'center'},
		      {field: 'hair_output', title: '毛产量',align:'center'}
		    ]]
		});
		//文件上传
		upload.render({
		    elem: '#dataImportBtn',
		    url: '/animalsBreed/dataImport',
		    accept: 'file',
		    exts: 'xls|xlsx',
		    done: function(res){
		    	if(res){
		    		if(res.code==0){
		    			layer.msg('导入数据成功');
		    			render($("#date_month").val().replace('-',''));
				      }else{
				    	  layer.msg(res.msg);
				      }
		    	}
		    }
		});
	}
	
	function bindEvent(){
		//查询数据
		$("#queryBtn").click(function(){
			render($("#date_month").val().replace('-',''));
		});
		//监听工具条
		table.on('tool(datalist)', function(obj){
			var data = obj.data;
		    switch(obj.event){
		      case 'edit':
		    	  layer.open({
            		    title: "修改数据",
						type: 2,
						area: ['800px', '500px'],
						scrollbar: true,
						content: '/animalsBreed/update?gid='+data.gid
					});
		      break;
		      case 'del':
		        
		      break;
		    };
		  });
	}
	//获取url参数
	function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
	render(curdate.replace('-',''));
	bindEvent();
});