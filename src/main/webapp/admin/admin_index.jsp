<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RIIZE store后台管理系统</title>
    <style>
        *{margin:0;padding:0;box-sizing: border-box;}
        body{
            background: linear-gradient(135deg, #fdfaf7 0%, #f9f4ef 100%);
            background-attachment: fixed;
            padding:50px 0;
            font-family: "Microsoft Yahei", "微软雅黑", sans-serif;
            position: relative;
        }
        /* ✅ 右上角退出按钮：和你提供的按钮样式完全一致 */
        .logout-btn {
            position: absolute;
            top: 30px;
            right: 5%;
            text-decoration: none;
            color: #fff;
            background: #6c757d; /* 匹配指定按钮的灰色 */
            font-size:16px;
            padding:9px 20px;
            border-radius:8px; /* 匹配指定按钮的圆角 */
            display: flex;
            align-items: center;
            gap: 6px;
            transition: all 0.2s ease;
        }
        .logout-btn:hover {
            background: #5a6268; /* hover加深灰色，和指定按钮一致 */
        }
        /* 标题美化：暖橙渐变+居中 */
        h2{
            background: linear-gradient(90deg, #ff8a50, #ff6b35);
            -webkit-background-clip: text;
            color: transparent;
            margin-bottom:40px;
            font-size:28px;
            font-weight:700;
            letter-spacing:2px;
            text-align: center;
        }
        /* 菜单主容器：纯白圆角卡片+轻阴影 */
        .menu {
            padding: 30px 20px;
            background: #ffffff;
            border-radius:20px;
            width:60%;
            margin:0 auto;
            box-shadow: 0 4px 15px rgba(0,0,0,0.03);
            border:1px solid #f3ebe6;
            text-align: center;
        }
        /* 功能菜单按钮：统一风格 */
        .menu a {
            margin: 0 25px;
            text-decoration: none;
            color: #555;
            font-size:18px;
            padding:10px 22px;
            border-radius:10px;
            transition: all 0.25s ease;
            font-weight:500;
        }
        .menu a:hover {
            color: #fff;
            background: linear-gradient(90deg, #ff8a50, #ff6b35);
            box-shadow: 0 2px 6px rgba(255,107,53,0.15);
        }
    </style>
</head>
<body>
<%-- ✅ 右上角退出按钮：和你提供的按钮样式完全一致 --%>
<a href="${pageContext.request.contextPath}/login_out.jsp" class="logout-btn">退出登录</a>

<div align="center">
    <h2>RIIZE store后台管理系统</h2>
    <div class="menu">
        <a href="${pageContext.request.contextPath}/goods?action=manage">商品管理</a>
        <a href="${pageContext.request.contextPath}/order?action=manage">订单管理</a>
        <a href="${pageContext.request.contextPath}/admin?action=userManage">用户管理</a>
        <a href="${pageContext.request.contextPath}/admin?action=dataStat">数据统计</a>
    </div>
</div>
</body>
</html>