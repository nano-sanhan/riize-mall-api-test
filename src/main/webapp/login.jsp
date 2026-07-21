<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box;font-family:"Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;}
        .login-box {
            width: 380px;
            margin: 80px auto;
            padding: 30px;
            border: 1px solid #f5e6d8;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(234, 125, 35, 0.06);
        }
        .login-box h3{
            text-align: center;
            color: #e77d23;
            font-size: 22px;
            margin-bottom: 25px;
            letter-spacing: 1px;
        }
        .login-box form div{
            margin-top: 18px;
            display: flex;
            align-items: center;
        }
        .login-box form div label{
            width: 90px;
            text-align: right;
            padding-right: 15px;
            color: #666;
            font-size: 15px;
        }
        .login-box form div input{
            flex:1;
            height: 38px;
            padding: 0 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            outline: none;
            font-size: 15px;
        }
        .login-box form div input:focus{
            border-color: #fa9941;
            box-shadow: 0 0 3px rgba(250, 153, 65, 0.3);
        }
        .btn-group{
            margin-top: 25px !important;
            justify-content: center;
        }
        .btn-group input{
            width: 100px !important;
            height: 40px !important;
            border: none;
            background: #fa9941;
            color: #fff;
            cursor: pointer;
            font-size: 15px;
            border-radius: 6px;
            transition: background 0.2s;
        }
        .btn-group input:hover{
            background: #e77d23;
        }
        .btn-group input[type="reset"]{
            background: #fff;
            color: #666;
            border: 1px solid #ddd;
            margin-left: 15px;
        }
        .btn-group input[type="reset"]:hover{
            border-color: #e77d23;
            color: #e77d23;
        }
        .error { color: red; margin-top: 15px; text-align: center; font-size: 14px; height: 18px; }
        .register-link { text-align: center; margin-top: 18px; font-size: 14px; color: #666;}
        .register-link a{
            color: #e77d23;
            text-decoration: none;
        }
        .register-link a:hover{
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-box">
    <h3>用户登录</h3>

    <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return trimUsername()">
        <div>
            <label>用户名：</label>
            <input type="text" id="username" name="username" required placeholder="请输入用户名">
        </div>
        <div>
            <label>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
            <input type="password" name="password" required placeholder="请输入密码">
        </div>
        <div class="btn-group">
            <input type="submit" value="登录">
            <input type="reset" value="重置">
        </div>
    </form>
    <div class="error">${msg}</div>
    <%session.removeAttribute("msg");%>
    <div class="register-link">
        还没账号？<a href="${pageContext.request.contextPath}/register.jsp">立即注册</a>
    </div>
</div>
<script>
    function trimUsername() {
        var usernameInput = document.getElementById('username');
        if (usernameInput) {
            usernameInput.value = usernameInput.value.trim();
        }
        // 密码自动去除前后空格
        var pwdInput = document.getElementsByName('password')[0];
        if (pwdInput) {
            pwdInput.value = pwdInput.value.trim();
        }
        return true;
    }
</script>
</body>
</html>