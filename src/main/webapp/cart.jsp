<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>我的购物车</title>
    <style>
        /* 全局样式重置+统一字体 - 背景色和全站一致 浅暖米色 */
        *{margin:0;padding:0;box-sizing: border-box;font-family: "Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;padding-top: 30px;}

        /* ✅ 新增：库存不足提示框样式 橙色主题 醒目不刺眼 */
        .stock-error {
            width: 88%;
            margin:0 auto 20px auto;
            padding:15px;
            background: #fff8f5;
            border:1px solid #ffe6d8;
            border-radius:6px;
            color: #dc3545;
            font-size:15px;
            line-height: 1.8;
        }

        /* 头部标题+返回首页按钮 组合栏 - 橙色主题 与个人中心一致 布局微调更协调 */
        .top-box{width: 88%;margin:0 auto;display: flex;justify-content: space-between;align-items: center;margin-bottom: 30px;}
        .top-box h2{color: #e77d23;font-size:24px;margin:0;font-weight: 600;}
        .back-btn{text-decoration: none;padding:9px 18px;background: #fa9941;color:#fff;border-radius:6px;}
        .back-btn:hover{background: #e77d23;}

        /* 购物车主表格 - 柔和风格 圆角+轻阴影 配色协调 保留原布局和交互 */
        table { width: 88%; margin: 0 auto; border-collapse: collapse; background: #fff; border-radius:8px;overflow: hidden;box-shadow: 0 2px 10px rgba(234, 125, 35, 0.05);border:1px solid #f5e6d8;}
        th, td { border: 1px solid #f5e6d8; padding: 14px; text-align: center; }
        th { background: #fff8f5; color:#666;font-weight: 500;}
        tr:hover{background-color: #fff8f5;}

        /* 金额样式 - 橙色主色调 与全站一致 醒目美观 */
        td{color:#555;font-size:15px;}
        .price{color: #e77d23; font-weight: bold;font-size:15px;}

        /* 删除按钮 - 橙色系红色 柔和不刺眼 保留原交互逻辑 */
        .delete { color: #e77d23; text-decoration: none; padding:5px 10px;border-radius:6px;border:1px solid #ffe6d8;}
        .delete:hover { color: #fff; background: #e77d23;}

        /* 总价区域 - 橙色高亮总价 字体协调 布局对齐表格 */
        .total { width: 88%; margin:25px auto 20px; text-align: right; font-size: 20px; font-weight: bold; color:#333;padding-right: 10px;}
        .total span{color: #e77d23;padding-left:6px;}

        /* 提交订单表单 - 橙色主题按钮 输入框美化 保留原必填校验+布局+功能 */
        .form-box{width: 88%;margin:0 auto;text-align: right;padding-bottom: 60px;}
        .form-box div{margin-bottom:20px;display: flex;justify-content: flex-end;align-items: center;}
        .form-box label{font-size:16px;color:#666;margin-right:12px;width: 80px;text-align: right;}
        .form-box input[type="text"]{width: 360px; padding:11px; border:1px solid #ddd; border-radius:6px;outline:none;}
        .form-box input[type="text"]:focus{border-color: #fa9941;box-shadow: 0 0 5px rgba(250,153,69,0.2);}
        .submit-btn { padding: 11px 30px; background: #fa9941; color: #fff; border: none; border-radius:6px; cursor: pointer; font-size:16px;}
        .submit-btn:hover { background: #e77d23;}

        /* 购物车为空提示 - 柔和卡片风格 橙色点缀 与全站一致 */
        .empty-cart{width: 88%;margin:0 auto;padding:90px 0;background:#fff;text-align:center;border-radius:8px;box-shadow: 0 2px 10px rgba(234, 125, 35, 0.05);color:#666;font-size:18px;border:1px solid #f5e6d8;}
    </style>
</head>
<body>
<%-- 头部：标题 + 返回首页按钮 --%>
<div class="top-box">
    <h2>我的购物车</h2>
    <a class="back-btn" href="${pageContext.request.contextPath}/goods?action=list">&nbsp;返回首页&nbsp;</a>
</div>

<%-- ✅ 新增：库存不足提示框，有错误信息时显示 --%>
<c:if test="${not empty stockError}">
    <div class="stock-error" dangerouslySetInnerHTML="${stockError}">${stockError}</div>
</c:if>

<c:choose>
    <%-- 购物车为空时显示提示 内容不变 --%>
    <c:when test="${empty cartList}">
        <div class="empty-cart">您的购物车空空如也，快去选购商品吧~</div>
    </c:when>

    <%-- 购物车有商品时展示列表 所有功能/取值/逻辑/删除交互 完全不变 --%>
    <c:otherwise>
        <table>
            <tr>
                <th>商品名称</th>
                <th>单价</th>
                <th>购买数量</th>
                <th>商品小计</th>
                <th>操作</th>
            </tr>
            <c:set var="total" value="0" />
            <c:forEach items="${cartList}" var="cart">
                <tr>
                    <td>${cart.goodsName}</td>
                    <td class="price">¥${cart.goods.price}</td>
                    <td>${cart.num}</td>
                    <td class="price">¥<fmt:formatNumber value="${cart.goods.price * cart.num}" pattern="#.00" /></td>
                    <td>
                        <a class="delete" href="${pageContext.request.contextPath}/cart?action=delete&cid=${cart.cid}" onclick="return confirm('确定删除该商品吗？')">删除</a>
                    </td>
                </tr>
                <c:set var="total" value="${total + cart.goods.price * cart.num}" />
            </c:forEach>
        </table>

        <div class="total">
            购物车总价：<span>¥<fmt:formatNumber value="${total}" pattern="#.00" /></span>
        </div>

        <form class="form-box" action="${pageContext.request.contextPath}/order?action=add" method="post">
            <input type="hidden" name="totalPrice" value="${total}">
            <div>
                <label>收货地址：</label>
                <input type="text" name="address" required placeholder="请输入您的收货地址">
            </div>
            <button class="submit-btn" type="submit">提交订单</button>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>