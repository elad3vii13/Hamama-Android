<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="css/globalClasses.css" />
    <style>
      body {
        overflow: hidden;
        clear: both;
      }

      header {
        font-weight: bold;
        color: white;
        text-shadow: 0 1px 0 #ccc, 0 2px 0 #c9c9c9, 0 3px 0 #bbb,
          0 4px 0 #b9b9b9, 0 5px 0 #aaa, 0 6px 1px rgba(0, 0, 0, 0.1),
          0 0 5px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.3),
          0 3px 5px rgba(0, 0, 0, 0.2), 0 5px 10px rgba(0, 0, 0, 0.25),
          0 10px 10px rgba(0, 0, 0, 0.2), 0 20px 20px rgba(0, 0, 0, 0.15);
      }
    </style>
    <meta charset="UTF-8" />
    <title>Hamama</title>
  </head>
  <body>
    <%@ include file="header.jsp" %>

    <img style="float: left; width: 30%; height: 100%" src="images/bg1.jpg" />
    <div style="float: right; width: 70%">
      <header
        style="
          margin-top: 30px;
          text-align: center;
          margin-left: 150px;
          margin-right: 150px;
          font-size: 70px;
        "
      >
        ברוכים הבאים למסך הבית של אתר החממה
      </header>

      <p
        style="
          margin: 70px;
          color: white;
          font-size: 40px;
          text-shadow: 1px 2px 1px #000000;
        "
      >
        באתר האינטרנט של החממה ניתן לצפות בנתוני מדידות החיישנים שנמצאים בחממה
        בצורת גרף קווי. בנוסף ניתן לראות את היסטוריית התקלות שנמצאו בחיישנים
        באמצעות הגדרת טווח תאריכים
      </p>
    </div>
  </body>
</html>
