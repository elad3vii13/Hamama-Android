<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="css/common.css">
	<link rel="stylesheet" href="css/header.css">
	<title>Insert title here</title>
	<%@ include file="context.jsp" %>
</head>

<%
	// try to connect to the db if not connected
	if(ctx == null)
	{
		try
		{
			ctx = new Context(pageContext);
		}
		catch(Exception e)
		{
			out.write("<script charset=\"UTF-8\">");
			out.write("alert('db is not connected or not loaded yet.');");
			out.write("</script>");
		}
	}
%>

<body class="top-menu">
<ul>
  <li><a href="home.jsp">בית</a></li>
  <%
  if(ctx != null)
  {
 		if (!ctx.isLoggedIn()) {
			out.write("<li><a href='login.jsp'>כניסה</a></li>");
	 	}
	 	else {
			out.write("<li><a href='HttpHandler?cmd=logout'>יציאה</a></li>");
	 	}
  }
  %>
  <p style= "float: left; color: white; margin-left: 10px; margin-top: 10px">
	<% 
		if(ctx != null && ctx.isLoggedIn())
			out.write(ctx.getCurrentUserName());
		else
			out.write("שלום אורח"); 
	%>
  </p>
	<%
		if (ctx != null && ctx.isLoggedIn()){
			out.write("<li><a href='logs.jsp'>היסטוריה</a></li>");
		  	out.write("<li><a href='stats.jsp'>נתונים</a></li>");
	  		if(ctx.isManager()) {
	 			out.write("<li><a href='management.jsp'>ניהול</a></li>");
	  		}
		}%>
  <li><a href="about.jsp">אודות</a></li> 
</ul>
</body>
</html>