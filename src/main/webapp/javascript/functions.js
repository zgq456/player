var currAudio;
var duration = 30;
var timer ;
var initTimer ;
var currentAudioSrc = "";
var audioName = "";
function Init() {
	$.ajax({
		type : "POST",
		url : "play.do",
		data : {
			action : 'getLrcList',
			name : audioName
		},
		async:false,
		dataType : "json",
		success : function(data) {
//			alert(data);
			for(var i = 0; i < data.length; i++) {
				addDetail(data[i].start, data[i].end, data[i].text, data[i].lastUpt, null, true);
			}
		}

	});
	
	var startVar = 0;
	var endVar = 0;
	if ($("#tblData tr").length == 1) {
		startVar = 0;
		endVar = duration;
		addDetail(startVar, endVar, null, null, null, false);
	} 
}

//<img src='images/delete.png' class='btnDelete'/>
function addDetail(startVar, endVar, text, lastUpt, baseRow, saved) {
	var baseObj;
	if(typeof (text) == "undefined" || text == null) {
		text = "";
	}
	var trInfo = "<tr>"
			+ "<td><span name='numSpan'>1</span></td>"
			+ "<td>"
			+ conversionTime(startVar)
			+ "</td>"
			+ "<td><span>"
			+ conversionTime(endVar)
			+ "</span><div><input type='button' value='split'  onclick='mysplit(this)' /></div></td>"
			+ "<td><textarea class='textareaText' rows='5' cols='10' onkeyup='taChange(this)'>" + text + "</textarea></td>"
			+ "<td><img src='images/play.JPG' class='btnPlay'  onclick='playSeg(this)'><img src='images/save" + (saved ? "d" : "") + ".png' class='btnSave' onclick='Save(this)'>" 
			+ "<input type='text' name='lastUpt' readonly='true' value='" + lastUpt + "'"
			+ "</td>"
			+ "</tr>";
	if (typeof (baseRow) == "undefined" || baseRow == null) {
		baseObj = $("#tblData tbody");
		baseObj.append(trInfo);
	} else {
		baseObj = baseRow;
		baseObj.after(trInfo);
	}

	// $(".btnSave").bind("click", Save);
	// $(".btnDelete").bind("click", Delete);
//	var numSpans = document.getElementsByName("numSpan");
//	alert("numSpans.length:" + $('[name="numSpan"]').length);
//	for(var i = 0; i < numSpans.length; i++) {
//		alert(i);
//		$(numSpans[i]).html(i + 1);
//	}
	
	$.each( $('[name="numSpan"]'), function( key, value ) {
//		alert( key + ": " + value );
		$(value).html(key + 1);
	});

}

$('.textareaText').bind('input propertychange', function() {
	taChange(this);
});

function taChange(taObj) {
//	alert($(taObj).val());
	var saveBtn = $(taObj).parent().next().children(".btnSave");
	saveBtn.attr("src", "images/save.png");
}

var lastPlayBtn = null;
var ix = 0;
function playSeg(btn) {
	if(lastPlayBtn != null && lastPlayBtn != btn) {
//		$(".btnPlay").attr("src", "images/play.JPG");
		$(lastPlayBtn).attr("src", "images/play.JPG");
	}
	//var currBtnSrc = $(btn).attr("src");
	//$(btn).attr("src", currBtnSrc);
	clearInterval(timer);
	currAudio.pause();

//	alert("here1");
	var start;
	var end;
	// alert("start:" + start + " end:" + end + " currentTime:" +
	// currAudio.currentTime);
	var startTd = $(btn).parent().prev().prev().prev();
	var endTd = $(btn).parent().prev().prev();
	var thisTr = startTd.parent().parent();

	start = getSeconds(startTd.html());
	end = getSeconds(endTd.text());

	if ($(btn).attr("src") == "images/play.JPG") {
//		console.log("here22");
		//alert("here2");
		setTimeout(function () { 
	        $(btn).attr("src", "images/pause.jpg");
	    }, 100);
		currAudio.currentTime = start;
		currAudio.play();
		
		timer = setInterval(function() {
			if (currAudio.currentTime > end) {
				currAudio.pause();
				$(btn).attr("src", "images/play.JPG");
				clearInterval(timer);
			} else {
//				$(btn).attr("src", "images/pause.jpg");
			}
		}, 1000);
	} else {
//		console.log("here3");
		//alert("here3");
		$(btn).attr("src", "images/play.JPG");
		currAudio.pause();
	}

	lastPlayBtn = btn;
}

