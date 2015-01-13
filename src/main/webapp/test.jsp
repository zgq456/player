<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<title>Fight English</title>
<style type="text/css">
.btnPlay {
	width: 30px;
	height: 30px;
	cursor: pointer;
	margin: 2px;
}

.btnSave {
	width: 30px;
	height: 30px;
	cursor: pointer;
	margin: 2px;
}

.btnDelete {
	width: 30px;
	height: 30px;
	cursor: pointer;
}

.btnEdit {
	width: 30px;
	height: 30px;
	cursor: pointer;
}

.textareaText {
	border: none;
	width: 100%;
	-webkit-box-sizing: border-box; /* <=iOS4, <= Android  2.3 */
	-moz-box-sizing: border-box; /* FF1+ */
	box-sizing: border-box; /* Chrome, IE8, Opera, Safari 5.1*/
}
</style>
</head>
<body>
	<div data-role="page" id="pageone">
		<div data-role="header">audio list</div>

		<div data-role="content">
			<div id="contentDiv"></div>
			<div id="uploadDiv">
				<!-- 				<table> -->
				<!-- 					<tr> -->
				<!-- 						<td> -->
				<!-- 							 File Name: -->
				<!-- 						</td> -->
				<!-- 						<td> -->
				<!-- 							 <input type="text" id="filename" name="filename" /> -->
				<!-- 						</td> -->
				<!-- 					</tr> -->
				<!-- 					<tr> -->
				<!-- 						<td> -->
				<!-- 							 File URL: -->
				<!-- 						</td> -->
				<!-- 						<td> -->
				<!-- 							 <input type="text" id="fileurl" name="fileurl" /> -->
				<!-- 						</td> -->
				<!-- 					</tr> -->
				<!-- 					<tr> -->
				<!-- 						<td colspan="2"> -->
				<!-- 							<input type="button" value="submit" onclick="addExtFile()">				 -->
				<!-- 						</td> -->
				<!-- 					</tr> -->
				<!-- 				</table> -->
				<form id="myform" name="myform" method="POST" enctype="multipart/form-data" action="play2.do" >
					<table>
						<tr>
							<td>File :</td>
							<td><input type="file" id="myfile" name="myfile" /></td>
						</tr>
						<tr>
							<td colspan="2"><input type="submit" value="submit" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div data-role="footer"></div>
	</div>
</body>
</html>
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css">
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>

<script>
	$(document)
			.ready(
					function() {
						$
								.ajax({
									type : "POST",
									url : "play.do",
									data : {
										action : 'getAudioList'
									},
									async : false,
									dataType : "json",
									success : function(data) {
										var htmlContent = "";
										for (var i = 0; i < data.length; i++) {
											htmlContent += "<div><a target='_blank' href='index.html?name="
													+ data[i].name
													+ "&source="
													+ (data[i].external ? data[i].url
															: data[i].name)
													+ "'>"
													+ data[i].name
													+ "</a></div>";
										}
										$("#contentDiv").html(htmlContent);
									}

								});
					});

	function addExtFile() {
		$.ajax({
			type : "POST",
			url : "play.do",
			data : {
				action : 'addExtFile',
				filename : $('#filename').val(),
				fileurl : $('#fileurl').val()
			},
			async : false,
			dataType : "text",
			success : function(data) {
				if (data == 1) {
					alert("success");
					location = location;
				} else {
					alert("failed");
				}
			}

		});
	}

	function uploadFile() {
		$.ajax({
			type : "POST",
			url : "play.do",
			data : {
				action : 'addExtFile',
				filename : $('#filename').val(),
				fileurl : $('#fileurl').val()
			},
			async : false,
			dataType : "text",
			success : function(data) {
				if (data == 1) {
					alert("success");
					location = location;
				} else {
					alert("failed");
				}
			}

		});
	}
</script>