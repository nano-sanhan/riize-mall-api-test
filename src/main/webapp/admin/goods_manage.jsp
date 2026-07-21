<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>商品管理 - riize周边购物商城</title>
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
            width: 80%;
            max-width: 1000px;
            margin: 0 auto;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            border-radius: 12px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            padding: 20px 0;
            overflow: hidden;
        }
        .btn-group{
            width:95%;
            margin:0 auto 20px;
            text-align: left;
            padding-bottom: 12px;
            border-bottom:1px solid #f5e6d8;
        }
        .add-btn {
            text-decoration: none;
            color: #fff;
            background: linear-gradient(90deg, #20c997, #28a745);
            padding: 9px 20px;
            border-radius: 8px;
            margin-right:15px;
            font-size:14px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .add-btn:hover{
            background: linear-gradient(90deg, #198754, #20c997);
            box-shadow:0 2px 6px rgba(40,167,69,0.2);
        }
        .back-btn {
            text-decoration: none;
            color: #fff;
            background: linear-gradient(90deg, #6c757d, #5a6268);
            padding: 9px 20px;
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
            width: 95%;
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
        td a{
            margin:0 8px;
            text-decoration: none;
            font-size:14px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        td a:first-child{
            color: #fa9941;
        }
        td a:first-child:hover{
            color: #e77d23;
        }
        td a:last-child{
            color: #dc3545;
        }
        td a:last-child:hover{
            color: #bb2d3b;
        }
        .price-text{
            color:#e77d23;
            font-weight:600;
            font-size:15px;
        }
        .empty-tip{
            padding: 60px 0;
            color:#999;
            font-size:16px;
        }
    </style>
</head>
<body>
<h2>商品管理中心</h2>
<div class="main-box">
    <div class="btn-group">
        <a class="add-btn" href="${pageContext.request.contextPath}/goods?action=toAdd">✨ 添加商品</a>
        <a class="back-btn" href="${pageContext.request.contextPath}/admin/admin_index.jsp">← 返回后台首页</a>
    </div>
    <table>
        <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>商品价格</th>
            <th>商品库存</th>
            <th>操作管理</th>
        </tr>
        <c:if test="${empty goodsList}">
            <tr>
                <td colspan="5" class="empty-tip">暂无商品数据，点击【添加商品】上架新商品吧~</td>
            </tr>
        </c:if>
        <c:forEach items="${goodsList}" var="goods">
            <tr>
                <td>${goods.gid}</td>
                <td>${goods.gname}</td>
                <td class="price-text">¥${goods.price}</td>
                <td>${goods.stock}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/goods?action=toEdit&gid=${goods.gid}">修改</a>
                    <a href="${pageContext.request.contextPath}/goods?action=delete&gid=${goods.gid}" onclick="return confirm('确定删除该商品吗？删除后不可恢复！')">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>