function Save(btn) {
	var lastUpt = $(btn).next().val();
//	console.log("lastUpt:" + lastUpt);
//	alert("save btn");
	var start;
	var end;
	var startTd = $(btn).parent().prev().prev().prev();
	var endTd = $(btn).parent().prev().prev();
	var textTd = $(btn).parent().prev();
	var thisTr = startTd.parent().parent();
	
	start = startTd.html();
	end = endTd.text();
	$.ajax({
		type : "POST",
		url : "play.do",
		data : {
			action : 'add',
			name : audioName,
			start : start,
			end : end,
			text : textTd.children(0).val(),
			lastUpt : lastUpt
		},
		dataType : "json",
		success : function(data) {
//			alert(data);
			if(data[0] == "0") {
				$(btn).attr("src", "images/saved.png");
				$(btn).next().val(data[1]);
			} else {
				alert("your this row data is dirty, please backup data, refresh page, and try to save again");
			}
		}

	});
}

function Delete() {
	var par = $(this).parent().parent(); // tr
	par.remove();
}

function mysplit(btn) {
	var start;
	var end;
	var endStr;
	var preSpan = $(btn).parent().prev();
	var thisTr = preSpan.parent().parent();

	start = getSeconds($(btn).parent().parent().prev().html());
	end = getSeconds(preSpan.html());
//	alert("start:" + start + " end:" + end);
	if (currAudio.currentTime > start
			&& currAudio.currentTime < end) {
		var saveBtn = $(btn).parent().parent().next().next().children(".btnSave");
		var playBtn = $(btn).parent().parent().next().next().children(".btnPlay");
		saveBtn.attr("src", "images/save.png");
		playBtn.attr("src", "images/play.JPG");
		
		preSpan.html(conversionTime(parseInt(currAudio.currentTime)));
		
		addDetail(parseInt(currAudio.currentTime), end, "", "", thisTr);
		
		saveBtn.click();
		
		$(btn).parent().parent().parent().next().children("td:last-child").children(".btnSave").click();
	}
}

function back() {
	currAudio.currentTime -= 3;
}
// 歌词时间增加2s
function forward() {
	currAudio.currentTime += 3;
}

function conversionTime(time){
    var surplusHour,
    	surplusMinite,
        surplusSecond,
        cTime;
    //将剩余秒数转化为分钟
    surplusHour = Math.floor(time / 3600);
    //将剩余秒数转化为分钟
    surplusMinite = Math.floor((time / 60) % 60);
    //将剩余秒数转化为秒钟
    surplusSecond = Math.floor(time % 60);
    if(surplusSecond < 10){
        surplusSecond = "0" + surplusSecond;
    }
    if(surplusMinite < 10){
    	surplusMinite = "0" + surplusMinite;
    }
    if(surplusHour < 10){
    	surplusHour = "0" + surplusHour;
    }
    cTime = surplusMinite + ":" + surplusSecond;
    if(surplusHour != 0) {
    	cTime = surplusHour + ":" + cTime;
    }
    return cTime;
}

function getSeconds(timeStr) {
	var timeStrArr = timeStr.split(":");
	var timeLong = 0;
	timeLong += (parseInt(timeStrArr[timeStrArr.length-1]) + parseInt(timeStrArr[timeStrArr.length-2] * 60));  
	if(timeStrArr.length == 3) {
		timeLong += parseInt(timeStrArr[0] * 3600);  
	}
	//alert("timeStr:" + timeStr + " timeLong:" + timeLong);
	return timeLong;
}

