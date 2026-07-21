<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>我的订单</title>
    <style>
        /* 全局样式重置 统一字体和边距 跟【购物车页面】完全保持一致 橙色主题统一 */
        *{margin:0;padding:0;box-sizing: border-box;font-family: "Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;padding: 30px 0;}

        /* 页面标题样式 优化配色+加粗 与购物车标题风格统一 */
        h2{text-align: center;color: #e77d23;margin-bottom: 30px;font-size: 26px;font-weight: 600;letter-spacing:1px;}

        /* 订单表格 核心美化升级 - 圆角加大+柔和阴影+橙色系边框 质感拉满 保留原功能 */
        table {
            width: 88%;
            margin: 0 auto;
            border-collapse: collapse;
            background: #ffffff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 3px 12px rgba(234, 125, 35, 0.06);
            border:1px solid #f5e6d8;
        }
        th, td {
            border: 1px solid #f5e6d8;
            padding: 15px;
            text-align: center;
        }
        /* 表头样式 暖橙底色 配色协调 字体优化 */
        th {
            background: #fff8f5;
            color:#666;
            font-weight: 500;
            font-size: 15px;
        }
        /* 表格行悬停高亮 柔和橙色系高亮 跟购物车hover一致 提升交互体验 */
        tr:hover{background-color: #fff8f5;transition: all 0.2s;}

        /* 表格内容文字样式 字体优化 更清晰 */
        td{color: #555;font-size: 15px;line-height: 1.5;}
        /* 收货地址自动换行 防止内容过长撑破表格 优化行高更美观 */
        td.address-cell{word-break: break-all;padding:15px 10px;}
        /* 商品名称样式 改为橙色主题高亮 与全站风格统一 */
        .goods-name{color: rgba(68, 66, 61, 0.54); font-weight: 600;}
        /* 订单金额样式 橙色突出 与购物车价格配色一致 醒目不刺眼 */
        .price{color: #e77d23; font-weight: bold;font-size:16px;}

        /* 订单状态样式 优化配色+圆角+间距 保留原状态逻辑 更精致美观 */
        .status { padding: 6px 10px; border-radius: 6px; color: #fff; font-size:13px; font-weight:500; }
        .wait { background: #ff9500; }  /* 待发货-暖橙 */
        .send { background: #00b4aa; } /* 已发货-青蓝 */
        .done { background: #27ae60; } /* 已完成-清新绿 */

        /* 删除订单按钮 美化升级 与购物车删除按钮样式完全统一 橙色系风格 */
        .delete {
            color: #e77d23;
            text-decoration: none;
            padding:6px 12px;
            border-radius: 6px;
            font-size: 14px;
            border:1px solid #ffe6d8;
        }
        .delete:hover {
            color: #fff;
            background: #e77d23;
            transition: all 0.2s ease;
        }

        /* 空订单提示 美化升级 橙色主题卡片 与全站风格一致 */
        .empty-tip{
            width:88%;
            margin:0 auto;
            padding: 90px 0;
            background:#fff;
            text-align:center;
            border-radius:10px;
            box-shadow: 0 3px 12px rgba(234, 125, 35, 0.06);
            color:#999;
            font-size:18px;
            border:1px solid #f5e6d8;
        }

        /* 返回首页按钮 美化升级 橙色主题 与购物车返回按钮样式完全统一 位置优化 */
        .back-home{
            display: block;
            width: 110px;
            height: 40px;
            line-height: 40px;
            text-align: center;
            background: #fa9941;
            color:#fff;
            text-decoration: none;
            border-radius:6px;
            margin: 35px auto 0;
            font-size: 15px;
        }
        .back-home:hover{
            background: #e77d23;
            transition: all 0.2s ease;
        }
        /* 禁用用户提示样式 - 居中红色 醒目不刺眼 */
        .error-tip{color:#dc3545;text-align:center;margin:15px auto 25px;font-size:15px;font-weight:500;}
    </style>
</head>
<body>
<h2>我的订单</h2>

<%-- ✅ 核心新增：显示禁用用户的拦截提示信息，有错误信息才展示 --%>
<c:if test="${not empty errorMsg}">
    <div class="error-tip">${errorMsg}</div>
</c:if>

<table>
    <tr>
        <th>订单ID</th>
        <th>购买商品</th>
        <th>订单总价</th>
        <th>下单时间</th>
        <th>订单状态</th>
        <th>收货地址</th>
        <th>操作</th>
    </tr>
    <c:if test="${empty orderList}">
        <tr>
            <td colspan="7" class="empty-tip">暂无订单记录，快去选购商品下单吧~</td>
        </tr>
    </c:if>
    <c:if test="${not empty orderList}">
        <c:forEach items="${orderList}" var="order">
            <tr>
                <td>${order.oid}</td>
                <td><span class="goods-name">${order.goodsName}</span></td>
                <td class="price">¥${order.totalPrice}</td>
                <td><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <span class="status
                        <c:choose>
                            <c:when test="${order.status == 0}">wait</c:when>
                            <c:when test="${order.status == 1}">send</c:when>
                            <c:when test="${order.status == 2}">done</c:when>
                        </c:choose>">
                        <c:choose>
                            <c:when test="${order.status == 0}">已支付</c:when>
                            <c:when test="${order.status == 1}">待发货</c:when>
                            <c:when test="${order.status == 2}">已完成</c:when>
                        </c:choose>
                    </span>
                </td>
                <td class="address-cell">${order.address}</td>
                <td>
                    <a class="delete" href="${pageContext.request.contextPath}/order?action=delete&oid=${order.oid}" onclick="return confirm('确定删除该订单吗？删除后不可恢复哦！')">删除订单</a>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</table>

<a class="back-home" href="${pageContext.request.contextPath}/goods?action=list">返回首页</a>
</body>
</html>