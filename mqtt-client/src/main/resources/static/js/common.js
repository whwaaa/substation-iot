var url = window.location.origin;//"http://192.168.0.104"
// var url = "http://localhost"

var settings;
var debug = true;
var iotAccount = $.cookie('iot-username');
var iotName = $.cookie('iot-name');
var lastPage = $.cookie("iot-lastPage");
var iotSettings = $.cookie("iot-settings");//{"theme":"dark","authArea":{"peidianshi":1}}

if ( iotAccount === undefined ){
	if ( !window.location.href.endsWith("login.html") ){ //无cookie并且不是login.html,则跳转到login.html进行登录
	// if (debug) window.location.href = "login.html"; //$.cookie('iot-username', "username", { expires: 7, path: '/' });
	if (!debug)
		window.location.href = url + "/login.html";
	}
} else {
	if ( ( window.location.href.endsWith("index.html") || window.location.href.endsWith("192.168.0.104/") )
		&& lastPage !== undefined ) {//有cookie,index.html页面中,cookie存有page,则跳转到存储的page

		$.cookie('iot-lastPage', lastPage, { expires: 7, path: '/' });
		if(debug) window.location.href = lastPage;
		if(!debug)
		window.location.href = url + "/" + lastPage;
	}
}

function loginPage(){
	// 跳到登陆界面, 传入当前URL作为参数, 登陆成功再跳回来
	setTimeout(function (){
		let callBackUrL = window.location.href;
		if(debug) window.location.href = "login.html";
		if(!debug)
			window.location.href = url + "/login.html";
		// if(crossDomainMode){
		// 	window.location.href = projectUrl + xhr.getResponseHeader("CONTENTPATH") + "?callBackUrL=" + callBackUrL;
		// }else{
		// 	window.location.href = xhr.getResponseHeader("CONTENTPATH") + "?callBackUrL=" + callBackUrL;
		// }
	}, 1500)
}
	
// ajax配合拦截器跳转登陆界面
function myComplete(xhr, status){
	// 通过xhr取得响应头
	let REDIRECT = xhr.getResponseHeader("REDIRECT");
	let TOKEN_MSG = xhr.getResponseHeader("TOKEN_MSG");
	// 如果响应头中包含 REDIRECT 则说明是拦截器返回的
	if(REDIRECT === "REDIRECT"){
		layer.closeAll();
		if(TOKEN_MSG === "no-token"){
			layer.msg("请先登陆")
			loginPage();
		}else if(TOKEN_MSG === "token-invalid"){
			layer.msg("登陆信息过期,请再次登陆")
			loginPage();
		}else if(TOKEN_MSG === "noauth-monitor") {
			layer.msg("监视权限不足，请询问管理员开通！")
			setTimeout( function(){
				$.cookie('iot-lastPage', "", { expires: -1, path: '/' });
				if(debug) window.location.href = "index.html";
				if(!debug)
					window.location.href = url + "/index.html";
			} , 2000);
		}else if(TOKEN_MSG === "noauth-control") {
			queryUserSettings();
			layer.msg("无控制权限，请询问管理员开通！", function () {})
		}
		// else if(TOKEN_MSG === "relogin") {
		// 	layer.msg("账号信息更新，请重新登录！", function(){})
		// 	setTimeout( function(){
		// 		$.cookie('iot-lastPage', "", { expires: -1, path: '/' });
		// 		if(debug) window.location.href = "login.html";
		// 		if(!debug)
		// 			window.location.href = url + "/login.html";
		// 	} , 2000);
		// }
	}
}

function queryUserSettings(){
	$.ajax({
		type: "GET",
		url: url + "/user/username/" + iotAccount,
		async: false,//同步更新
		dataType: "json",
		xhrFields: {
			// 允许携带cookie跨域
			withCredentials: true
		},
		crossDomain: true,
		success: function (data) {
			if (data.code === 200) {
				$.cookie('iot-username', data.obj.account, { expires: 365, path: '/' });
				$.cookie('iot-name', data.obj.name, { expires: 365, path: '/' });
				$.cookie('iot-settings', data.obj.settings, { expires: 365, path: '/' });
				iotAccount = data.obj.account;
				iotName = data.obj.name;
				iotSettings = data.obj.settings;
			} else {
				layer.msg(data.msg, function(){})
			}
		},
		error: function(e){
			layer.msg("服务器异常  GET: " + url + "/user/username/" + iotAccount, function(){})
		},
		complete : function(xhr, status) {
			myComplete(xhr, status);
		}
	})
}

function updateUserSettings(){
	$.ajax({
		type: "POST",
		url: url + "/user/username/" + iotAccount,
		data: {settings:settings,_method:"PUT"},
		dataType: "json",
		xhrFields: {
			// 允许携带cookie跨域
			withCredentials: true
		},
		crossDomain: true,
		success: function (data) {
			if (data.code === 200) {
				$.cookie('iot-settings', settings, { expires: 365, path: '/' });
			} else {
				layer.msg(data.msg, function(){})
			}
		},
		error: function(e){
			layer.msg("服务器异常  PUT: " + url + "/user/username/" + iotAccount, function(){})
		},
		complete : function(xhr, status) {
			myComplete(xhr, status);
		}
	})
	
	
}
	
$(function(){
	$(document).ready(function(){
		//$.cookie('iotIsLogin', "true", { expires: 7, path: '/' });
		if ( iotSettings !== undefined ){
			let theme = $.parseJSON(iotSettings).theme;
			if ( theme === "dark" ){
				$("html").addClass("darkness")
			} else if ( theme === "light" ) {
				$("html").removeClass("darkness")
			}
		}
		$("html").removeClass("hidden");
		
		$(".theme .left").on("click",function(){
			if (!debug) {
			let set = $.parseJSON(iotSettings);
			set.theme = "light";
			settings = JSON.stringify(set);
			updateUserSettings();
			}
			$("html").removeClass("darkness");
		})
		$(".theme .right").on("click",function(){
			if (!debug) {
			let set = $.parseJSON(iotSettings);
			set.theme = "dark";
			settings = JSON.stringify(set);
			updateUserSettings();
			}
			$("html").addClass("darkness");
		})
		
		$(".lnr-user").on("click",function(){
			window.location.href = url + "/usermanage.html";
		})
	})
})
