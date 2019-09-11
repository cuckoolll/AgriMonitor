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
		    url: '/agriinputinfo/datalist', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
		    	{type:'checkbox',rowspan:3},
			      {field: 'gid', title: 'ID',hide: true,align:'center',rowspan:3},
			      {field: 'year', title: '年份',align:'center',rowspan:3,width:90},
			      {title: '农业投入品',align:'center',colspan:4},
			      {title: '农业废弃物',align:'center',colspan:7}
			    ],[ 
			      {title: '化肥施用强度（折纯量）',align:'center',colspan:3},
			      {field: 'lyqd',title: '农药施用强度（折百量）',align:'center',rowspan:2,width:200},
			      {field: 'jgzl', title: '秸秆总量',align:'center',rowspan:2,width:140},
			      {field: 'zhlyjg', title: '综合利用的秸秆量',align:'center',rowspan:2,width:140},
			      {field: 'jgksj', title: '秸秆可收集资源量',align:'center',rowspan:2,width:140},
			      {field: 'lmzl', title: '回收利用的农膜总量',align:'center',rowspan:2,width:140},
			      {field: 'lmsyl', title: '农膜使用总量',align:'center',rowspan:2,width:140},
			      {field: 'qcfl', title: '综合利用的畜禽粪污量',align:'center',rowspan:2,width:140},
			      {field: 'qcfzl', title: '畜禽粪污总量',align:'center',rowspan:2,width:140}
			    ],[
		      {field: 'n', title: 'N',align:'center',width:100},
		      {field: 'p', title: 'P',align:'center',width:100},
		      {field: 'yjf', title: '有机肥',align:'center',width:100}
		    ]]
		});
	}
	
	function bindEvent(){
		//监听工具条
		table.on('toolbar(datalist)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id)
		    ,data = checkStatus.data; //获取选中的数据
		    switch(obj.event){
		      case 'add':
		    	  layer.open({
              		    title: "新增农业投入信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/agriinputinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农业投入信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/agriinputinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农业投入信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/agriinputinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农业投入数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农业投入数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农业投入数据失败');
				        		}
				        	});
		        	});
		        }
		      break;
		    };
		  });
		//查询数据
		$("#queryBtn").click(function(){
			datatable.reload({//表格数据重新加载
				where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
			});
		});
	}
	render();
	bindEvent();
});