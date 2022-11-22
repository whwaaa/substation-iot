
//var admin = false;
var admin = false;
// var iotAccount = $.cookie('iot-username');
// var iotName = $.cookie('iot-name');
var iotAccount = "admin"
var iotName = "管理员"

if ( iotAccount == "admin" ) {
	admin = true;
	$("html").addClass("admin");
} else {
	admin = false;
	$("html").removeClass("admin");
}

$(document).ready(function(){ 
	
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
	

	function newPasswdCheck(){
		if ($(".newPasswd").val().trim().length >= 8 ){
			return $(".newPasswd").val().trim();
		}
		return false;
	}
	function oldPasswdCheck(){
		if ($(".oldPasswd").val().trim().length >= 1 ){
			return $(".oldPasswd").val().trim();
		}
		return false;
	}
	
	// 修改密码
	function passwdModify(newPasswd, oldPasswd){
		let account = $(".account").text().trim();
		$.ajax({
			type: "POST",
			url: url + "/user/modify",
			data: {
				_method:"PUT",
				account:account,
				oldPasswd:oldPasswd,
				newPasswd:newPasswd
			},
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				if (data.code == 200) {
					layer.msg("密码修改成功!")
					$.cookie('iot-time', 0, { expires: 1, path: '/' });
					setTimeout(function() {
						window.location.href = url + "/login.html";
					}, 1500);
				}
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/user/modify/", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
	}
	
	
	var userList;
	var userListLenth = 0;
	var eachIndex = 0;
	// 删除账号
	function unAuthAccount(account){
		let res = confirm("确认销毁 " + account + " 账号?");
		if ( res ) {
			layer.load(0)
			$.ajax({
				type: "POST",
				url: url + "/user/" + userList[eachIndex].id,
				data: {account:account,_method:"DELETE"},
				dataType: "json",
				xhrFields: {
					// 允许携带cookie跨域
					withCredentials: true
				},
				crossDomain: true,
				success: function (data) {
					layer.closeAll();
					if (data.code == 204) {
						//列表移除,切换下一个账号
						userList.splice(eachIndex,1);
						userListLenth--;
						if ( eachIndex == userListLenth ) eachIndex = 0;
						layer.msg("已删除！");
						if ( userListLenth == 0 ) {
							$(".auth-account").prop("placeholder","暂无需要授权注册的账号")
							$(".auth-msg").prop("placeholder","暂无需要授权账号的昵称")
							$(".auth-account").val("");
							$(".auth-msg").val("");
						} else {
							$(".auth-account").val(userList[eachIndex].account);
							$(".auth-msg").val(userList[eachIndex].name);
						}
					} else {
						layer.msg(data.msg, function(){})
					}
				},
				error: function(e){
					layer.msg("服务器异常  DELETE: " + url + "/user/" + userList[eachIndex].id, function(){})
				},
				complete : function(xhr, status) {
					myComplete(xhr, status);
				}
			})
		}
	}
	// 账号授权
	function authAccount(account){
		let res = confirm("确认授权 " + account + " 账号?");
		if ( res ) {
			layer.load(0)
			$.ajax({
				type: "POST",
				url: url + "/user/auth",
				data: {account:account,_method:"PUT"},
				dataType: "json",
				xhrFields: {
					// 允许携带cookie跨域
					withCredentials: true
				},
				crossDomain: true,
				success: function (data) {
					layer.closeAll();
					if (data.code == 200) {
						//列表移除,切换下一个账号
						userList.splice(eachIndex,1);
						userListLenth--;
						if ( eachIndex == userListLenth ) eachIndex = 0;
						layer.msg("授权成功！");
						if ( userListLenth == 0 ) {
							$(".auth-account").prop("placeholder","暂无需要授权注册的账号")
							$(".auth-msg").prop("placeholder","暂无需要授权账号的昵称")
							$(".auth-account").val("");
							$(".auth-msg").val("");
						} else {
							$(".auth-account").val(userList[eachIndex].account);
							$(".auth-msg").val(userList[eachIndex].name);
						}
					} else {
						layer.msg("授权失败", function(){})
					}
				},
				error: function(e){
					layer.msg("服务器异常  GET: " + url + "/user/modify/", function(){})
				},
				complete : function(xhr, status) {
					myComplete(xhr, status);
				}
			})
		}
	}
	// 查询未授权的所有账号
	function queryNoAuthAccount() {
		// layer.load(0)
		$.ajax({
			type: "GET",
			url: url + "/user/isdeletelist",
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				if (data.code == 200) {
					
					userList = data.obj;
					userListLenth = data.obj.length;
					if (userListLenth >= 0) {
						$(".auth-account").val(userList[eachIndex].account);
						$(".auth-msg").val(userList[eachIndex].name);
					}
					
					// 遍历填充
					$(".authpre").on("click", function(){
						if ( ++eachIndex >= userListLenth ) eachIndex = 0;
						$(".auth-account").val(userList[eachIndex].account);
						$(".auth-msg").val(userList[eachIndex].name);
					})
					$(".authnext").on("click", function(){
						if ( --eachIndex < 0 ) eachIndex = userListLenth-1;
						$(".auth-account").val(userList[eachIndex].account);
						$(".auth-msg").val(userList[eachIndex].name);
					})
					$(".unauthbut").on("click", function(){
						// 删除账号
						unAuthAccount($(".auth-account").val());
					})
					$(".authbut").on("click", function(){
						// 确认授权
						authAccount($(".auth-account").val());
					})
				} else {
					// 没有未授权账号
					$(".auth-account").prop("placeholder","暂无需要授权注册的账号")
					$(".auth-msg").prop("placeholder","暂无需要授权账号的昵称")
					$(".auth-account").val("");
					$(".auth-msg").val("");
				}
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/user/isdeletelist/", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
	}
	
	
	var resetUserList;
	var resetUserListLenth = 0;
	var resetEachIndex = 0;
	// 撤销重置
	function unResetAccount(account){
		let res = confirm("确认撤销 " + account + " 密码重置操作?");
		if ( res ) {
			layer.load(0)
			$.ajax({
				type: "POST",
				url: url + "/user/unreset",
				data: {account:account,_method:"PUT"},
				dataType: "json",
				xhrFields: {
					// 允许携带cookie跨域
					withCredentials: true
				},
				crossDomain: true,
				success: function (data) {
					layer.closeAll();
					if (data.code == 200) {
						//列表移除,切换下一个账号
						resetUserList.splice(resetEachIndex,1);
						resetUserListLenth--;
						if ( resetEachIndex == resetUserListLenth ) resetEachIndex = 0;
						layer.msg(data.msg);
						if ( resetUserListLenth == 0 ) {
							$(".reset-account").prop("placeholder","暂无需要重置密码的账号申请")
							$(".reset-account").val("");
						} else {
							$(".reset-account").val(resetUserList[resetEachIndex].account);
						}
					} else {
						layer.msg(data.msg, function(){})
					}
				},
				error: function(e){
					layer.msg("服务器异常  PUT: " + url + "/user/unreset/", function(){})
				},
				complete : function(xhr, status) {
					myComplete(xhr, status);
				}
			})
		}
	}
	// 重置密码
	function resetAccount(account){
		let res = confirm("确认重置 " + account + " 账号的密码?");
		if ( res ) {
			layer.load(0)
			$.ajax({
				type: "POST",
				url: url + "/user/reset",
				data: {account:account,_method:"PUT"},
				dataType: "json",
				xhrFields: {
					// 允许携带cookie跨域
					withCredentials: true
				},
				crossDomain: true,
				success: function (data) {
					layer.closeAll();
					if (data.code == 200) {
						//列表移除,切换下一个账号
						resetUserList.splice(resetEachIndex,1);
						resetUserListLenth--;
						if ( resetEachIndex == resetUserListLenth ) resetEachIndex = 0;
						layer.msg(data.msg);
						if ( resetUserListLenth == 0 ) {
							$(".reset-account").prop("placeholder","暂无需要重置密码的账号申请")
							$(".reset-account").val("");
						} else {
							$(".reset-account").val(resetUserList[resetEachIndex].account);
						}
					} else {
						layer.msg(data.msg, function(){})
					}
				},
				error: function(e){
					layer.msg("服务器异常  PUT: " + url + "/user/reset/", function(){})
				},
				complete : function(xhr, status) {
					myComplete(xhr, status);
				}
			})
		}
	}
	// 查询重置密码的所有账号
	function queryresetAccount() {
		// layer.load(0)
		$.ajax({
			type: "GET",
			url: url + "/user/resetpasswd",
			dataType: "json",
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				if (data.code == 200) {
					
					resetUserList = data.obj;
					resetUserListLenth = data.obj.length;
					if (resetUserListLenth >= 0) {
						$(".reset-account").val(resetUserList[resetEachIndex].account);
					}
					
					// 遍历填充
					$(".resetpre").on("click", function(){
						if ( ++resetEachIndex >= resetUserListLenth ) resetEachIndex = 0;
						$(".reset-account").val(resetUserList[resetEachIndex].account);
					})
					$(".resetnext").on("click", function(){
						if ( --resetEachIndex < 0 ) resetEachIndex = resetUserListLenth-1;
						$(".reset-account").val(resetUserList[resetEachIndex].account);
					})
					$(".unresetbut").on("click", function(){
						// 撤销重置
						unResetAccount($(".reset-account").val());
					})
					$(".resetbut").on("click", function(){
						// 确认重置
						resetAccount($(".reset-account").val());
					})
				} else {
					// 没有未授权账号
					$(".reset-account").prop("placeholder","暂无需要重置密码的账号")
					$(".reset-account").val("");
				}
			},
			error: function(e){
				layer.msg("服务器异常  GET: " + url + "/user/resetpasswd/", function(){})
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
		
	}
	
	$(".account").text(iotAccount);
	$(".name").text(iotName);

	$(".theme .left").on("click",function(){
		$("html").removeClass("darkness");
	})
	$(".theme .right").on("click",function(){
		$("html").addClass("darkness");
	})
	$(".return").on("click",function(){
		window.location.href = url + "/index.html";
	})
	
	// 修改密码
	$(".passwdModify").on("click", function(){
		let newPasswd = newPasswdCheck();
		if ( newPasswd != false ) {
			let oldPasswd = oldPasswdCheck();
			if ( oldPasswd != false ) {
				passwdModify(newPasswd, oldPasswd);
			} else {
				layer.msg("旧密码输入错误。",function(){})
			}
		} else {
			layer.msg("新密码至少8位",function(){})
		}
	})
	
	// 管理员
	if ( admin == true ) {
		queryNoAuthAccount();
		queryresetAccount();
	}
	
}); 	