<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>订单管理 - riize周边购物商城</title>
    <style>
        *{margin:0;padding:0;box-sizing: border-box;font-family:"Microsoft Yahei", "微软雅黑", sans-serif;}
        body{
            background: linear-gradient(120deg, #f9f5f1 0%, #fcf7f2 100%);
            background-attachment: fixed;
            padding: 30px 0;
        }
        h2{
            text-align:center;
            background: linear-gradient(90deg, #e77d23, #fa9941);
            -webkit-background-clip: text;
            color: transparent;
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 25px;
            letter-spacing: 2px;
        }
        .main-box{
            width: 95%;
            max-width: 1400px;
            margin: 0 auto;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            border-radius: 12px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            padding: 20px 0;
            overflow: hidden;
        }
        .btn-box{
            width:96%;
            margin: 0 auto 20px;text-align: left;
            padding-bottom: 12px;
            border-bottom:1px solid #f5e6d8;
        }
        .back-btn{
            text-decoration: none;
            color: #fff;
            background: linear-gradient(90deg, #6c757d, #5a6268);
            padding: 8px 16px;
            border-radius: 8px;
            font-size:14px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .back-btn:hover{
            background: linear-gradient(90deg, #5a6268, #343a40);
            box-shadow:0 2px 6px rgba(108,117,125,0.2);
        }
        table {
            width: 96%;
            margin: 0 auto;
            border-collapse: collapse;
            border-radius: 10px;
            overflow: hidden;
        }
        th {
            background: linear-gradient(90deg, #fff8f5, #fff3eb);
            border: 1px solid #f5e6d8;
            padding: 12px 8px;
            text-align: center;
            font-size: 15px;
            color:#887766;
            font-weight:600;
            letter-spacing: 1px;
        }
        td {
            border: 1px solid #f5e6d8;
            padding: 12px 8px;
            text-align: center;
            font-size: 14px;
            color:#333;
            background: #fff;
        }
        tr:hover td{
            background: #fffbf7;
            transition: all 0.2s ease;
        }
        .status {
            padding: 6px 10px;
            border-radius: 8px;
            color: #fff;
            font-size:13px;
            font-weight:500;
            display: inline-block;
            min-width: 70px;
        }
        .wait { background: linear-gradient(90deg, #ff9500, #ffc107); }
        .send { background: linear-gradient(90deg, #00b4aa, #17a2b8); }
        .done { background: linear-gradient(90deg, #20c997, #28a745); }
        select{
            padding:7px 10px;
            border-radius:8px;
            border:1px solid #f0e0d5;
            cursor:pointer;
            font-size:14px;
            color:#333;
            background: #fffbf7;
            transition: all 0.25s ease;
            min-width: 100px;
        }
        select:focus,select:hover{
            border-color: #fa9941;
            box-shadow: 0 0 8px rgba(250, 153, 65, 0.25);
            outline: none;
        }
        td.txt-wrap{
            word-break: break-all;
            line-height:1.6;
            padding: 12px 10px;
        }
        .price{
            color:#e77d23;
            font-weight:600;
            font-size:15px;
        }
        /* 空数据提示样式 */
        .empty-tip{
            padding: 60px 0;
            color:#999;
            font-size:16px;
        }
    </style>
</head>
<body>
<h2>订单管理中心</h2>
<div class="main-box">
    <div class="btn-box">
        <a class="back-btn" href="${pageContext.request.contextPath}/admin/admin_index.jsp">← 返回后台首页</a>
    </div>
    <table>
        <tr>
            <th>订单ID</th>
            <th>用户ID</th>
            <th>用户名称</th>
            <th>订单总价</th>
            <th>购买商品</th>
            <th>订单状态</th>
            <th>收货地址</th>
            <th>操作</th>
        </tr>
        <c:if test="${empty orderList}">
            <tr>
                <td colspan="8" class="empty-tip">暂无订单数据</td>
            </tr>
        </c:if>
        <c:forEach items="${orderList}" var="order">
            <tr>
                <td>${order.oid}</td>
                <td>${order.uid}</td>
                <td>${order.username}</td>
                <td class="price">¥${order.totalPrice}</td>
                <td class="txt-wrap">${order.goodsName}</td>
                <td>
                            <span class="status <c:choose>
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
                <td class="txt-wrap">${order.address}</td>
                <td>
                    <select onchange="updateStatus(${order.oid}, this.value)">
                        <option value="0" <c:if test="${order.status == 0}">selected</c:if>>已支付</option>
                        <option value="1" <c:if test="${order.status == 1}">selected</c:if>>待发货</option>
                        <option value="2" <c:if test="${order.status == 2}">selected</c:if>>已完成</option>
                    </select>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    function updateStatus(oid, status) {
        window.location.href = "${pageContext.request.contextPath}/order?action=updateStatus&oid=" + oid + "&status=" + status;
    }
</script>
</body>
</html>