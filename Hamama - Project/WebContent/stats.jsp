<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Hamama</title>
		<link rel="stylesheet" href="css/globalClasses.css">
		<script type="text/javascript" src="script.js"></script>
	</head>
	
	<body onload="getUpdatedSensorList()">
	<%@ include file="header.jsp" %>
	<% 
		if(!ctx.isLoggedIn()){
			ctx.insertAlertDlg("You are not allowed to access this page, you are forwarded to the home page", "home.jsp");
		} %>
	
	<!--  position: relative - the element is positioned relative to its normal position -->
	<div style="position: relative; height: 30%; width: 95%; border-radius: 20px; background-color: white; margin: 20px auto; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">
		<div>
			<!-- FROM DATE -->
			<header style="padding-top: 10px; padding-right: 30px;">מתאריך:</header>
			<input type="datetime-local" id="fromValue" name="from" value="2021-01-01T19:30" style="width: 30%; margin-right: 30px"></input>
			<!-- TO DATE --> 
			<header style="padding-top: 10px; padding-right: 30px;">עד תאריך:</header>
			<input type="datetime-local" id="toValue" name="to" value="2021-01-01T19:30" style="width: 30%; margin-right: 30px"></input>
		</div>
		
		<form>
			<select name="sensor" id="sensor" style="width: 30%; margin-right: 30px; margin-top: 10px;" dir="rtl" multiple>
			</select>
		</form>
		
		<!--  With absolute positioning, an element can be placed anywhere on a page -->
		<div style="position: absolute; bottom: 0; margin-bottom: 10px; right: 50%;">
			<button class="buttonStyle" style="align-items: center;  padding-bottom: 10px; justify-content: center;" onclick="addGraph()">הוסף גרף</button>
		</div>
	</div>
	 
	<div style="height: 56%; width: 95%; border-radius: 20px; background-color: white; margin: auto; position: fixed; left: 50%; transform: translateX(-50%); box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">
	<div id="chartContainer" style="margin:30px; position: relative; top: 10%;"></div>
	<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
	</div>
	</body>
</html>
