
$(function(){

	var promptMessage = {
		"client001":{"dId":"1","serial":"0","on":"大门已打开","off":"大门已关闭","oning":"大门打开中...","offing":"大门关闭中..."},
		"client002":{"dId":"2","serial":"1","on":"防鼠挡板已打开","off":"防鼠挡板已关闭","oning":"防鼠挡板打开中...","offing":"防鼠挡板关闭中..."},
		"client003":{"dId":"3","serial":"2","on":"感应地灯已开启","off":"感应地灯已关闭","oning":"感应地灯开启中...","offing":"感应地灯关闭中..."}
	}

	// ajax配合拦截器跳转登陆界面
	function myComplete(xhr, status){
		// 通过xhr取得响应头
		let REDIRECT = xhr.getResponseHeader("REDIRECT");
		let TOKEN_MSG = xhr.getResponseHeader("TOKEN_MSG");
		// 如果响应头中包含 REDIRECT 则说明是拦截器返回的
		if(REDIRECT == "REDIRECT"){
			if(TOKEN_MSG == "no-token"){
				layer.msg("请先登陆")
			}else if(TOKEN_MSG == "token-invalid"){
				layer.msg("登陆信息过期,请再次登陆")
			}
			// 跳到登陆界面, 传入当前URL作为参数, 登陆成功再跳回来
			setTimeout(function (){
				let callBackUrL = window.location.href;
				window.location.href = url + "/login.html";
				// if(crossDomainMode){
				// 	window.location.href = projectUrl + xhr.getResponseHeader("CONTENTPATH") + "?callBackUrL=" + callBackUrL;
				// }else{
				// 	window.location.href = xhr.getResponseHeader("CONTENTPATH") + "?callBackUrL=" + callBackUrL;
				// }
			}, 1500)
		}
		// $.cookie('iotIsLogin', "true", { expires: 7, path: '/' });
	}

	// /* 根据开关状态更新提示信息 */
	// function updatePromptBySw(){	
	// 	for( var i=0; i<3; i++ ) {
	// 		if ( $(".mui-switch").eq(i).hasClass("mui-active") ) {
	// 			$(".prompt").eq(i).text(promptMessage[i].on);
	// 		} else {
	// 			$(".prompt").eq(i).text(promptMessage[i].off);
	// 		}
	// 	}
	// }
	// /* 在执行操作中根据开关状态更新提示信息 */
	// function updatePromptBySwOnExecuting( index ){
	// 	if ( $(".mui-switch").eq(index).hasClass("mui-active") ) {
	// 		$(".prompt").eq(index).text(promptMessage[index].oning);
	// 	} else {
	// 		$(".prompt").eq(index).text(promptMessage[index].offing);
	// 	}
	// }
	var client001StateQueryOk = true;
	var client002StateQueryOk = true;
	var client003StateQueryOk = true;
	
	function queryClient001State(){
		client001StateQueryOk = false;
		$.ajax({
			type: "GET",
			url: url + "/device/state/client001",
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				if (data.code == 200 && data.msg=="ok"){
					
					$(".state").eq(promptMessage.client001.serial).addClass("state1");
					$(".state").eq(promptMessage.client001.serial).removeClass("state3 state2");
					
					$.ajax({
						type: "GET",
						url: url + "/devicemsg/latest/" + promptMessage.client001.dId,
						dataType: "json",
						xhrFields: {
							// 允许携带cookie跨域
							withCredentials: true
						},
						crossDomain: true,
						success: function (data) {
							 // '1:开启 2:关闭'
							if (data.code == 200 && data.msg=="ok"){
								if ( data.obj.state == 1 ) {
									$(".con").eq(promptMessage.client001.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client001.serial).addClass("mui-active");
									$(".prompt").eq(promptMessage.client001.serial).text(promptMessage.client001.on);
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client001.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client001.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client001.serial).text(promptMessage.client001.off);
								} else {
									layer.msg("状态未知,服务器异常,请联系管理员.",function(){})
								}
							}else{
								layer.msg(data.msg,function(){})
							}
							client001StateQueryOk = true;
						},
						error: function(e){
							layer.msg("服务器异常  GET: " + url + "/devicemsg/latest/" + promptMessage.client001.dId, function(){})
						},
						complete : function(xhr, status) {
							myComplete(xhr, status);
						}
					})
					
				} else {
					$(".con").eq(promptMessage.client001.serial).find(".right").addClass("hidden");
					$(".state").eq(promptMessage.client001.serial).addClass("state3");
					$(".state").eq(promptMessage.client001.serial).removeClass("state1 state2");
					client001StateQueryOk = true;
				}
				
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/device/state/client001", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
		
	}
	function queryClient002State(){
		client002StateQueryOk = false;
		$.ajax({
			type: "GET",
			url: url + "/device/state/client002",
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				
				if (data.code == 200 && data.msg=="ok"){
					
					$(".state").eq(promptMessage.client002.serial).addClass("state1");
					$(".state").eq(promptMessage.client002.serial).removeClass("state3 state2");
					
					$.ajax({
						type: "GET",
						url: url + "/devicemsg/latest/" + promptMessage.client002.dId,
						dataType: "json",
						xhrFields: {
							// 允许携带cookie跨域
							withCredentials: true
						},
						crossDomain: true,
						success: function (data) {
							 // '1:开启 2:关闭'
							if (data.code == 200 && data.msg=="ok"){
								if ( data.obj.state == 1 ) {
									$(".con").eq(promptMessage.client002.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client002.serial).addClass("mui-active");
									$(".prompt").eq(promptMessage.client002.serial).text(promptMessage.client002.on);
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client002.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client002.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client002.serial).text(promptMessage.client002.off);
								} else {
									layer.msg("状态未知,服务器异常,请联系管理员.",function(){})
								}
								
							}else{
								layer.msg(data.msg,function(){})
							}
							client002StateQueryOk = true;
						},
						error: function(e){
							layer.msg("服务器异常  GET: " + url + "/devicemsg/latest/" + promptMessage.client002.dId, function(){})
						},
						complete : function(xhr, status) {
							myComplete(xhr, status);
						}
					})
					
				} else {
					$(".con").eq(promptMessage.client002.serial).find(".right").addClass("hidden");
					$(".state").eq(promptMessage.client002.serial).addClass("state3");
					$(".state").eq(promptMessage.client002.serial).removeClass("state1 state2");
					client002StateQueryOk = true;
				}
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/device/state/client002", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
		
	}
	function queryClient003State(){
		client003StateQueryOk = false;
		$.ajax({
			type: "GET",
			url: url + "/device/state/client003",
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				
				if (data.code == 200 && data.msg=="ok"){
					$(".state").eq(promptMessage.client003.serial).removeClass("state3 state2");
					$(".state").eq(promptMessage.client003.serial).addClass("state1");
					
					$.ajax({
						type: "GET",
						url: url + "/devicemsg/latest/" + promptMessage.client003.dId,
						dataType: "json",
						xhrFields: {
							// 允许携带cookie跨域
							withCredentials: true
						},
						crossDomain: true,
						success: function (data) {
							 // '1:开启 2:关闭'
							if (data.code == 200 && data.msg=="ok"){
								if ( data.obj.state == 1 ) {
									$(".con").eq(promptMessage.client003.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client003.serial).addClass("mui-active");
									$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.on);
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client003.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client003.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.off);
								} else {
									layer.msg("状态未知,服务器异常,请联系管理员.",function(){})
								}
								
							}else{
								layer.msg(data.msg,function(){})
							}
							client003StateQueryOk = true;
						},
						error: function(e){
							layer.msg("服务器异常  GET: " + url + "/devicemsg/latest/" + promptMessage.client003.dId, function(){})
						},
						complete : function(xhr, status) {
							myComplete(xhr, status);
						}
					})
				} else {
					$(".con").eq(promptMessage.client003.serial).find(".right").addClass("hidden");
					$(".state").eq(promptMessage.client003.serial).removeClass("state1 state2");
					$(".state").eq(promptMessage.client003.serial).addClass("state3");
					client003StateQueryOk = true;
				}
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/device/state/client003", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
	}
	
	$(document).ready(function(){ 

		//查询在线状态
		var queryStateTime = setInterval( function(){
			if ( client001StateQueryOk ) queryClient001State();
			if ( client002StateQueryOk ) queryClient002State();
			if ( client003StateQueryOk ) queryClient003State();
		}, 1000 );
		



		//查询数据库状态

		// updatePromptBySw();
		
	    $(".mui-switch").on("click",function(){
			var index = $(".mui-switch").index(this);
			if ($(this).hasClass("mui-active")){
				console.log("第"+index+"个开关 执行打开")
			} else {
				console.log("第"+index+"个开关 执行关闭")
			}
			updatePromptBySwOnExecuting(index);
			setTimeout(function() {
				updatePromptBySw();
			}, 3000);
		})
		
		$(".theme .left").on("click",function(){
			$("html").removeClass("darkness");
		})
		$(".theme .right").on("click",function(){
			$("html").addClass("darkness");
		})
		
		$(".lnr-user").on("click",function(){
			window.location.href = url + "/usermanage.html";
		})
	}); 	
})