/*
*函数功能：从href获得参数
*sHref:   http://www.artfh.com/arg.htm?arg1=d&arg2=re
*sArgName:arg1, arg2
*return:    the value of arg. d, re
*/
function GetArgsFromHref(sHref, sArgName)
{
      var args    = sHref.split("?");
      var retval = "";
    
      if(args[0] == sHref) /*参数为空*/
      {
           return retval; /*无需做任何处理*/
      }  
      var str = args[1];
      args = str.split("&");
      for(var i = 0; i < args.length; i ++)
      {
          str = args[i];
          var arg = str.split("=");
          if(arg.length <= 1) continue;
          if(arg[0] == sArgName) retval = arg[1]; 
      }
      return retval;
}

function fetchRawText() {
	$("#rawTa").val("");
	$.ajax({
		type : "POST",
		url : "play.do",
		data : {
			action : 'fetchRawMsg',
			name : audioName
		},
		dataType : "text",
		success : function(data) {
			$("#rawTa").val(data);
		}
	});
}

function downloadRawText() {
	window.location = "play.do?action=downloadLrc&name=" + audioName; 
}

function saveRawText() {
	$.ajax({
		type : "POST",
		url : "play.do",
		data : {
			action : 'saveRawMsg',
			name : audioName,
			content : $("#rawTa").val()
		},
		dataType : "text",
		success : function(data) {
			alert(data);
			window.location = window.location; 
		}
	});
}

function doSearch() {
	var fromDate = $("#from").val() + " 00:00:00";
	var toDate = $("#to").val() + " 23:59:59";
//	console.log("fromDate:" + fromDate + " toDate:" + toDate);
	
	$.each( $("#tblData tr"), function( key, value ) {
//		console.log( key + ": " + value );
		if(key != "0") {
			var currUptDate = $(value).children().eq(4).children().eq(2).val();
			if(currUptDate >= fromDate && currUptDate <= toDate) {
				$(value).show();
			} else {
				$(value).hide();
			}
		}
//		$(value).html(key + 1);
	});
}


$(document).ready(function() {
	currAudio = document.getElementById("currAudio");
	
//	alert("window.location.href:" + window.location.href);
	//alert("source:" + GetArgsFromHref(window.location.href, "source"));
	audioName = GetArgsFromHref(window.location.href, "name");
	$("#filenameSpan").html(audioName);
	var audioSource = GetArgsFromHref(window.location.href, "source");
	if(audioSource == "") {
		audioSource = "audio/demo.mp3";
	} else if(audioSource.toLowerCase().indexOf("http://") >= 0) {
//		audioSource = audioSource;
	} else {
		audioSource = "audio/" + audioSource;
	}
	
//	alert("audioSource:" + audioSource );
	$("#audioSource").attr("src", audioSource).detach().appendTo("#currAudio");
	currentAudioSrc = audioSource;
//	currAudio.load();

	initTimer = setInterval(function() {
//		alert("here:" + currAudio.duration + "  isNaN:" + !isNaN(currAudio.duration));
		if (!isNaN(currAudio.duration)) {
			duration = currAudio.duration;
			$("#totSec").html(duration);
			clearInterval(initTimer);
			Init();
		}
	}, 1000);
	
	
	$('#currAudio').bind('pause', function () { 
		$(".btnPlay").attr("src", "images/play.JPG");
	});
	
	
	 $( "#from" ).datepicker({
		 defaultDate: "+1w",
		 changeMonth: true,
		 numberOfMonths: 1,
		 dateFormat : "yy-mm-dd",
		 onClose: function( selectedDate ) {
		 $( "#to" ).datepicker( "option", "minDate", selectedDate );
		 }
	 });
	 $( "#to" ).datepicker({
		 defaultDate: "+1w",
		 changeMonth: true,
		 dateFormat : "yy-mm-dd",
		 numberOfMonths: 1,
		 onClose: function( selectedDate ) {
		 $( "#from" ).datepicker( "option", "maxDate", selectedDate );
		 }
	 });
	
	
});
