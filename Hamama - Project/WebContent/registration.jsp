<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Hamama</title>

    <style>
      body {
        overflow: hidden;
        transform: translate3d(0, 0, 0);
        background: linear-gradient(45deg, #49d49d 10%, #a2c7e5 90%);
        height: 100vh;
      }

      .form__input {
        display: block;
        width: 100%;
        padding: 20px;
        border: 0;
        outline: 0;
        transition: 0.3s;
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
    <%@ include file="header.jsp" %> <% if(!ctx.isLoggedIn() ||
    !ctx.isManager()) ctx.insertAlertDlg("You are not allowed to access this page, you are forwarded to the home page", "home.jsp"); String msg=null;
    String nickName = ctx.getFieldFromRequest("nickname"); String password =
    ctx.getFieldFromRequest("password"); if ((msg =
    (String)request.getAttribute("error"))!= null) { ctx.insertAlertDlg(msg,
    null); } %>

    <div style="max-width: 340px; margin: 10vh auto">
      <header style="text-align: center">
        <img
          src="https://cdn.iconscout.com/icon/free/png-512/leaf-444-675813.png"
          style="width: 100px; height: 100px"
        />
        <h1
          style="
            font-weight: 500;
            color: white;
            text-shadow: 0 1px 0 #ccc, 0 0 5px rgba(0, 0, 0, 0.1),
              0 1px 3px rgba(0, 0, 0, 0.3), 0 3px 5px rgba(0, 0, 0, 0.2),
              0 5px 10px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.2),
              0 20px 20px rgba(0, 0, 0, 0.15);
          "
        >
          ברוכים הבאים לדף הרישום של החממה
        </h1>
      </header>

      <form style="margin-top: 40px" ; method="post">
        <br />
        <label for="role">מנהל?</label>
        <input
          type="checkbox"
          id="role"
          name="role"
          value="manager"
        /><br /><br />

        <div class="form__group">
          <input
            type="text"
            name="nickname"
            id="nickname"
            placeholder="Username"
            class="form__input"
            style="width: 90%"
          />
        </div>

        <input
          style="width: 90%; margin-top: 1px"
          type="password"
          placeholder="Password"
          class="form__input"
          name="password"
          id="password"
        />
        <input
          style="width: 90%; margin-top: 1px"
          type="password"
          placeholder="Password"
          class="form__input"
          name="password"
          id="password2"
        />
        <button
          class="btn"
          type="submit"
          formaction="HttpHandler?cmd=register"
          onclick="return approve();"
          style="border-radius: 50px; margin-top: 30px; w"
        >
          Register
        </button>
      </form>
    </div>

    <script>
      function approve() {
        var pass1 = document.getElementById("password");
        var pass2 = document.getElementById("password2");

        if (pass1.value == pass2.value && pass1.value.length > 4) {
          window.alert("Thank you for registering");
          return true;
        } else {
          window.alert(
            "The passwords must be the same and longer than 4 characters!"
          );
          return false;
        }
      }
    </script>
  </body>
</html>
