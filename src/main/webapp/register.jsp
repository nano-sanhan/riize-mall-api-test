<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box;font-family:"Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;}
        .register-box {
            width: 380px;
            margin: 80px auto;
            padding: 30px;
            border: 1px solid #f5e6d8;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(234, 125, 35, 0.06);
        }
        .register-box h3{
            text-align: center;
            color: #e77d23;
            font-size: 22px;
            margin-bottom: 25px;
            letter-spacing: 1px;
        }
        .form-item {
            margin-top: 18px;
            display: flex;
            align-items: center;
        }
        .form-item label {
            width: 90px;
            text-align: right;
            padding-right: 15px;
            color: #666;
            font-size: 15px;
            white-space: nowrap;
        }
        .form-item input {
            flex: 1;
            height: 38px;
            padding: 0 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            outline: none;
            font-size: 15px;
        }
        .form-item input:focus{
            border-color: #fa9941;
        }

        /* 核心：限制验证码行不超出容器 */
        .code-group {
            flex: 1;
            display: flex;
            gap: 8px;
            align-items: center;
            max-width: calc(100% - 90px); /* 限制最大宽度 = 容器宽度 - 标签宽度 */
        }
        .code-group input {
            flex: 1;
            min-width: 0; /* 允许输入框收缩，避免撑破容器 */
        }
        .code-btn {
            flex: 0 0 110px; /* 固定按钮宽度，不拉伸 */
            height: 38px;
            background: #fa9941;
            color: #fff;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            white-space: nowrap;
        }
        .code-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .btn-group{
            margin-top: 25px;
            display: flex;
            justify-content: flex-start;
            gap:15px;
        }
        .btn-group input{
            width: 160px;
            height: 40px;
            border: none;
            background: #fa9941;
            color: #fff;
            cursor: pointer;
            border-radius: 6px;
        }
        .btn-group input[type="reset"]{
            width: 120px;
            background: #fff;
            color: #666;
            border: 1px solid #ddd;
        }
        .error { color: red; margin-top: 15px; text-align: center; font-size:14px; }
        .login-link { text-align: center; margin-top: 25px; font-size:14px; }
        .login-link a{ color: #e77d23; text-decoration: none; }
    </style>
</head>
<body>
<div class="register-box">
    <h3>用户注册</h3>
    <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return trimUsername()">

        <div class="form-item">
            <label>用户名：</label>
            <input type="text" id="username" name="username" required placeholder="请输入用户名">
        </div>

        <div class="form-item">
            <label>密 码：</label>
            <input type="password" name="password" required placeholder="请输入密码">
        </div>

        <div class="form-item">
            <label>手机号：</label>
            <input type="tel" id="phone" name="phone" required placeholder="11位手机号" pattern="^1[3-9]\d{9}$">
        </div>

        <div class="form-item">
            <label>验证码：</label>
            <div class="code-group">
                <input type="text" name="code" required placeholder="请输入验证码">
                <button type="button" class="code-btn" id="getCodeBtn" onclick="sendCode()">获取验证码</button>
            </div>
        </div>

        <div class="btn-group">
            <input type="submit" value="注册">
            <input type="reset" value="重置">
        </div>
    </form>
    <div class="error">${msg}</div>
    <div class="login-link">
        已有账号？<a href="${pageContext.request.contextPath}/login.jsp">立即登录</a>
    </div>
</div>

<script>
    function trimUsername() {
        var u = document.getElementById('username');
        if(u) u.value = u.value.trim();
        // 【新增】密码去前后空格（企业标准做法）
        var pwd = document.getElementsByName('password')[0];
        if(pwd) pwd.value = pwd.value.trim();
        return true;
    }

    function sendCode() {
        let phone = document.getElementById('phone').value.trim();
        let btn = document.getElementById('getCodeBtn');
        if(!/^1[3-9]\d{9}$/.test(phone)){
            alert("请输入正确手机号");
            return;
        }
        btn.disabled = true;
        let s = 60;
        let timer = setInterval(()=>{
            s--;
            btn.innerText = s+"秒重发";
            if(s<=0){
                clearInterval(timer);
                btn.disabled=false;
                btn.innerText="获取验证码";
            }
        },1000);

        fetch("${pageContext.request.contextPath}/sendCode?phone="+phone)
            .then(res=>res.text())
            .then(data=>{
                if(data==="exist"){
                    alert("该手机号已注册");
                    clearInterval(timer);
                    btn.disabled=false;
                    btn.innerText="获取验证码";
                }else{
                    alert("验证码："+data);
                }
            });
    }
</script>
</body>
</html>