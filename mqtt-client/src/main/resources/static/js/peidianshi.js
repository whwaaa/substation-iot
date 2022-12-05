var bootModeUpdateLock = false;//焦点在辅助照明控制面板时锁定设置更新
$(function(){
	
	var canControl = false;

	var promptMessage = {
		"client001":{"topic":"iot-client001","class":"door","dId":"1","serial":"0","on":"大门已合好","off":"大门未合好","oning":"大门打开中...","offing":"大门关闭中..."},
		"client002":{"topic":"iot-client002","class":"mouse-board","dId":"2","serial":"1","on":"防鼠挡板已合好","off":"防鼠挡板未合好","oning":"防鼠挡板打开中...","offing":"防鼠挡板关闭中..."},
		"client003":{"topic":"iot-client003","class":"device-light","dId":"3","serial":"2","on":"人体感应已开启","off":"人体感应关闭","oning":"人体感应开启中...","offing":"人体感应关闭中..."}
	}


	var receiveAssistLightSetting = {};
	var sendAssistLightSetting = {};
	//辅助照明灯操作模式调节
	//开启模式监听、检测模式、隐藏、显示、动态调节div高度、监听撤销恢复
	
	var bootMode;//开启模式 0、1、2、3
	var MoreSettingOnlyRunningOnce = true;//更多设置只能运行一次
	var ti1_1, ti2_1, ti1_2, ti2_2;
	function updateDivHeight(mode){
		switch(mode) {
			case 0:
				$(".choseTime1").removeClass("hidden");
				$(".choseTime2").removeClass("hidden");
				$(".light_value").removeClass("hidden");
				$(".content>div:nth-child(3)").animate({ "height":350 },400);
				break;
			
			case 1:
				$(".choseTime1").removeClass("hidden");
				$(".choseTime2").removeClass("hidden");
				$(".light_value").addClass("hidden");
				$(".content>div:nth-child(3)").animate({ "height":315 },400);
				break;
			
			case 2:
				$(".choseTime1").addClass("hidden");
				$(".choseTime2").addClass("hidden");
				$(".light_value").removeClass("hidden");
				$(".content>div:nth-child(3)").animate({ "height":315 },400);
				break;
				
			case 3:
				$(".choseTime1").addClass("hidden");
				$(".choseTime2").addClass("hidden");
				$(".light_value").addClass("hidden");
				$(".content>div:nth-child(3)").animate({ "height":270 },400);
				break;
		}
	}
	function hiddenLigth(){
		$(".conadd").addClass("hidden")
		
	}
	function updateLigthSetting(){
		$(".conadd").removeClass("hidden");
		$(".lightUnSave").removeClass("hidden");
		$(".lightSave").removeClass("hidden");
		if ( !bootModeUpdateLock ) {
			$("input[name='mode']").eq(parseInt(receiveAssistLightSetting["bootMode"])).prop("checked",true)
			updateDivHeight(parseInt(receiveAssistLightSetting["bootMode"]));
			// var nowHour = new Date().getHours();
			// var nowMin = (new Date().getMinutes()/5).toFixed() * 5;
			$(".hour1").animate({"scrollTop":parseInt(receiveAssistLightSetting["startTimeHour"])*30});
			$(".min1").animate({"scrollTop":parseInt(receiveAssistLightSetting["startTimeMin"])*6});
			$(".hour2").animate({"scrollTop":parseInt(receiveAssistLightSetting["endTimeHour"])*30});
			$(".min2").animate({"scrollTop":parseInt(receiveAssistLightSetting["endTimeMin"])*6});
			$(".light_value_ipt").val(receiveAssistLightSetting["lightValue"]);
			$(".light_value_ipt").val(receiveAssistLightSetting["lightValue"]);
		} else {
			
		}
	}
	function deviceConnectionException(){//设备连接异常，需要删除更多设置的监听、恢复div高度、删除动态创建的元素
		$(".conadd").addClass("hidden");
		$(".lightUnSave").addClass("hidden");
		$(".lightSave").addClass("hidden");
		$(".content>div:nth-child(3)").animate({ "height":160 },400);
	}
	function assistFloodLightOpreate(){
		if ( MoreSettingOnlyRunningOnce ) {
			MoreSettingOnlyRunningOnce = false;
			/*-------- 创建时间text --------*/
			$(".min1").append("<div class='min-first min-text'>00</div>")
			$(".min1").append("<div class='min-text'>05</div>")
			$(".min2").append("<div class='min-first min-text'>00</div>")
			$(".min2").append("<div class='min-text'>05</div>")
			for(var i=0; i<=9; i++){
				if(i==0){ 
					$(".hour1").append("<div class='hour-first hour-text'>00</div>")
					$(".hour2").append("<div class='hour-first hour-text'>00</div>")
				}else{
					$(".hour1").append("<div class='hour-text'>0"+i+"</div>")
					$(".hour2").append("<div class='hour-text'>0"+i+"</div>")
				}
				$(".min1").append("<div class='min-text'>"+(i+2)*5+"</div>")
				$(".min2").append("<div class='min-text'>"+(i+2)*5+"</div>")
			}
			for(var i=10; i<=23; i++){
				$(".hour1").append("<div class='hour-text'>"+i+"</div>")
				$(".hour2").append("<div class='hour-text'>"+i+"</div>")
			}
			
			/*-------- 监听时间滑动 --------*/
			$(".hour1").on("touchstart",function(event){
				bootModeUpdateLock = true;
				setTimeout(ti1_1);
				$(this).stop(true,true);//清除元素的所有动画，让当前动画直接到达末状态
				event.stopPropagation();
			}).on("touchmove",function(event){
			}).on("touchcancel touchend",function(event){
				// 获取scroll
				var hour_1;
				ti1_1 = setTimeout(function(){
					var margintop_1 = $(".hour1").scrollTop()
					hour_1 = parseInt((margintop_1+15)/30)
					$(".hour1").animate({"scrollTop":hour_1*30})
				},100)
				event.stopPropagation();
			})
			$(".min1").on("touchstart",function(event){
				bootModeUpdateLock = true;
				clearTimeout(ti2_1);
				$(this).stop(true,true);//清除元素的所有动画，让当前动画直接到达末状态
				event.stopPropagation();
			}).on("touchmove",function(event){
			}).on("touchcancel touchend",function(event){
				// 获取scroll 
				var min_1;	// 真实值 = min*5
				ti2_1 = setTimeout(function(){
					var margintop_1 = $(".min1").scrollTop()
					min_1 = parseInt((margintop_1+15)/30)
					$(".min1").animate({"scrollTop":min_1*30})
				},100)
				event.stopPropagation();
			})
			
			$(".hour2").on("touchstart",function(){
				bootModeUpdateLock = true;
				clearTimeout(ti1_2);
				$(this).stop(true,true);//清除元素的所有动画，让当前动画直接到达末状态
				event.stopPropagation();
			}).on("touchmove",function(event){
			}).on("touchcancel touchend",function(event){
				// 获取scroll
				var hour_2;
				ti1_2 = setTimeout(function(){
					var margintop_2 = $(".hour2").scrollTop()
					hour_2 = parseInt((margintop_2+15)/30)
					$(".hour2").animate({"scrollTop":hour_2*30})
				},100)
				event.stopPropagation();
			})
			$(".min2").on("touchstart",function(){
				bootModeUpdateLock = true;
				clearTimeout(ti2_2);
				$(this).stop(true,true);//清除元素的所有动画，让当前动画直接到达末状态
				event.stopPropagation();
			}).on("touchmove",function(event){
			}).on("touchcancel touchend",function(event){
				// 获取scroll 
				var min_2;	// 真实值 = min*5
				ti2_2 = setTimeout(function(){
					var margintop_2 = $(".min2").scrollTop()
					min_2 = parseInt((margintop_2+15)/30)
					$(".min2").animate({"scrollTop":min_2*30})
				},100)
				event.stopPropagation();
			})
			
			$(".light_value h3").eq(0).on("click", function(e){
				bootModeUpdateLock = true;
				let v = parseInt($(".light_value_ipt").val()) - 100;
				if ( v >= 0 ) {
					$(".light_value_ipt").val( v );
				} else {
					$(".light_value_ipt").val( 0 );
				}
				e.stopPropagation();
			})
			$(".light_value h3").eq(1).on("click", function(e){
				bootModeUpdateLock = true;
				let v = parseInt($(".light_value_ipt").val()) + 100;
				if ( v <= 1000 ) {
					$(".light_value_ipt").val( v );
				} else {
					$(".light_value_ipt").val( 1000 );
				}
				e.stopPropagation();
			})
			
			//模式0
			$("#mode_tim_light").on("click", function(e){
				bootModeUpdateLock = true;
				updateDivHeight(0);
				e.stopPropagation();
			});
			//模式1
			$("#mode_tim").on("click", function(e){
				bootModeUpdateLock = true;
				updateDivHeight(1);
				e.stopPropagation();
			});
			//模式2
			$("#mode_light").on("click", function(e){
				bootModeUpdateLock = true;
				updateDivHeight(2);
				e.stopPropagation();
			});
			//模式3
			$("#mode_manual").on("click", function(e){
				bootModeUpdateLock = true;
				updateDivHeight(3);
				e.stopPropagation();
			});
		
			//撤销
			$(".lightUnSave").on("click", function(e){
				bootModeUpdateLock = false;
				updateLigthSetting();
				e.stopPropagation();
			})
			//保存
			$(".lightSave").on("click", function(e){
				if ( canControl ) {
					sendAssistLightSetting["bootMode"] = parseInt($("input[name='mode']:checked").val());
					sendAssistLightSetting["startTimeHour"] = parseInt(($(".hour1").scrollTop()/30).toFixed());
					sendAssistLightSetting["startTimeMin"] = parseInt(($(".min1").scrollTop()/6).toFixed());
					sendAssistLightSetting["endTimeHour"] = parseInt(($(".hour2").scrollTop()/30).toFixed());
					sendAssistLightSetting["endTimeMin"] = parseInt(($(".min2").scrollTop()/6).toFixed());
					sendAssistLightSetting["lightValue"] = parseInt($(".light_value_ipt").val());

					layer.load(0)
					$.ajax({
						type: "POST",
						url: url + "/device/" + promptMessage.client003.dId,
						dataType: "json",
						data: {settings:JSON.stringify(sendAssistLightSetting),_method:"PUT"},
						xhrFields: {
							// 允许携带cookie跨域
							withCredentials: true
						},
						crossDomain: true,
						success: function (data) {
							layer.closeAll()
							if(data.code == 200){
								layer.msg("保存成功");
								receiveAssistLightSetting = sendAssistLightSetting;
								sw3Control();
								bootModeUpdateLock = false;							
							}
						},
						error: function(e){
							layer.msg("服务器异常  GET: " + url + "/device/" + promptMessage.client003.dId, function(){})
						},
						complete : function(xhr, status) {
							
							myComplete(xhr, status);
						}
					})
					e.stopPropagation();
				} else {
					layer.msg("操控功能已上锁，请点击右下角解锁。")
				}
			})
		
		}
	}


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
									$(".con").eq(promptMessage.client001.serial).parent().removeClass("open");
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client001.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client001.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client001.serial).text(promptMessage.client001.off);
									$(".con").eq(promptMessage.client001.serial).parent().addClass("open");
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
									$(".con").eq(promptMessage.client002.serial).parent().removeClass("open");
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client002.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client002.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client002.serial).text(promptMessage.client002.off);
									$(".con").eq(promptMessage.client002.serial).parent().addClass("open");
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
					
					//更新设置参数
					receiveAssistLightSetting = $.parseJSON(data.obj.settings);
					
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
								//设置界面参数更新
								updateLigthSetting();
								if ( data.obj.state == 1 ) {
									$(".con").eq(promptMessage.client003.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client003.serial).addClass("mui-active");
									$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.on);
									$(".con").eq(promptMessage.client003.serial).parent().addClass("open");
								} else if ( data.obj.state == 2 ){
									$(".con").eq(promptMessage.client003.serial).find(".right").removeClass("hidden");
									$(".mui-switch").eq(promptMessage.client003.serial).removeClass("mui-active");
									$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.off);
									$(".con").eq(promptMessage.client003.serial).parent().removeClass("open");

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
				} else {//设备断开连接
					$(".con").eq(promptMessage.client003.serial).find(".right").addClass("hidden");
					$(".state").eq(promptMessage.client003.serial).removeClass("state1 state2");
					$(".state").eq(promptMessage.client003.serial).addClass("state3");
					client003StateQueryOk = true;
					//设备断开连接界面处理
					deviceConnectionException();
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

	function sw3Control(){
		if ( canControl && receiveAssistLightSetting["bootMode"] === 3 ) {
			$("#sw3").removeClass("mui-disabled");
		} else {
			$("#sw3").addClass("mui-disabled");
		}
	}
	function sw3Operate(){
		layer.load(0);
		$.ajax({
			type: "POST",
			url: url + "/operat/peidianshi",
			dataType: "json",
			async: false,
			data: {_method:"PUT",dId:promptMessage.client003.dId,topic:promptMessage.client003.topic},
			xhrFields: {
				// 允许携带cookie跨域
				withCredentials: true
			},
			crossDomain: true,
			success: function (data) {
				layer.closeAll();
				if ( data.code === 201 ) {//
					if ( data.obj.state === 1 ) {
						$(".mui-switch").eq(promptMessage.client003.serial).addClass("mui-active");
						$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.on);
						$(".con").eq(promptMessage.client003.serial).parent().addClass("open");
					} else if ( data.obj.state === 2 ){
						$(".mui-switch").eq(promptMessage.client003.serial).removeClass("mui-active");
						$(".prompt").eq(promptMessage.client003.serial).text(promptMessage.client003.off);
						$(".con").eq(promptMessage.client003.serial).parent().removeClass("open");
					} else {
						layer.msg("状态未知,服务器异常,请联系管理员.",function(){})
					}
				}
			},
			timeout: 8000,//设置超时时间为8s
			error: function(e){
				layer.closeAll();
				layer.msg("辅助照明开关请求超时！")
			},
			complete : function(xhr, status) {
				myComplete(xhr, status);
			}
		})
	}

	
	$(document).ready(function(){

		//查询在线状态
		if (!debug){

		//定时获取状态信息
		var queryStateTime = setInterval( function(){
			if ( client001StateQueryOk ) queryClient001State();
			if ( client002StateQueryOk ) queryClient002State();
			if ( client003StateQueryOk ) queryClient003State();
		}, 1000 );
		
		}
		//感应灯操作监听
		assistFloodLightOpreate();
		
		//Debug
		// setInterval(function(){
		// 	testFn();
		// }, 1000);
		// $(".mui-switch").on("click",function(){
		// 	var index = $(".mui-switch").index(this);
		// 	if ($(this).hasClass("mui-active")){
		// 		console.log("第"+index+"个开关 执行打开")
		// 	} else {
		// 		console.log("第"+index+"个开关 执行关闭")
		// 	}
		// 	updatePromptBySwOnExecuting(index);
		// 	setTimeout(function() {
		// 		updatePromptBySw();
		// 	}, 3000);
		// })
		
		
		

		//监听返回
		$(".lnr-exit").on("click", function(){
			$.cookie('iot-lastPage', "", { expires: -1, path: '/' });
			if(debug) window.location.href = "index.html";
			if(!debug)
			window.location.href = url + "/index.html";
		})
		
		//监听操控解锁
		$(".control-lock").on("click", function(){
			if ( $(this).hasClass("lock") ) {//执行后解锁控制
				let auth = $.parseJSON(iotSettings).authArea.peidianshi;
				if ( auth === 1 ) {
					$(this).removeClass("lock");
					$(this).addClass("unlock");
					canControl = true;
					sw3Control();
				} else {
					queryUserSettings();
					auth = $.parseJSON(iotSettings).authArea.peidianshi;
					if ( auth === 1 ) {
						$(this).removeClass("lock");
						$(this).addClass("unlock");
						canControl = true;
						sw3Control();
					} else {
						layer.msg("无控制权限，请询问管理员开通！", function(){})
					}
				}
			} else {//执行后控制上锁
				$(this).addClass("lock");
				$(this).removeClass("unlock");
				canControl = false;
				sw3Control();
			}
		})

		//监听辅助照明灯控制按钮
		$("#sw3").on("click", function (){
			if ( !$("#sw3").hasClass("mui-disabled") ) {
				sw3Operate();
			}
		})
	});
})