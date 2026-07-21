<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>RIIZE周边购物商城-首页</title>
  <style>
    * {margin: 0;padding: 0;box-sizing: border-box;font-family: "Microsoft Yahei", "微软雅黑", sans-serif;}
    body {
      background: linear-gradient(135deg, #fdfaf7 0%, #f9f4ef 100%);
      background-attachment: fixed;
    }
    .success-tip{
      width: 100%;
      background: linear-gradient(90deg, #fff6f3, #ffe9e0);
      border-bottom: 1px solid #fdb9a0;
      text-align: center;
      padding: 10px 0;
      color: #ff7d45;
      font-size: 15px;
      font-weight: 500;
      letter-spacing: 1px;
      box-shadow: 0 1px 4px rgba(255,125,69,0.08);
    }
    .user-bar {
      padding: 12px 8%;
      background: #ffffff;
      text-align: right;
      border-bottom: 1px solid #f3ebe6;
      font-size: 15px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.03);
      position: relative;
      z-index: 10;
    }
    .user-bar a {
      color: #555;
      text-decoration: none;
      margin: 0 10px;
      padding: 6px 12px;
      border-radius: 10px;
      transition: all 0.2s ease;
    }
    .user-bar a:hover {
      color: #fff;
      background: linear-gradient(90deg, #ff8a50, #ff6b35);
      box-shadow: 0 1px 5px rgba(255,107,53,0.15);
    }
    .user-bar span {
      color: #ff7d45;
      font-weight: 500;
      margin-right: 8px;
    }
    .header {
      text-align: center;
      padding: 30px 0 20px;
      margin-bottom: 30px;
      position: relative;
    }
    .header h2 {
      color: #ff7d45;
      font-size: 30px;
      font-weight: 600;
      letter-spacing: 2px;
      margin: 0;
      text-shadow: 0 1px 3px rgba(255,125,69,0.1);
    }
    .header p {
      color: #887a70;
      font-size: 14px;
      margin-top: 8px;
      letter-spacing: 2px;
    }
    .header::after{
      content: "";
      display: block;
      width: 120px;
      height: 1.5px;
      background: linear-gradient(90deg, transparent, #ff8a50, transparent);
      margin: 12px auto 0;
    }
    .content-container {
      width: 85%;
      margin: 0 auto;
      padding-bottom: 60px;
    }
    .login-tip, .empty-tip {
      text-align: center;
      padding: 90px 0;
      background: #ffffff;
      border-radius: 20px;
      border:1px solid #f3ebe6;
      color: #666;
      font-size: 18px;
      box-shadow: 0 3px 10px rgba(0,0,0,0.02);
      line-height: 2;
    }
    .login-tip a {
      color: #ff7d45;
      text-decoration: none;
      font-weight: 600;
      padding: 3px 8px;
      border-radius: 6px;
      background: #fff6f3;
    }
    .login-tip a:hover {
      background: #ffe9e0;
    }
    .goods-list {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      margin-top: 0;
    }
    .goods-item {
      width: calc(25% - 16px);
      background: #ffffff;
      padding: 25px 18px;
      border-radius: 18px;
      border:1px solid #f3ebe6;
      text-align: center;
      transition: all 0.25s ease;
      margin-bottom: 20px;
      position: relative;
      overflow: hidden;
    }
    .goods-item:hover {
      transform: translateY(-4px);
      box-shadow: 0 6px 15px rgba(255,125,69,0.06);
      border-color: #fdb9a0;
    }
    .goods-item::before{
      content: "";
      position: absolute;
      top:0;left:0;right:0;
      height: 2px;
      background: #ff8a50;
    }
    /* ✅ 修复后：商品图片完整显示，不裁剪、不变形 */
    .goods-img {
      width: 100%;
      height: auto;
      max-height: 200px;
      object-fit: contain;
      border-radius: 12px;
      margin-bottom: 18px;
      border: 1px solid #f3ebe6;
      display: block;
      background: #f0f0f0;
    }
    .goods-item h3 {
      color: #333;
      font-size: 17px;
      margin-bottom: 20px;
      height: 46px;
      line-height: 23px;
      overflow: hidden;
      font-weight: 500;
    }
    .goods-price {
      background: linear-gradient(90deg, #ff6b35, #ff8a50);
      -webkit-background-clip: text;
      color: transparent;
      font-weight: 600;
      font-size: 22px;
      margin: 10px 0;
      letter-spacing: 1px;
    }
    .goods-item .stock {
      color: #999;
      font-size: 14px;
      margin-bottom: 20px;
      padding: 4px 0;
    }
    .stock-enough { color: #ff7d45; font-weight: 500; }
    .stock-lack { color: #f04848; font-weight: 500; }
    .stock-zero { color: #b8b8b8; font-weight: 500; text-decoration: line-through; }
    .btn-group{
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 10px;
      width: 100%;
      margin-top: 10px;
    }
    .goods-btn {
      display: inline-block;
      text-decoration: none;
      padding: 9px 0;
      width: 100px;
      border-radius: 10px;
      font-size: 14px;
      background: linear-gradient(90deg, #7a8288, #6c757d);
      color: #fff;
      border: none;
      transition: all 0.2s ease;
    }
    .goods-btn:hover {
      background: linear-gradient(90deg, #6c757d, #5a6268);
      box-shadow: 0 1px 4px rgba(108,117,125,0.1);
    }
    .add-cart-btn {
      display: inline-block;
      text-decoration: none;
      padding: 9px 0;
      width: 100px;
      border-radius: 10px;
      font-size: 14px;
      background: linear-gradient(90deg, #ff8a50, #ff6b35);
      color: #fff;
      border: none;
      transition: all 0.2s ease;
    }
    .add-cart-btn:hover {
      background: linear-gradient(90deg, #ff6b35, #e85a2a);
      box-shadow: 0 1px 4px rgba(255,107,53,0.15);
    }
    .add-cart-btn-disabled {
      background: #e6e6e6 !important;
      cursor: not-allowed;
      box-shadow: none !important;
      pointer-events: none;
      color: #999 !important;
    }
  </style>
</head>
<body>
<c:if test="${param.msg == 'registerSuccess'}">
  <div class="success-tip">✅ 注册成功，欢迎加入，立即登录选购周边吧！</div>
</c:if>
<div class="user-bar">
  <c:if test="${empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/login.jsp">🔑 登录</a>
    <a href="${pageContext.request.contextPath}/register.jsp">📝 注册</a>
  </c:if>
  <c:if test="${not empty sessionScope.user}">
    <span>欢迎您：${sessionScope.user.username}</span> |
    <a href="${pageContext.request.contextPath}/cart?action=list">🛒 购物车</a> |
    <a href="${pageContext.request.contextPath}/order?action=list">📦 我的订单</a> |
    <a href="${pageContext.request.contextPath}/user?action=center">👤 用户中心</a>
  </c:if>
</div>

<div class="header">
  <h2>RIIZE store</h2>
  <p>we riize</p>
  <p>riize briize 登达！！！</p>
</div>

<div class="content-container">
  <c:if test="${empty sessionScope.user}">
    <div class="login-tip">
      🔒 您还未登录，<a href="${pageContext.request.contextPath}/login.jsp">点击登录</a> 后即可查看和选购周边商品哦~
    </div>
  </c:if>

  <c:if test="${not empty sessionScope.user}">
    <c:if test="${empty goodsList}">
      <div class="empty-tip">
        📭 暂无周边商品上架，敬请期待新品更新哦~
      </div>
    </c:if>
    <div class="goods-list">
      <c:forEach items="${goodsList}" var="goods">
        <div class="goods-item">
            <%-- ✅ 新增：商品图片展示 完美嵌入卡片 位置最优 --%>
          <img src="${pageContext.request.contextPath}/goods_img/${goods.gimg}"
               alt="${goods.gname}" class="goods-img"
               onerror="this.src='${pageContext.request.contextPath}/goods_img/default.jpg'">
          <h3>${goods.gname}</h3>
          <div class="goods-price">¥${goods.price}</div>
          <div class="stock">
            <c:choose>
              <c:when test="${goods.stock <= 0}">库存：<span class="stock-zero">缺货</span></c:when>
              <c:when test="${goods.stock > 0 && goods.stock <= 5}">库存：<span class="stock-lack">仅剩 ${goods.stock} 件</span></c:when>
              <c:otherwise>库存：<span class="stock-enough">充足 ${goods.stock} 件</span></c:otherwise>
            </c:choose>
          </div>
          <div class="btn-group">
            <a class="goods-btn" href="${pageContext.request.contextPath}/goods?action=detail&gid=${goods.gid}">查看详情</a>
            <c:choose>
              <c:when test="${goods.stock <= 0}">
                <a class="add-cart-btn add-cart-btn-disabled" href="javascript:void(0);">加入购物车</a>
              </c:when>
              <c:otherwise>
                <a class="add-cart-btn" href="${pageContext.request.contextPath}/cart?action=add&gid=${goods.gid}">加入购物车</a>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </c:forEach>
    </div>
  </c:if>
</div>
</body>
</html>