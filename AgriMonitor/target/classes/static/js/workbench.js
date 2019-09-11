layui.use(['form','layer'], function(form,layer) {
	  
	function menuFAMouseenter(_this){
		_this.animate({
		    borderWidth:'7px'
		},200);
	}

	function menuFAMouseleave(_this){
		_this.animate({
		    borderWidth:'0px'
		},200);
	}

	function menuFAClick(_this){
		var dl = _this.siblings("dl");
		var bgColor = "#282a32";
		if(dl.length > 0){
			if(dl.css("display") == "none"){
				dl.show();
				_this.find(".right").attr("class","iconfont icon-arrow-down right");
				bgColor = "#282a32";
			}else{
				dl.hide();
				_this.find(".right").attr("class","iconfont icon-dajiantouyou right");
				bgColor = "transparent";
			}
		}
		_this.css("background-color",bgColor);
		_this.parent().siblings().find("dl").hide();
		_this.parent().siblings().find("a.menuFA").css("background-color","transparent");
	}

	//二级菜单点击后的处理方法
	function menuCAClick(url,_this){
		if(!url){
			return;
		}
		//处理frameMain url地址
		$("#mainIframe").attr("src",url);
		
		//处理frameMain title名称变化
		if($(_this).find("i").attr("class") == "iconfont icon-yonghu1"){
			$("#frameMainTitle span").html('<i class="iconfont icon-xianshiqi"></i>个人资料');
			return;
		}

		if($(_this).hasClass("menuFA")){
			$("#nav").html($(_this).text());
		}else{
			//显示父菜单
			$("#nav").html($(_this).parent().parent().siblings(".menuFA").text()
					+"&nbsp;/&nbsp;"+$(_this).text());
		}
		
		//处理菜单样式变化
		if ($(_this).hasClass("menu3rd")) {
			$(_this).css("cssText", "background-color:#009688 !important;").css("color","#FFF").css("padding-left","60px");
		} else if ($(_this).hasClass("menuFA")) {
			$(_this).css("cssText", "background-color:#009688 !important;").css("color","#FFF");
		}
		$(_this).parent().siblings().find(".menu3rd").css("cssText", "background-color:#transparent").css("color","#c2c2c2").css("padding-left","60px");
		$(_this).parent().parent().parent().siblings().find(".menu3rd").css("cssText", "background-color:#transparent").css("color","#c2c2c2").css("padding-left","60px");

		
	}

	//初始化页面
	function init(){
		var win_h = $(window).height();
		var win_w = $(window).width();
		var frameMenuW = $(".frameMenu").width() * (win_w/1920).toFixed(0);
		var logoH = 60;
		var frameTopH = $(".frameTop").height();
		
		
		$(".frameMenu").height(win_h);
		$(".frameMenu .menu").height(win_h - logoH);
		$(".main").width(win_w - frameMenuW).height(win_h);
		$(".frameMain").height(win_h - frameTopH);
		$(".frameMain .con").height(win_h - frameTopH);
		$(".frameMain .con iframe").height(win_h - frameTopH);
		
		$("#monitorSpan").hide();
		
		//自定义滚动条
		$(".menu").mCustomScrollbar();
		
		$.get("http://wthrcdn.etouch.cn/weather_mini?citykey=101150806", {},function(res){
			 if(res){
				 var o=eval("(" + res + ")");
				 if(o && o.data.forecast){
					 $("#tqxx").html("今日天气："+o.data.forecast[0].type+"&nbsp;&nbsp;"
							 +o.data.forecast[0].high+"&nbsp;&nbsp;"
							 +o.data.forecast[0].low);/*if(o.data.forecast.length>1){
						 $("#tqxx").html("今日天气："+o.data.forecast[0].type+"&nbsp;&nbsp;"
								 +o.data.forecast[0].high+"&nbsp;&nbsp;"
								 +o.data.forecast[0].low+"&nbsp;&nbsp;"
								 +o.data.forecast[0].fengxiang+"&nbsp;&nbsp;"
								 +"明日天气："+o.data.forecast[0].type+"&nbsp;&nbsp;"
								 +o.data.forecast[0].high+"&nbsp;&nbsp;"
								 +o.data.forecast[0].low+"&nbsp;&nbsp;"
								 +o.data.forecast[0].fengxiang);
					 }*/
				 }
			 }
		});
	}
	$(window).resize(function(){
		init();
	});
	
	function bindEvent(){
		//菜单
		$(".menuFA").click(function(){
			menuFAClick($(this));
		});
		$(".menuFA").mouseenter(function(){
			menuFAMouseenter($(this));
		});
		$(".menuFA").mouseleave(function(){
			menuFAMouseleave($(this));
		});
		$(".menuLA").click(function(){
			menuCAClick($(this).attr("url"),$(this));
		});
		$("#updpw").click(function(){
			layer.open({
      		    title: "修改密码",
				type: 2,
				area: ['400px', '380px'],
				scrollbar: true,
				content: '/userinfo/updpw'
			});
		});
	}
	function monitorinfo(){
		 $.post("/monitorManage/findmonitorinfo", {},function(res){
		        if(res && res.length>0){
		        	$("#monitorSpan").show();
		        	$(".box").show();
		        	var str="";
		        	$.each(res,function(index,item){
		        		if(item.ratio==0 || item.ratio>=1){
		        			str+="<p class='item'>"+item.log+"</p>";
		        		}else if(item.ratio>=0.5 && item.ratio<1){
		        			str+="<p class='item' style='color: orange;'>"+item.log+"</p>";
		        		}else if(item.ratio>=0.1 && item.ratio<0.5){
		        			str+="<p class='item' style='color: yellow;'>"+item.log+"</p>";
		        		}else if(item.ratio<0.1){
		        			str+="<p class='item' style='color: blue;'>"+item.log+"</p>";
		        		}else{
		        			str+="<p class='item'>"+item.log+"</p>";
		        		}
		        	});
		        	$("#monitorinfo").html(str);
		        	
		        	//if(res.length>1){
		        	      var wrapper;
		        		  var offset = 50
		        		  var timer;
		        		  setInterval(function () {
		        			wrapper  = document.getElementsByClassName('wrapper')[0];
		        		    if(timer) {
		        		      clearInterval(timer);
		        		    }
		        		    if($("#monitorinfo p").length==1){
		        		    	$("#monitorSpan").show();
		        		    	wrapper.style="";
		        		    }else if($("#monitorinfo p").length>1){
		        			   var step = 1;
		        			   timer = setInterval(function () {
				        		      wrapper.style.transform = 'translateY(-' + (offset + step) + 'px)';
				        		      if(step == 50) {
				        		        clearInterval(timer);
				        		      }
				        		      step++;
				        		    }, 10);
				        		    
				        		    offset += 50;
				        		    var num = Math.floor(wrapper.offsetHeight / 50)
				        		    if (! (offset%((num - 1)*50))) {
				        		      offset = 0;
				        		    }
		        		   }else{
		        			   $("#monitorSpan").hide();
		       		           $(".box").hide();
		        		   }
		        		  }, 8000);
		        	}
		        //} else {
		        //	$("#monitorSpan").hide();
		        //	$(".box").hide();
		       // }
		    });
	}
	
	init();
	monitorinfo();
	bindEvent();
});

