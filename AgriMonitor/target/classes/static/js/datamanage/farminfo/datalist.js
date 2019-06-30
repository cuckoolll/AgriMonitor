layui.use(['form','layer','table','upload'], function(form,layer,table,upload) {
	var winH=$(window).height();
	function render(){
		//表格渲染
		table.render({
		    elem: '#datalist',
		    url: '/user/findAll1', //数据接口，
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
		var fileListView = $('#fileList'),
		    uploadListIns = upload.render({
			    elem: '#selFileBtn',
			    url: '/upload/',
			    accept: 'file',
			    multiple: true,
			    auto: false,
			    bindAction: '#fileUploadBtn',
			    choose: function(obj){   
			      var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
			      //读取本地文件
			      obj.preview(function(index, file, result){
				        var tr = '<tr id="upload-'+ index +'">'
				        		 +'<td>'+ file.name +'</td>'
				        		 +'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
				        		 +'<td>等待上传</td>'
					             +'<td>'
					             +'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
					             +'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
					             +'</td></tr>';
				        //单个重传
				        tr.find('.demo-reload').on('click', function(){
				          obj.upload(index, file);
				        });
				        
				        //删除
				        tr.find('.demo-delete').on('click', function(){
				          delete files[index]; //删除对应的文件
				          tr.remove();
				          uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
				        });
				        
				        fileListView.append(tr);
			      });
			    },
			    done: function(res, index, upload){
			      if(res.code == 0){ //上传成功
			        var tr = fileListView.find('tr#upload-'+ index),tds = tr.children();
			        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
			        tds.eq(3).html(''); //清空操作
			        return delete this.files[index]; //删除文件队列已经上传成功的文件
			      }
			      this.error(index, upload);
			    },
			    error: function(index, upload){
			      var tr = demoListView.find('tr#upload-'+ index)
			      ,tds = tr.children();
			      tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
			      tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
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
		$("#dataImportBtn").click(function(){
			layer.open({
				  type: 1,
				  title:'导入养殖场信息',
				  area: ['800px', '400px'],
				  content: $('#dataImportDiv') //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
				});
		});
	}
	render();
	bindEvent();
});