layui.use(['form','layer'], function(form,layer,table,upload) {
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

		if($(_this).attr("class") == "menuFA"){
			$("#frameMainTitle span").html('<i class="iconfont icon-xianshiqi"></i>'+$(_this).text());
		}else{
			//显示父菜单
			$("#frameMainTitle span").html('<i class="iconfont icon-xianshiqi"></i>'+$(_this).parent().parent().siblings(".menuFA").text()
					+"&nbsp;>&nbsp;"+$(_this).text());
		}
		
		//处理菜单样式变化
		$(_this).css("cssText", "background-color:#009688 !important;").css("color","#FFF");
		$(_this).parent().siblings().find("a").css("cssText", "background-color:#transparent").css("color","#c2c2c2");
		$(_this).parent().parent().parent().siblings().find("dl dt a").css("cssText", "background-color:#transparent").css("color","#c2c2c2")
		
	}

	//初始化页面
	function init(){
		var win_h = $(window).height();
		var win_w = $(window).width();
		var frameMenuW = $(".frameMenu").width();
		var logoH = 60;
		var frameTopH = $(".frameTop").height();
		
		$(".frameMenu").height(win_h);
		$(".frameMenu .menu").height(win_h - logoH);
		$(".main").width(win_w - frameMenuW).height(win_h);
		$(".frameMain").height(win_h - frameTopH);
		$(".frameMain .con").height(win_h - frameTopH - 40);
		$(".frameMain .con iframe").height(win_h - frameTopH - 40);
		
		//自定义滚动条
		$(".menu").mCustomScrollbar();
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
	
	init();
	bindEvent();
});

