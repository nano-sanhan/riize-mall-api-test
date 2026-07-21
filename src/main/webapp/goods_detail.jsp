<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>商品详情 - RIIZE周边购物商城</title>
    <style>
        *{margin: 0;padding: 0;box-sizing: border-box;font-family: "Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;padding: 40px 0;}

        .detail-box {
            width: 70%;
            margin: 0 auto;
            padding: 35px;
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 5px 20px rgba(231,125,35,0.05);
            border: 1px solid #f8ece0;
            display: flex;
            gap: 40px;
            align-items: flex-start;
        }

        .goods-img-box {
            width: 38%;
            flex-shrink: 0;
            min-width: 260px;
        }
        .goods-img {
            width: 100% !important;
            height: auto !important;
            display: block;
            border-radius: 12px;
            border: 1px solid #f8ece0;
            box-shadow: 0 3px 10px rgba(0,0,0,0.02);
            transition: all 0.3s ease;
            cursor: pointer;
        }
        .goods-img:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(231,125,35,0.1);
        }

        .goods-info-box h2{
            background: linear-gradient(90deg, #e77d23, #fa9941);
            -webkit-background-clip: text;
            color: transparent;
            font-size: 26px;
            margin-bottom: 22px;
            padding-bottom: 18px;
            border-bottom: 1px solid #fef5ef;
            font-weight: 600;
            letter-spacing: 1px;
        }

        .goods-base {
            padding: 20px 22px;
            background: #fefaf7;
            border-radius: 10px;
            margin-bottom: 22px;
            border:1px solid #f8ece0;
        }
        .goods-id {
            font-size: 15px;
            color: #776b60;
            margin-bottom: 12px;
            line-height: 1.6;
        }
        .goods-id span {
            color: #e77d23;
            font-weight: 600;
        }

        .price {
            color: #e77d23;
            font-size: 32px;
            font-weight: 700;
            margin: 8px 0 12px;
            font-family: "微软雅黑";
        }
        .old-price {
            font-size: 14px;
            color: #b8b0a8;
            text-decoration: line-through;
            margin-left: 12px;
        }

        .stock {
            font-size: 17px;
            margin: 12px 0 20px;
            padding: 8px 0;
            border-bottom: 1px solid #fef5ef;
            font-weight: 500;
            color: #555;
        }
        .stock-normal {
            color: #e77d23;
            font-weight: 700;
            padding: 2px 6px;
            background: #fff8f2;
            border-radius: 4px;
        }
        .stock-empty {
            color: #dc3545;
            font-weight: 700;
            padding: 2px 6px;
            background: #fff5f5;
            border-radius: 4px;
        }

        .buy-num {
            margin: 22px 0;
            padding: 15px 0;
            border-bottom: 1px solid #fef5ef;
        }
        .buy-num label {
            font-size: 17px;
            color: #555;
            margin-right: 15px;
            font-weight: 500;
        }
        .num-btn {
            display: inline-block;
            width: 34px;
            height: 34px;
            line-height: 34px;
            text-align: center;
            background: #fefaf7;
            border: 1px solid #f8ece0;
            color: #e77d23;
            font-size: 17px;
            cursor: pointer;
            border-radius: 6px;
            user-select: none;
            transition: all 0.2s ease;
        }
        .num-btn:hover {
            background: #ffe6d8;
            border-color: #e77d23;
        }
        .num-input {
            width: 70px;
            height: 34px;
            line-height: 34px;
            text-align: center;
            border: 1px solid #f8ece0;
            background: #fff;
            color: #333;
            font-size: 16px;
            margin: 0 8px;
            border-radius: 6px;
            outline: none;
            transition: all 0.2s ease;
        }
        .num-input:focus {
            border-color: #e77d23;
            box-shadow: 0 0 6px rgba(231,125,35,0.15);
        }
        .num-tips {
            color: #dc3545;
            font-size: 13px;
            margin-left: 12px;
            display: none;
            line-height: 34px;
        }

        .desc {
            margin: 20px 0;
            line-height: 2.3;
            color: #666;
            font-size: 16px;
            text-indent: 2em;
            padding-right: 5px;
        }

        .goods-points {
            margin: 20px 0;
            padding: 14px 18px;
            background: #fefaf7;
            border-left: 3px solid #e77d23;
            border-radius: 8px;
            border:1px solid #f8ece0;
        }
        .goods-points h4 {
            color: #e77d23;
            font-size: 16px;
            margin-bottom: 8px;
            font-weight: 600;
        }
        .goods-points p {
            color: #666;
            font-size: 15px;
            line-height: 1.8;
        }

        .buy-tips {
            margin: 22px 0 10px;
            padding: 14px 18px;
            background: #f9fafb;
            border-radius: 8px;
            border-left: 3px solid #fa9941;
            border:1px solid #e9ecef;
        }
        .buy-tips h4 {
            color: #6c757d;
            font-size: 16px;
            margin-bottom: 8px;
            font-weight: 600;
        }
        .buy-tips p {
            color: #666;
            font-size: 14px;
            line-height: 1.8;
        }

        .add-btn, .back-btn{
            display: inline-block;
            text-decoration: none;
            padding: 14px 30px;
            border-radius: 10px;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
            border: none;
            margin-right: 15px;
            margin-top: 10px;
            transition: all 0.25s ease;
            font-weight: 500;
        }
        .add-btn {
            background: linear-gradient(90deg, #fa9941, #e77d23);
            box-shadow: 0 2px 6px rgba(231,125,35,0.1);
        }
        .add-btn:hover {
            background: linear-gradient(90deg, #e77d23, #d46a10);
            box-shadow: 0 4px 10px rgba(231,125,35,0.2);
            transform: translateY(-1px);
        }
        .back-btn{
            background: linear-gradient(90deg, #6c757d, #5a6268);
            box-shadow: 0 2px 6px rgba(108,117,125,0.1);
        }
        .back-btn:hover{
            background: linear-gradient(90deg, #5a6268, #343a40);
            box-shadow: 0 4px 10px rgba(108,117,125,0.2);
            transform: translateY(-1px);
        }
        .btn-disabled {
            background: #cccccc !important;
            cursor: not-allowed;
            box-shadow: none !important;
            transform: none !important;
        }

        .btn-group{
            margin-top: 28px;
            padding-top: 20px;
            border-top: 1px solid #fef5ef;
            text-align: left;
        }

        /* 🔥 图片预览层 - 最终完美版 */
        .img-preview {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.9);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 9999;
            cursor: pointer;
        }
        .img-preview img {
            max-width: 90%;
            max-height: 90vh;
            width: auto;
            height: auto;
            object-fit: contain;
            border-radius: 10px;
            box-shadow: 0 0 50px rgba(255,255,255,0.2);
        }
    </style>
</head>
<body>

<!-- 放大预览层 -->
<div class="img-preview" id="imgPreview" onclick="closePreview()"></div>

<div class="detail-box">
    <div class="goods-img-box">
        <img src="${pageContext.request.contextPath}/goods_img/${goods.gimg}"
             alt="${goods.gname}" class="goods-img"
             onclick="openPreview(this)"
             onerror="this.src='${pageContext.request.contextPath}/goods_img/default.jpg'">
    </div>

    <div class="goods-info-box">
        <h2>${goods.gname}</h2>
        <div class="goods-base">
            <div class="goods-id">商品编号：<span>${goods.gid}</span></div>
            <div class="price">¥${goods.price} <span class="old-price">原价 ¥${goods.price}</span></div>
        </div>

        <div class="stock">
            库存状态：
            <c:choose>
                <%-- 1. 无库存：显示缺货 --%>
                <c:when test="${goods.stock <= 0 || empty goods.stock}">
                    <span class="stock-empty">缺货 ❌</span>
                </c:when>

                <%-- 2. 库存紧张 1~5件：红色高亮 --%>
                <c:when test="${goods.stock >= 1 && goods.stock <= 5}">
                    <span class="stock-empty">库存紧张（剩余${goods.stock}件）</span>
                </c:when>

                <%-- 3. 库存充足 >5件：正常显示 --%>
                <c:otherwise>
                    <span class="stock-normal">现货充足（剩余${goods.stock}件）</span>
                </c:otherwise>
            </c:choose>
        </div>

        <c:choose>
            <c:when test="${goods.stock <= 0 || empty goods.stock}">
                <div class="buy-num">
                    <label>购买数量：</label>
                    <span class="num-tips" style="display: inline;">库存不足，暂无法购买</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="buy-num">
                    <label>购买数量：</label>
                    <span class="num-btn" onclick="changeNum(-1)">-</span>
                    <input type="number" class="num-input" id="buyNum" value="1" min="1" max="${goods.stock}">
                    <span class="num-btn" onclick="changeNum(1)">+</span>
                    <span class="num-tips" id="numTips">最多可购买${goods.stock}件</span>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="desc">商品详情：${goods.gdesc}</div>
        <div class="goods-points">
            <h4>✨ 商品亮点</h4>
            <p>正版周边 | 品质保障 | 现货速发 | 售后无忧 | 专属包装</p>
        </div>

        <div class="buy-tips">
            <h4>💡 购买须知</h4>
            <p>1. 库存实时更新，拍下请尽快付款，避免缺货；<br>
                2. 发货后同步物流信息，收货地址请填写完整；<br>
                3. 产品无质量问题不退不换，有问题48小时内联系客服。
            </p>
        </div>

        <div class="btn-group">
            <c:choose>
                <c:when test="${goods.stock <= 0 || empty goods.stock}">
                    <a class="add-btn btn-disabled" href="javascript:void(0);">加入购物车</a>
                </c:when>
                <c:otherwise>
                    <a class="add-btn" href="javascript:void(0);" onclick="addToCart(${goods.gid})">加入购物车</a>
                </c:otherwise>
            </c:choose>
            <a class="back-btn" href="${pageContext.request.contextPath}/goods?action=list">返回商品列表</a>
        </div>
    </div>
</div>

<script>
    function changeNum(type) {
        let numInput = document.getElementById("buyNum");
        let numTips = document.getElementById("numTips");
        let maxNum = ${goods.stock};
        let currNum = parseInt(numInput.value);

        if(type == 1){
            if(currNum >= maxNum){
                numTips.innerHTML = "已达最大库存，最多可购"+maxNum+"件";
                numTips.style.display = "inline";
                return;
            }
            numInput.value = currNum + 1;
        }else{
            if(currNum <= 1){
                numTips.innerHTML = "最少购买1件哦~";
                numTips.style.display = "inline";
                return;
            }
            numInput.value = currNum - 1;
        }
        numTips.style.display = "none";
    }

    document.getElementById("buyNum").onblur = function(){
        let numTips = document.getElementById("numTips");
        let maxNum = ${goods.stock};
        let currNum = parseInt(this.value);

        if(isNaN(currNum) || currNum < 1){
            this.value = 1;
            numTips.innerHTML = "最少购买1件哦~";
            numTips.style.display = "inline";
        }else if(currNum > maxNum){
            this.value = maxNum;
            numTips.innerHTML = "最多可购"+maxNum+"件";
            numTips.style.display = "inline";
        }else{
            numTips.style.display = "none";
        }
    }

    function addToCart(gid) {
        let buyNum = document.getElementById("buyNum").value;
        let basePath = "${pageContext.request.contextPath}";
        location.href = basePath + "/cart?action=add&gid="+gid+"&buyNum="+buyNum;
    }

    // 🔥 最终版：图片放大预览（完整显示 + 自适应 + 大小适中）
    function openPreview(imgElement) {
        let preview = document.getElementById("imgPreview");
        let clone = imgElement.cloneNode(true);
        clone.alt = "";
        preview.innerHTML = "";
        preview.appendChild(clone);
        preview.style.display = "flex";
        document.body.style.overflow = "hidden";
    }

    function closePreview() {
        document.getElementById("imgPreview").style.display = "none";
        document.body.style.overflow = "auto";
    }
</script>
</body>
</html>