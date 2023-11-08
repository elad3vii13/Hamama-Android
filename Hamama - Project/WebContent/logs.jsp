<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hamama</title>
<style>

.upperDiv {
	margin-top: 10px;
  justify-content: space-between;   
  flex-direction: column;
}

body {
    background-size: 200% 100% !important;
    animation: move 10s ease infinite;
    transform: translate3d(0, 0, 0);
    background: linear-gradient(45deg, #06D5FA 10%, #A2C7E5 90%);
    height: 100vh
}

select {
    font-size: .9rem;
    padding: 5px 15px;
}

.table-header {
	padding: 10px;
	text-align:center;
	color: white;
	background:#000;
}

th,td {
	background-color: white;
	padding: 20px;
    border: 1px solid black;
    text-align: center;
}

 table {
   box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.5);
   margin-top: 100px;
   margin: 0 auto;
   align="center";
   outline: 10px black;
   border-collapse: collapse;
   width: 50%;
   height: 100%; 
 }


.button-class {
  background: -webkit-linear-gradient(right, #a6f77b, #2dbd6e);
  border: none;
  border-radius: 21px;
  box-shadow: 0px 1px 8px #24c64f;
  cursor: pointer;
  color: white;
  height: 42.3px;
  margin: 0 auto;
  margin-top: 50px;
  transition: 0.25s;
  width: 350px;
}

button{
	font-size: 16px;
	font-weight: bold;
	padding: 10px;
	border-radius:28px;
	border:1px solid #000;
	display:inline-block;
	color: white;
	background:#000;
}

</style>
<script type="text/javascript" src="script.js"></script>
</head>
<body onload="getUpdatedSensorList()">
<%@ include file="header.jsp" %>
<% if(!ctx.isLoggedIn())
		ctx.insertAlertDlg("You are not allowed to access this page, you are forwarded to the home page", "home.jsp");%>
		
<div class="upperDiv" style="position: relative; height: 40%; width: 95%; border-radius: 20px; background-color: white; margin: 20px auto; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">

	<div style="margin-top: 30px;">
		<!-- FROM DATE -->
		<header style="margin-right: 30px; padding-top: 25px">מתאריך:</header>
		<input type="datetime-local" id="fromValue" name="from" value="2021-01-1T19:30" style="width: 30%; margin-right: 30px"></input>
	</div>
	      
	 <div>
		<!-- TO DATE --> 
		<header style="margin-right: 30px;">עד תאריך:</header>
		<input type="datetime-local" id="toValue" name="to" value="2021-01-1T19:30" style="width: 30%; margin-right: 30px"></input>
	</div>
	
	<form>
		<select name="sensor" id="sensor" style="width: 30%; margin-right: 30px; margin-top: 10px" dir="rtl"> 
		    <option value="-1">כללי</option>
		    <option value="0">הכל</option>
		</select>
	</form>
	
	<select name="priority" id="priority" style="width: 30%; margin-right: 30px; margin-top: 10px" dir="rtl"> 
	    <option value="0">כל העדיפויות</option>
	    <option value="error">תקלה</option>
	    <option value="warning">אזהרה</option>
	    <option value="info">ידיעה</option>
	</select>
	
	<div style="position: absolute; bottom: 0; margin-bottom: 10px; right: 50%;">
		<button class="button-class" style="margin-bottom: 10px;" onclick="getHistory()">הוסף טבלה</button>
	</div>
</div>

<div style="display:flex;justify-content:center;align-items:center; margin-top: 60px;">
	<table id="Board"></table>
</div>

<!-- SPACE AFTER THE TABLE --> 
<div style="height: 50px;">
</div>
</body>
</html>