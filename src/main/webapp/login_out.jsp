<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 核心代码：销毁整个session会话，彻底清除登录的user信息
    session.invalidate();
    // 退出后，强制跳转到商城首页
    response.sendRedirect(request.getContextPath() + "/goods?action=list");
%>