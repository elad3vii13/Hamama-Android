<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>about</title>
    <link rel="stylesheet" href="css/globalClasses.css" />
  </head>

  <style>
    body {
      clear: both;
    }

    header {
      font-weight: bold;
      color: white;
      text-shadow: 0 1px 0 #ccc, 0 2px 0 #c9c9c9, 0 3px 0 #bbb, 0 4px 0 #b9b9b9,
        0 5px 0 #aaa, 0 6px 1px rgba(0, 0, 0, 0.1), 0 0 5px rgba(0, 0, 0, 0.1),
        0 1px 3px rgba(0, 0, 0, 0.3), 0 3px 5px rgba(0, 0, 0, 0.2),
        0 5px 10px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.2),
        0 20px 20px rgba(0, 0, 0, 0.15);
    }
  </style>

  <body>
    <%@ include file="header.jsp" %>
    <div>
      <header
        style="
          margin-top: 30px;
          text-align: center;
          margin-left: 150px;
          margin-right: 150px;
          font-size: 100px;
        "
      >
        אודות
      </header>

      <p
        style="
          margin-left: 200px;
          margin-right: 200px;
          color: white;
          font-size: 50px;
          text-shadow: 1px 2px 1px #000000;
          text-align: center;
        "
      >
        אתר האינטרנט של החממה החכמה פותח על מנת לאפשר גישה נוחה לנתונים שמתקבלים
        מהחממה והצגתם בצורה ידידותית למשתמש. אתר האינטרנט של החממה פותח בשנת
        הלימודים תשפ"א (2021) על ידי אלעד לוי בהדרכת דניאל בואנוס.
      </p>
    </div>
  </body>
</html>
