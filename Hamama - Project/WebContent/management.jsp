<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.User"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hamama</title>

<style>
body {
	background: #06D5FA;
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
   margin-bottom: 100px;
   margin: 0 auto;
   align="center";
   outline: 10px black;
   border-collapse: collapse;
   width: 50%;
   height: 100%; 
 }


.table-header {
padding: 10px;
text-align:center;
	color: white;
	background:#000;
	padding:($half-spacing-unit * 1.5) 0;
}

.buttonClass{
	box-shadow: 0 5px 6px -4px rgba(0,0,0,0.22), 0 10px 13px -4px rgba(0,0,0,0.34), inset 0 30px 30px 0 rgba(255,255,255,0.35), inset 0 -30px 30px 0 rgba(0,0,0,0.10);
	margin-top: 15px;
	font: 30px;"
}

button {
  margin:10px auto;
  display:block;
  text-align: center;
  background: -webkit-linear-gradient(right, #a6f77b, #2dbd6e);
  border: none;
  border-radius: 21px;
  box-shadow: 0px 1px 8px #24c64f;
  cursor: pointer;
  color: white;
  font-family: "Raleway SemiBold", sans-serif;
  height: 42.3px;
  transition: 0.25s;
  width: 350px;
}
</style>

<script type="text/javascript" src="script.js"></script>
</head>
<body>
	<%@ include file="header.jsp" %>
	<% if(!ctx.isLoggedIn() || !ctx.isManager())
			ctx.insertAlertDlg("You are not allowed to access this page, you are forwarded to the home page", "home.jsp");%>
			
	<header style="text-align: center; margin-top: 30px; font-size: 60px; color: white; font-weight: bold; text-align: center; margin-left: 190px; margin-right: 190px; font-weight: bold; color: white; font-size: 70px; text-shadow: 0 1px 0 #ccc, 0 2px 0 #c9c9c9, 0 3px 0 #bbb, 0 4px 0 #b9b9b9, 0 5px 0 #aaa, 0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">תפריט מנהל</header>
	<button class="buttonClass" style="margin-top: 30px" onclick="location.href='registration.jsp'">רשום משתמש חדש</button>
	<button class="buttonClass" onclick="location.href='remove.jsp'">הסר משתמש רשום</button>
	<button class="buttonClass" onclick="createATable()">צפה במשתמשים הרשומים </button>
	<button class="buttonClass" onclick="getSensorList()">צפה ברשימת החיישנים </button>
	
	<div style="display:flex;justify-content:center;align-items:center; margin-top: 60px;">
		<table id="Board"></table>
	</div>
	
	<!-- SPACE AFTER THE TABLE --> 
	<div style="height: 50px;">
</body>
<script>

function createATable(){
	  <% List<User> users= ctx.getAllUseList();
	  int size = users.size();
	  	  
      String string_final = "<tr><th class=table-header>מזהה</th><th class=table-header>שם משתמש</th><th class=table-header>סיסמא</th><th class=table-header>תפקיד</th><th class=table-header>שעת התחברות אחרונה</th></tr>";
      for (int i = 0; i < size; i++) {
	   	   string_final += "<tr>";
	       string_final += "<td>" + users.get(i).getId() + "</td>";
	       string_final += "<td>" + users.get(i).getNickName() + "</td>";
	       string_final += "<td>" + users.get(i).getPassword() + "</td>";
	       string_final += "<td>" + users.get(i).getRole() + "</td>";
	       
		    // convert seconds to milliseconds
		    Date date = new java.util.Date(users.get(i).getLastLogin()); 
		    // the format of your date
		    SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss dd-MM-yyyy"); 
		    // give a timezone reference for formatting (see comment at the bottom)
		    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
		    String formattedDate = sdf.format(date);
	       
	       string_final += "<td>" + formattedDate + "</td>";
		   string_final += "</tr>";
      } %>
      document.getElementById("Board").innerHTML = '<%=string_final %>';
}
</script>
</html>