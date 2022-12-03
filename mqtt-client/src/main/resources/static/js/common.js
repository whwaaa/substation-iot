var url = window.location.origin;//"http://192.168.0.104"
// var url = "http://localhost"

var iotAccount = $.cookie('iot-username');
var iotName = $.cookie('iot-name');
var lastPage = $.cookie("iot-lastPage");
var iotSettings = $.cookie("iot-settings");//"{\"theme\":\"dark\"}"
var settings;

var debug = false;

if ( iotAccount == undefined ){
	if ( !window.location.href.endsWith("login.html") ){ //无cookie并且不是login.html,则跳转到login.html进行登录
	if (debug) window.location.href = "login.html"; //$.cookie('iot-username', "username", { expires: 7, path: '/' });
	if (!debug)
		window.location.href = url + "/login.html";
	}
} else {
	if ( window.location.href.endsWith("index.html") 
		&& lastPage != undefined ) {//有cookie,index.html页面中,cookie存有page,则跳转到存储的page
		$.cookie('iot-lastPage', lastPage, { expires: 7, path: '/' });
		if(debug) window.location.href = lastPage;
		if(!debug)
		window.location.href = url + "/" + lastPage;
	}
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
			if (data.code == 200) {
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
		if ( iotSettings != undefined ){
			let theme = $.parseJSON(iotSettings).theme;
			if ( theme == "dark" ){
				$("html").addClass("darkness")
			} else if ( theme == "light" ) {
				$("html").removeClass("darkness")
			}
		}
		$("html").removeClass("hidden");
		
		$(".theme .left").on("click",function(){
			let set = $.parseJSON(iotSettings);
			set.theme = "light";
			settings = JSON.stringify(set);
			updateUserSettings();
			$("html").removeClass("darkness");
		})
		$(".theme .right").on("click",function(){
			let set = $.parseJSON(iotSettings);
			set.theme = "dark";
			settings = JSON.stringify(set);
			updateUserSettings();
			$("html").addClass("darkness");
		})
		
		$(".lnr-user").on("click",function(){
			window.location.href = url + "/usermanage.html";
		})
	})
})
