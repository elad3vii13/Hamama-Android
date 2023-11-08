<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Hamama</title>
    <link rel="stylesheet" href="css/globalClasses.css" />

    <style>
      .user__title {
        text-shadow: 0 1px 0 #ccc, 0 0 5px rgba(0, 0, 0, 0.1),
          0 1px 3px rgba(0, 0, 0, 0.3), 0 3px 5px rgba(0, 0, 0, 0.2),
          0 5px 10px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.2),
          0 20px 20px rgba(0, 0, 0, 0.15);
        font-weight: 500;
        color: white;
      }

      .form__input {
        display: block;
        width: 90%;
        padding: 20px;
        border: 0;
        outline: 0;
      }

      .btn {
        display: block;
        width: 100%;
        padding: 20px;
        outline: 0;
        border: 0;
        color: white;
        background: #000000;
        transition: 0.3s;
      }
    </style>
  </head>
  <body>
    <%@ include file="header.jsp" %> <% String msg=null; String nickName =
    ctx.getFieldFromRequest("nickname"); String password =
    ctx.getFieldFromRequest("password"); if ((msg =
    (String)request.getAttribute("error"))!= null) { ctx.insertAlertDlg(msg,
    null); } %>

    <div style="max-width: 350px; margin: 10vh auto">
      <header style="text-align: center">
        <img
          src="https://cdn.iconscout.com/icon/free/png-512/leaf-444-675813.png"
          style="width: 100px; height: 100px"
        />
        <h1 class="user__title">ברוכים הבאים לדף הרישום של החממה</h1>
      </header>

      <form method="post" style="margin-top: 40px">
        <br />
        <div>
          <input
            type="text"
            name="nickname"
            id="nickname"
            placeholder="Username"
            class="form__input"
          />
        </div>
        <div>
          <input
            type="password"
            name="password"
            id="password"
            placeholder="Password"
            class="form__input"
            style="margin-top: 1px"
          />
        </div>
        <button
          class="btn"
          type="submit"
          formaction="HttpHandler?cmd=login"
          style="margin-top: 50px; border-radius: 50px"
        >
          Login
        </button>
      </form>
    </div>
  </body>
</html>
