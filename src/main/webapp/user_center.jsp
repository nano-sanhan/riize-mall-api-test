<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if(session.getAttribute("user") == null){
        response.sendRedirect(request.getContextPath()+"/login.jsp");
        return;
    }
%>
<html>
<head>
    <title>用户中心 - riize周边购物商城</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box;font-family:"Microsoft Yahei", "微软雅黑", sans-serif;}
        body{
            background: linear-gradient(120deg, #f9f5f1 0%, #fcf7f2 100%);
            background-attachment: fixed;
            padding: 50px 0;
        }
        .user-center {
            width: 650px;
            margin: 0 auto;
            padding: 45px 50px;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            border-radius: 15px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            position: relative;
        }
        .user-center::before{
            content: "";
            position: absolute;
            top:0;left:0;right:0;
            height: 4px;
            background: linear-gradient(90deg, #fa9941, #e77d23);
            border-top-left-radius: 15px;
            border-top-right-radius: 15px;
        }
        .user-center h2{
            text-align:center;
            background: linear-gradient(90deg, #e77d23, #fa9941);
            -webkit-background-clip: text;
            color: transparent;
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 35px;
            padding-bottom: 20px;
            border-bottom:1px solid #f5e6d8;
            letter-spacing: 3px;
            text-shadow: 0 2px 3px rgba(231,125,35,0.1);
        }
        .info-wrap {
            width: 100%;
        }
        .info-item {
            display: flex;
            align-items: center;
            margin-bottom: 28px;
            font-size: 16px;
            padding: 6px 0;
        }
        .info-item label{
            color:#887766;
            font-weight:600;
            width: 110px;
            text-align: right;
            padding-right: 25px;
            letter-spacing: 1px;
        }
        .info-item span{
            color:#333;
            font-weight: 500;
        }
        .info-item input{
            flex:1;
            height: 42px;
            padding: 0 15px;
            border: 1px solid #f0e0d5;
            border-radius: 8px;
            outline: none;
            font-size: 15px;
            color:#333;
            background: #fffbf7;
            transition: all 0.25s ease;
        }
        .info-item input:focus{
            border-color: #fa9941;
            box-shadow: 0 0 8px rgba(250, 153, 65, 0.25);
            background: #fff;
        }
        .info-item input:hover{
            border-color: #f8c4b8;
        }
        .phone-text{color: #e77d23 !important;font-weight: 500;}
        .address-text{color: #fa9941 !important;font-weight: 500;}
        .status-normal{color: #28a745; font-weight: 500;}
        .status-admin{color: #dc3545; font-weight: 500;}
        .time-text{color: #6c757d; font-size: 15px;}
        .edit-btn{
            display:block;
            width: 180px;
            height: 45px;
            line-height:45px;
            text-align:center;
            text-decoration:none;
            background: linear-gradient(90deg, #fa9941, #e77d23);
            color:#fff;
            border-radius: 8px;
            font-size:16px;
            margin:0 auto 20px;
            transition: all 0.2s;
            border:none;
            cursor: pointer;
            font-weight: 500;
            letter-spacing: 1px;
        }
        .edit-btn:hover{
            background: linear-gradient(90deg, #e77d23, #d46a10);
            box-shadow: 0 3px 8px rgba(231,125,35,0.2);
        }
        .success-tip{color:#28a745;text-align:center;margin:12px 0;height:22px;font-weight:500;font-size:15px;}
        .error-tip{color:#dc3545;text-align:center;margin:12px 0;height:22px;font-weight:500;font-size:15px;}
        /* ========== 核心优化：按钮横向均匀分布，不换行 ========== */
        .btn-group{
            display: flex;
            justify-content: space-between; /* 均匀分布 */
            align-items: center;
            padding-top: 28px;
            margin-top:10px;
            border-top:1px solid #f5e6d8;
        }
        .btn-group a{
            display:inline-block;
            text-decoration:none;
            padding: 10px 15px; /* 缩小内边距，适配宽度 */
            border-radius: 8px;
            font-size:14px; /* 微调字体大小 */
            transition: all 0.2s ease;
            color:#fff;
            font-weight: 500;
            letter-spacing: 1px;
            flex: 1; /* 自动平分宽度 */
            margin: 0 5px; /* 按钮间间距 */
            text-align: center;
        }
        .index-btn{background: linear-gradient(90deg, #6c757d, #5a6268);}
        .index-btn:hover{background: linear-gradient(90deg, #5a6268, #343a40); box-shadow:0 2px 6px rgba(108,117,125,0.2);}

        .cart-btn{background: linear-gradient(90deg, #fa9941, #e77d23);}
        .cart-btn:hover{background: linear-gradient(90deg, #e77d23, #d46a10); box-shadow:0 2px 6px rgba(231,125,35,0.2);}

        .order-btn{background: linear-gradient(90deg, #28a745, #20c997);}
        .order-btn:hover{background: linear-gradient(90deg, #20c997, #198754); box-shadow:0 2px 6px rgba(40,167,69,0.2);}

        .logout-btn{background: linear-gradient(90deg, #dc3545, #bb2d3b) !important;}
        .logout-btn:hover{background: linear-gradient(90deg, #bb2d3b, #99222c) !important; box-shadow:0 2px 6px rgba(220,53,69,0.2);}
    </style>
</head>
<body>
<div class="user-center">
    <h2>我的个人中心</h2>
    <form action="${pageContext.request.contextPath}/user?action=update" method="post" class="info-wrap">
        <input type="hidden" name="uid" value="${user.uid}">
        <div class="info-item"><label>用户名：</label><span>${user.username}</span></div>
        <div class="info-item">
            <label>账号状态：</label>
            <c:choose>
                <c:when test="${user.status == 1}"><span class="status-normal">✅ 正常使用</span></c:when>
                <c:when test="${user.status == 0}"><span class="status-disable">❌ 账号已禁用</span></c:when>
            </c:choose>
        </div>
        <div class="info-item">
            <label>手机号码：</label>
            <input type="tel" name="phone" class="phone-text" placeholder="请输入您的手机号码"
                   value="${user.phone == null || user.phone == '' ? '' : user.phone}">
        </div>
        <div class="info-item">
            <label>收货地址：</label>
            <input type="text" name="address" class="address-text" placeholder="请输入您的收货地址"
                   value="${user.address == null || user.address == '' ? '' : user.address}">
        </div>
        <div class="info-item">
            <label>账号类型：</label>
            <span>${user.type == 0 ? '普通用户' : '<span class="status-admin">✨ 管理员用户</span>'}</span>
        </div>
        <div class="info-item"><label>注册时间：</label><span class="time-text">${user.regTime == null ? '暂无数据' : user.regTime}</span></div>

        <div class="success-tip">${successMsg}</div>
        <div class="error-tip">${errorMsg}</div>

        <button type="submit" class="edit-btn">✨ 保存修改信息</button>
    </form>

    <div class="btn-group">
        <a href="${pageContext.request.contextPath}/goods?action=list" class="index-btn">📌 返回商城首页</a>
        <a href="${pageContext.request.contextPath}/cart?action=list" class="cart-btn">🛒 我的购物车</a>
        <a href="${pageContext.request.contextPath}/order?action=list" class="order-btn">📦 我的订单</a>
        <a href="${pageContext.request.contextPath}/login_out.jsp" class="logout-btn">🚪 退出登录</a>
    </div>
</div>
</body>
</html>