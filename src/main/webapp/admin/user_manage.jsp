<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>用户管理 - riize周边购物商城</title>
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
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 20px;
            letter-spacing: 2px;
        }
        .main-box{
            width: 85%;
            max-width: 1300px;
            margin: 0 auto;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            border-radius: 12px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            padding: 25px 0;
            overflow: hidden;
        }
        .btn-box{width:95%;margin: 0 auto 20px;text-align: left;}
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

        .search-box{width:95%;margin:0 auto;text-align: center;margin-bottom:25px;}
        .search-box input{
            padding:9px 15px;
            border:1px solid #f0e0d5;
            border-radius:8px;
            width:280px;
            font-size:14px;
            outline: none;
            background: #fffbf7;
            transition: all 0.25s ease;
        }
        .search-box input:focus{
            border-color: #fa9941;
            box-shadow: 0 0 8px rgba(250, 153, 65, 0.25);
            background: #fff;
        }
        .search-box button{
            padding:9px 18px;
            border:none;
            background:linear-gradient(90deg, #fa9941, #e77d23);
            color:#fff;
            border-radius:8px;
            cursor:pointer;
            margin-left:10px;
            font-size:14px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .search-box button:hover{
            background:linear-gradient(90deg, #e77d23, #d46a10);
            box-shadow:0 2px 6px rgba(231,125,35,0.2);
        }

        table {
            width: 95%;
            margin: 0 auto;
            border-collapse: collapse;
            border-radius:10px;
            overflow: hidden;
        }
        th {
            background: linear-gradient(90deg, #fff8f5, #fff3eb);
            padding: 12px 8px;
            text-align: center;
            color:#887766;
            font-weight:600;
            font-size:15px;
            border: 1px solid #f5e6d8;
            letter-spacing: 1px;
        }
        td {
            padding: 12px 8px;
            text-align: center;
            color: #333;
            font-size:14px;
            border: 1px solid #f5e6d8;
            background: #fff;
            line-height: 1.6;
        }
        table tr:hover td{
            background: #fffbf7;
            transition: all 0.2s ease;
        }

        .user-type-normal{color: #6c757d; font-weight:500;}
        .user-type-admin{color: #dc3545; font-weight:600;}
        .no-address{color: #999; font-style: italic; font-size:13px;}
        .status-normal{color: #28a745; font-weight:600;}
        .status-disable{color: #dc3545; font-weight:600;}

        /* 操作按钮美化升级 渐变+圆角+hover反馈 */
        .btn-disable{
            color: #fff;
            background: linear-gradient(90deg, #dc3545, #bb2d3b);
            text-decoration: none;
            padding:6px 12px;
            border-radius:8px;
            font-size:13px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .btn-disable:hover{
            opacity: 0.9;
            box-shadow:0 2px 4px rgba(220,53,69,0.2);
        }
        .btn-enable{
            color: #fff;
            background: linear-gradient(90deg, #20c997, #28a745);
            text-decoration: none;
            padding:6px 12px;
            border-radius:8px;
            font-size:13px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .btn-enable:hover{
            opacity: 0.9;
            box-shadow:0 2px 4px rgba(40,167,69,0.2);
        }

        .empty-tip{
            text-align:center;
            padding:70px 0;
            color:#999;
            font-size:16px;
        }
        /* 地址/手机号超长自动换行 */
        .txt-wrap{word-break: break-all;}
    </style>
</head>
<body>
<h2>用户管理中心</h2>
<div class="main-box">
    <div class="btn-box">
        <a class="back-btn" href="${pageContext.request.contextPath}/admin/admin_index.jsp">← 返回后台首页</a>
    </div>
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/admin" method="get">
            <input type="hidden" name="action" value="searchUser">
            <input type="text" name="username" placeholder="请输入用户名搜索用户" />
            <button type="submit">🔍 搜索用户</button>
        </form>
    </div>

    <c:if test="${empty userList}">
        <div class="empty-tip">暂无用户数据</div>
    </c:if>
    <c:if test="${not empty userList}">
        <table>
            <tr>
                <th>序号</th>
                <th>用户ID</th>
                <th>用户名</th>
                <th>手机号码</th>
                <th>收货地址</th>
                <th>用户类型</th>
                <th>用户状态</th>
                <th>注册时间</th>
                <th>操作</th>
            </tr>
            <c:forEach items="${userList}" var="user" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${user.uid}</td>
                    <td>${user.username}</td>
                    <td>${user.phone == null || user.phone == '' ? '未填写' : user.phone}</td>
                    <td class="txt-wrap">${user.address == null || user.address == '' ? '<span class="no-address">未设置收货地址</span>' : user.address}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.type == 0}"><span class="user-type-normal">普通用户</span></c:when>
                            <c:when test="${user.type == 1}"><span class="user-type-admin">✨ 管理员</span></c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.type == 1}">-</c:when>
                            <c:when test="${user.status == 1}"><span class="status-normal">✅ 正常</span></c:when>
                            <c:when test="${user.status == 0}"><span class="status-disable">❌ 已禁用</span></c:when>
                        </c:choose>
                    </td>
                    <td>${user.regTime == null ? '暂无数据' : user.regTime}</td>
                    <td>
                        <c:if test="${user.type == 0}">
                            <c:choose>
                                <c:when test="${user.status == 1}">
                                    <a href="${pageContext.request.contextPath}/admin?action=updateUserStatus&uid=${user.uid}&status=0" class="btn-disable">禁用账号</a>
                                </c:when>
                                <c:when test="${user.status == 0}">
                                    <a href="${pageContext.request.contextPath}/admin?action=updateUserStatus&uid=${user.uid}&status=1" class="btn-enable">启用账号</a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        <c:if test="${user.type == 1}">-</c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</div>
</body>
</html>