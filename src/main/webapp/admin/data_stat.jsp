<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>数据统计中心 - riize周边购物商城</title>
    <style>
        *{margin:0;padding:0;box-sizing: border-box;font-family: "Microsoft Yahei", "微软雅黑", sans-serif;}
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
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 25px;
            letter-spacing: 2px;
        }
        /* 主容器 统一卡片风格 */
        .main-container{
            width: 80%;
            max-width: 1200px;
            margin: 0 auto;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            border-radius: 12px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            padding: 25px 0;
            overflow: hidden;
        }
        .btn-box{width:95%;margin: 0 auto 25px;text-align: left;}
        .back-btn{
            text-decoration: none;
            color: #fff;
            background: linear-gradient(90deg, #6c757d, #5a6268);
            padding: 9px 18px;
            border-radius: 8px;
            font-size:14px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .back-btn:hover{
            background: linear-gradient(90deg, #5a6268, #343a40);
            box-shadow:0 2px 6px rgba(108,117,125,0.2);
        }

        /* ===== 统计卡片 重点美化+丰富样式 ===== */
        .card-box{
            width:95%;
            margin: 0 auto 35px;
            display: flex;
            justify-content: space-between;
            gap: 20px;
        }
        .card{
            flex: 1;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            padding: 25px 15px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(231,125,35,0.08), 0 1px 4px rgba(0,0,0,0.05);
            text-align: center;
            border:1px solid #f5e6d8;
            position: relative;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        /* 卡片顶部小渐变条 提升质感 */
        .card::before{
            content: "";
            position: absolute;
            top:0;left:0;right:0;height:4px;
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
        }
        .card-goods::before{background: linear-gradient(90deg, #fa9941, #e77d23);}
        .card-user::before{background: linear-gradient(90deg, #20c997, #28a745);}
        .card-order::before{background: linear-gradient(90deg, #00b4aa, #17a2b8);}
        /* 卡片悬浮上浮+阴影加深 动态效果 */
        .card:hover{
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(231,125,35,0.12), 0 2px 6px rgba(0,0,0,0.06);
        }
        .card h3{
            color: #887766;
            font-weight:600;
            margin-bottom: 15px;
            font-size:16px;
            letter-spacing:1px;
        }
        .card .num{
            font-size: 32px;
            font-weight: 700;
            line-height: 1.2;
        }
        .card-goods .num{color: #e77d23;}
        .card-user .num{color: #28a745;}
        .card-order .num{color: #17a2b8;}
        .card .desc{
            font-size:13px;
            color:#999;
            margin-top:8px;
        }

        /* ===== 销量榜单标题 新增美化 ===== */
        .table-title{
            width:95%;
            margin:0 auto 15px;
            font-size:18px;
            color:#887766;
            font-weight:600;
            letter-spacing:1px;
            padding-left:5px;
            border-left:3px solid #fa9941;
        }

        /* ===== 销量TOP3表格 美化升级 ===== */
        table{
            width: 95%;
            margin: 0 auto;
            border-collapse: collapse;
            background: #fff;
            border-radius:10px;
            overflow: hidden;
            border:1px solid #f5e6d8;
        }
        th{
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
            padding: 14px 8px;
            text-align: center;
            font-size: 14px;
            color:#333;
            background: #fff;
        }
        table tr:hover td{
            background: #fffbf7;
            transition: all 0.2s ease;
        }
        /* 销量排名数字特殊样式 突出TOP榜单 */
        td.rank-num{
            color: #e77d23;
            font-weight:700;
            font-size:16px;
        }
        /* 销量数字高亮 */
        td.sale-num{
            color: #28a745;
            font-weight:600;
        }
        /* 空数据提示美化 */
        .empty-tip{
            padding: 60px 0;
            color:#999;
            font-size:16px;
        }
    </style>
</head>
<body>
<h2>数据统计中心</h2>
<div class="main-container">
    <div class="btn-box">
        <a class="back-btn" href="${pageContext.request.contextPath}/admin/admin_index.jsp">← 返回后台首页</a>
    </div>

    <!-- 统计卡片：商品总数、用户总数、订单总数 -->
    <div class="card-box">
        <div class="card card-goods">
            <h3>商品总数</h3>
            <div class="num">${goodsCount}</div>
            <div class="desc">商城上架商品总数量</div>
        </div>
        <div class="card card-user">
            <h3>用户总数</h3>
            <div class="num">${userCount}</div>
            <div class="desc">平台注册用户总数量</div>
        </div>
        <div class="card card-order">
            <h3>订单总数</h3>
            <div class="num">${orderCount}</div>
            <div class="desc">平台成交订单总数量</div>
        </div>
    </div>

    <!-- 销量TOP3商品表格 - 新增标题更清晰 -->
    <div class="table-title">📊 商品销量排行榜 TOP3</div>
    <table>
        <tr>
            <th>销量排名</th>
            <th>商品名称</th>
            <th>销售数量</th>
        </tr>
        <c:forEach items="${topGoodsList}" var="item" varStatus="status">
            <tr>
                <td class="rank-num">${status.index+1}</td>
                <td>${item.goodsName}</td>
                <td class="sale-num">${item.saleNum}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty topGoodsList}">
            <tr>
                <td colspan="3" class="empty-tip">暂无商品销量数据</td>
            </tr>
        </c:if>
    </table>
</div>
</body>
</html>