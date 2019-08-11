layui.use(['form','layer','table'], function(form,layer,table) {
	var target_type_opt=['','','',''];
	var target_opt=['<option value="animals_size">牲畜存栏</option>',
		'<option value="animals_size">牲畜存栏</option>',
		'<option value="surplus_size">月末存栏数</option><option value="female_size">能繁殖母畜</option>'
		+'<option value="child_size">产仔数</option><option value="survival_size">成活数</option>'
		+'<option value="death_size">损亡数</option><option value="maturity_size">出栏数</option>'
		+'<option value="sell_size">出售数</option><option value="meat_output">肉产量</option>'
		+'<option value="milk_output">奶产量</option><option value="egg_output">蛋产量</option>'
		+'<option value="hair_output">毛产量</option>',
		'<option value="planted_area">农作物播种面积（万亩）</option><option value="planted_output">农作物总产（万公斤）</option>'];
	function init(){
		//获取指标类型
		$.post("/monitorManage/findTargetType", {},function(res){
	          if(res){
	        	  if(res.animalstype){
	        		  var str1='';
	        		  $.each(res.animalstype,function(index,item){
	        			  str1+='<option value="'+item.gid+'">'+item.type_name+'</option>';
		        	  });
	        		  target_type_opt[0]=str1;
	        		  target_type_opt[1]=str1;
	        	  }
	        	  if(res.animalstarget){
	        		  var str2='';
	        		  $.each(res.animalstarget,function(index,item){
	        			  str2+='<option value="'+item.gid+'">'+item.target_name+'</option>';
		        	  });
	        		  target_type_opt[2]=str2;
	        	  }
	        	  if(res.cropstype){
	        		  var str3='';
	        		  $.each(res.cropstype,function(index,item){
	        			  str3+='<option value="'+item.gid+'">'+item.type_name+'</option>';
		        	  });
	        		  target_type_opt[3]=str3;
	        	  }
	          }
	          var gid = getUrlParam("gid");
	  	      if(gid){//如果有值，为更新操作
	  			//查询数据并赋值到表单中
	  			$.post("/monitorManage/findById", {gid:gid},function(res){
	  		          if(res){
	  		        	  $("select[name=target_type]").html(target_type_opt[res.monitor_type-1]);
	  		  			  $("select[name=target]").html(target_opt[res.monitor_type-1]);
	  		        	  $.each(res,function(key,val){
	  		        		  if(key=='monitor_type'||key=='target'||key=='target_type'||key=='conditions'||key=='stopflag'){
	  		        			  $("[name='"+key+"'] option[value='"+val+"']").attr("selected","true");
	  		        			  form.render('select');
	  		        		  }else{
	  		        			  $("[name='"+key+"']").val(val);
	  		        		  }
	  		        	  });
	  		          }else{
	  		        	  layer.msg('加载监测数据信息失败');
	  		        	  $("#saveBtn").attr('disabled',true);
	  		          }
	  	        });
	  		}else{
	  			  $("select[name=target_type]").html(target_type_opt[0]);
	  			  $("select[name=target]").html(target_opt[0]);
	  			  form.render('select');
	  		}
       });
	}
	
	function bindEvent(){
		//监听提交
		form.on('submit(submitBut)', function(data) {
			data.field.condition_showname=
			$("#monitor_type").find("option:selected").text()
			+"["
			+$("#target_type").find("option:selected").text()+"."
			+$("#target").find("option:selected").text()
			+$("#conditions").find("option:selected").text()
			+$("#value_set").val()
			+"]"
			$.post("/monitorManage/doSave", data.field,function(res){
		          if(res && res.code==0){
		        	  parent.layer.msg('保存监测数据数据成功');
		        	  parent.layui.table.reload('datalist');
		        	  var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					  parent.layer.close(index); //再执行关闭
		          }else{
		        	  layer.msg('保存监测数据数据失败');
		          }
	        });
			return false;
		});
		form.on('select(monitor_type)', function(data){
			$("select[name=target_type]").html(target_type_opt[data.value-1]);
			$("select[name=target]").html(target_opt[data.value-1]);
			form.render('select');
		});
	}
	//获取url参数
	function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
	
	init();
	bindEvent();
});