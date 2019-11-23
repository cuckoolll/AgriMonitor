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
		    url: '/farmerlifeinfo/datalist', //数据接口，
		    height:winH-105,
		    page: true, //开启分页
		    limit:20,
		    limits:[20,40,60,100],
		    cols: [[ 
		    	{type:'checkbox',rowspan:2},
			     {field: 'county', title: '区（县、市）',width:120,rowspan:2, align:'center'},
			     {field: 'towns', title: '乡镇',rowspan:2, align:'center',width:130},
			     {field: 'village', title: '下辖地',rowspan:2, align:'center',width:150},
			      {field: 'year',title: '年份',align:'center',rowspan:2,width:100},
			      {title: '人均总收入',align:'center',colspan:4, align:'center'},
			      {field: 'rjkzpsr',title: '人均可支配收入',align:'center',rowspan:2,width:150},
			      {field: 'srzzl',title: '人均可支配收入年增长率',align:'center',rowspan:2,width:200},
			      {field: 'ljsj',title: '实施生活垃圾集中收集',align:'center',rowspan:2,width:200},
			      {field: 'ljcll',title: '农村生活垃圾处理率',align:'center',rowspan:2,width:170},
			      {field: 'wsclxzcs',title: '有生活污水处理设施',align:'center',rowspan:2,width:170},
			      {field: 'wscll',title: '农村生活污水处理率',align:'center',rowspan:2,width:170}
			    ],
			    [{field: 'rjsr_ny', title: '农业收入', align:'center',width:150},
			    {field: 'rjsr_cmy', title: '畜牧业收入', align:'center',width:150},
			    {field: 'rjsr_ly', title: '林业收入', align:'center',width:150},
			    {field: 'rjsr_fwy', title: '服务业收入', align:'center',width:150}
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
              		    title: "新增农民生活信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update'
					});
		      break;
		      case 'update':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else if(data.length > 1){
		          layer.msg('只能同时编辑一个');
		        } else {
		        	layer.open({
              		    title: "修改农民生活信息",
						type: 2,
						area: ['700px', '500px'],
						scrollbar: true,
						content: '/farmerlifeinfo/update?gid='+checkStatus.data[0].gid
					});
		        }
		      break;
		      case 'delete':
		        if(data.length === 0){
		          layer.msg('请选择一行');
		        } else {
		        	layer.confirm('确定要删除选择的农民生活信息吗？', function(index){
		        	        layer.close(index);
		        	        var gids=[];
				        	$.each(data,function(index,item){
				        		gids.push(item.gid);
				        	});
				        	$.ajax({
				        		type:"post",
				        		url:"/farmerlifeinfo/doDel",
				        		contentType:"application/json",
				        		data: JSON.stringify(gids),
				        		dataType:"json",
				        		success:function(res){
				        			if(res && res.code==0){
							        	  layer.msg('删除农民生活数据成功');
							        	  datatable.reload({//表格数据重新加载
							        		  where: {syear: $("#date_year").val(),eyear: $("#date_year1").val()},page: {curr: 1}
							        	  });
							          }else{
							        	  layer.msg('删除农民生活数据失败');
							          }
				        		},
				        		error:function(){
				        			layer.msg('删除农民生活数据失败');
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