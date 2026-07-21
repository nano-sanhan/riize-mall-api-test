<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>我的购物车</title>
    <style>
        *{margin:0;padding:0;box-sizing: border-box;font-family: "Microsoft Yahei", sans-serif;}
        body{background-color: #f9f5f1;padding-top: 30px;}
        .top-box{width: 88%;margin:0 auto;display: flex;justify-content: space-between;align-items: center;margin-bottom: 30px;}
        .top-box h2{color: #e77d23;font-size:24px;font-weight: 600;}
        .back-btn{text-decoration: none;padding:9px 18px;background: #fa9941;color:#fff;border-radius:6px;}
        .back-btn:hover{background: #e77d23;}
        table { width: 88%; margin: 0 auto; border-collapse: collapse; background: #fff; border-radius:8px;overflow: hidden;box-shadow: 0 2px 10px rgba(234, 125, 35, 0.05);border:1px solid #f5e6d8;}
        th, td { border: 1px solid #f5e6d8; padding: 14px; text-align: center; }
        th { background: #fff8f5; color:#666;font-weight: 500;}
        tr:hover{background-color: #fff8f5;}
        .price{color: #e77d23; font-weight: bold;}
        .delete { color: #e77d23; text-decoration: none; padding:5px 10px;border-radius:6px;border:1px solid #ffe6d8;}
        .delete:hover { color: #fff; background: #e77d23;}
        .total { width: 88%; margin:25px auto 20px; text-align: right; font-size: 20px; font-weight: bold; color:#333;padding-right: 10px;}
        .total span{color: #e77d23;padding-left:6px;}
        .form-box{width: 88%;margin:0 auto;text-align: right;padding-bottom: 60px;}
        .form-box div{margin-bottom:20px;display: flex;justify-content: flex-end;align-items: center;}
        .form-box label{font-size:16px;color:#666;margin-right:12px;width: 80px;text-align: right;}
        .form-box input[type="text"]{width: 360px; padding:11px; border:1px solid #ddd; border-radius:6px;outline:none;}
        .form-box input[type="text"]:focus{border-color: #fa9941;}
        .submit-btn { padding: 11px 30px; background: #fa9941; color: #fff; border: none; border-radius:6px; cursor: pointer; font-size:16px;}
        .submit-btn:hover{background:#e77d23}
        .empty-cart{width:88%;margin:0 auto;padding:90px 0;background:#fff;text-align:center;border-radius:8px;box-shadow:0 2px 10px rgba(234,125,35,0.05);color:#666;font-size:18px;border:1px solid #f5e6d8;}
        .num-input{width:60px;text-align:center;padding:4px;border:1px solid #ddd;border-radius:4px;}
        .update-btn{padding:3px 8px;background:#fa9941;color:white;border:none;border-radius:4px;cursor:pointer;}
        .stock-error {
            width: 88%;
            margin: 0 auto 15px;
            padding: 10px;
            background: #fff2f2;
            color: #e53e3e;
            border: 1px solid #feb2b2;
            border-radius: 6px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="top-box">
    <h2>我的购物车</h2>
    <a class="back-btn" href="${pageContext.request.contextPath}/goods?action=list">返回首页</a>
</div>

<c:if test="${not empty stockError}">
    <div class="stock-error">${stockError}</div>
</c:if>

<form id="cartForm" action="${pageContext.request.contextPath}/order?action=add" method="post" onsubmit="return validateSubmit()">
    <c:choose>
        <c:when test="${empty cartList}">
            <div class="empty-cart">您的购物车空空如也~</div>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th><input type="checkbox" id="checkAll" onclick="toggleCheckAll()"></th>
                    <th>商品名称</th>
                    <th>单价</th>
                    <th>购买数量</th>
                    <th>小计</th>
                    <th>操作</th>
                </tr>
                <c:set var="total" value="0"/>
                <c:forEach items="${cartList}" var="cart" varStatus="status">
                    <tr>
                        <td><input type="checkbox" name="cid" value="${cart.cid}" class="item" data-index="${status.index}"></td>
                        <td>${cart.goodsName}</td>
                        <td class="price" data-price="${cart.goods.price}">¥${cart.goods.price}</td>
                        <td>
                            <input type="number" class="num-input" name="num_${cart.cid}" value="${cart.num}" min="1" max="${cart.goods.stock}" data-cid="${cart.cid}" onchange="calculateTotal()">
                            <button class="update-btn" type="button" onclick="updateQuantity(${cart.cid})">修改</button>
                        </td>
                        <td class="price">¥<fmt:formatNumber value="${cart.goods.price * cart.num}" pattern="#.00"/></td>
                        <td>
                            <a class="delete" href="javascript:if(confirm('确定删除？')) location.href='${pageContext.request.contextPath}/cart?action=delete&cid=${cart.cid}'">删除</a>
                        </td>
                    </tr>
                    <c:set var="total" value="${total + cart.goods.price * cart.num}"/>
                </c:forEach>
            </table>

            <div class="total">总价：<span id="totalPriceDisplay">¥0.00</span></div>

            <div class="form-box">
                <div>
                    <label>收货地址：</label>
                    <input type="text" name="address" required placeholder="请填写收货地址">
                </div>
                <input type="hidden" id="totalPrice" name="totalPrice" value="0.00">
                <button class="submit-btn" type="submit">提交选中商品订单</button>
            </div>
        </c:otherwise>
    </c:choose>
</form>

<script>
    function updateQuantity(cid) {
        var numInput = document.querySelector('input[name="num_' + cid + '"]');
        if (numInput) {
            window.location.href = '${pageContext.request.contextPath}/cart?action=update&cid=' + cid + '&num=' + numInput.value;
        }
    }

    function toggleCheckAll() {
        var checkAllBox = document.getElementById('checkAll');
        var isChecked = checkAllBox.checked;
        var itemBoxes = document.getElementsByName('cid');
        for (var i = 0; i < itemBoxes.length; i++) {
            itemBoxes[i].checked = isChecked;
        }
        calculateTotal();
    }

    function calculateTotal() {
        var itemBoxes = document.getElementsByName('cid');
        var total = 0;
        var allChecked = true;
        for (var i = 0; i < itemBoxes.length; i++) {
            if (itemBoxes[i].checked) {
                var tr = itemBoxes[i].parentNode.parentNode;
                var priceTd = tr.cells[2];
                var numInput = tr.querySelector('.num-input');
                var priceText = priceTd.textContent.replace('¥', '').trim();
                var price = parseFloat(priceText);
                var num = parseInt(numInput.value) || 1;
                total += price * num;
            } else {
                allChecked = false;
            }
        }
        document.getElementById('totalPrice').value = total.toFixed(2);
        document.getElementById('totalPriceDisplay').textContent = '¥' + total.toFixed(2);
        document.getElementById('checkAll').checked = allChecked;
    }

    window.onload = function() {
        var items = document.getElementsByName('cid');
        for (var i = 0; i < items.length; i++) {
            items[i].addEventListener('change', calculateTotal);
        }
        calculateTotal();
    };

    function validateSubmit() {
        var items = document.getElementsByName('cid');
        var hasChecked = false;
        for (var i = 0; i < items.length; i++) {
            if (items[i].checked) {
                hasChecked = true;
                break;
            }
        }
        if (!hasChecked) {
            alert('请至少选择一件商品！');
            return false;
        }
        return true;
    }
</script>
</body>
</html>