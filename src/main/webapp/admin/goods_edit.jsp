<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改商品 - riize周边购物商城</title>
    <style>
        *{margin:0;padding:0;box-sizing: border-box;font-family: "Microsoft Yahei", "微软雅黑", sans-serif;}
        body{
            background: linear-gradient(120deg, #f9f5f1 0%, #fcf7f2 100%);
            background-attachment: fixed;
            padding: 40px 0;
        }
        .form-box{
            width: 620px;
            margin: 0 auto;
            background: linear-gradient(180deg, #ffffff, #fffbf7);
            padding:35px 40px;
            border-radius: 12px;
            border:1px solid #f5e6d8;
            box-shadow: 0 6px 20px rgba(231,125,35,0.08), 0 2px 8px rgba(0,0,0,0.03);
            position: relative;
        }
        .form-box::before{
            content: "";
            position: absolute;
            top:0;left:0;right:0;
            height: 4px;
            background: linear-gradient(90deg, #fa9941, #e77d23);
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
        }
        h2{
            text-align: center;
            background: linear-gradient(90deg, #e77d23, #fa9941);
            -webkit-background-clip: text;
            color: transparent;
            margin-bottom:30px;
            font-size: 24px;
            font-weight: 700;
            letter-spacing: 2px;
            padding-bottom: 18px;
            border-bottom:1px solid #f5e6d8;
        }
        .form-item{
            margin-bottom:22px;
            display: flex;
            align-items: flex-start;
        }
        .form-item label{
            width: 100px;
            text-align: right;
            padding-right:20px;
            padding-top:10px;
            color:#887766;
            font-size:15px;
            font-weight: 500;
            letter-spacing: 1px;
        }
        .form-item input,textarea{
            width: 420px;
            padding:10px 15px;
            border:1px solid #f0e0d5;
            border-radius:8px;
            outline:none;
            font-size:14px;
            color:#333;
            background: #fffbf7;
            transition: all 0.25s ease;
        }
        .form-item input:focus,textarea:focus{
            border-color: #fa9941;
            box-shadow: 0 0 8px rgba(250, 153, 65, 0.25);
            background: #fff;
            outline: none;
        }
        .form-item textarea{
            height: 110px;
            resize: none;
            line-height:1.6;
        }
        .form-item .disabled-input{
            background: #f5f0eb;
            color:#999;
            cursor: not-allowed;
            border-color: #e6d9cf;
        }
        .form-item input::placeholder, textarea::placeholder{
            color: #c9c2ba;
        }
        .btn-group{
            text-align: center;
            margin-top:35px;
            padding-top: 20px;
            border-top:1px solid #f5e6d8;
        }
        .submit-btn{
            padding:10px 35px;
            background: linear-gradient(90deg, #fa9941, #e77d23);
            color:#fff;
            border:none;
            border-radius:8px;
            cursor:pointer;
            margin-right:15px;
            font-size:15px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .submit-btn:hover{
            background: linear-gradient(90deg, #e77d23, #d46a10);
            box-shadow:0 2px 6px rgba(231,125,35,0.2);
        }
        .back-btn{
            padding:10px 35px;
            background: linear-gradient(90deg, #6c757d, #5a6268);
            color:#fff;
            border:none;border-radius:8px;
            cursor:pointer;
            text-decoration: none;
            font-size:15px;
            font-weight:500;
            transition: all 0.2s ease;
        }
        .back-btn:hover{
            background: linear-gradient(90deg, #5a6268, #343a40);
            box-shadow:0 2px 6px rgba(108,117,125,0.2);
        }
    </style>
</head>
<body>
<div class="form-box">
    <h2>修改商品信息</h2>
    <form action="${pageContext.request.contextPath}/goods?action=edit" method="post">
        <input type="hidden" name="gid" value="${goods.gid}"/>
        <div class="form-item">
            <label>商品ID：</label>
            <input type="text" value="${goods.gid}" class="disabled-input" disabled/>
        </div>
        <div class="form-item">
            <label>商品名称：</label>
            <input type="text" name="gname" value="${goods.gname}" required placeholder="请输入商品名称"/>
        </div>
        <div class="form-item">
            <label>商品价格：</label>
            <input type="number" step="0.01" name="price" value="${goods.price}" required placeholder="请输入商品价格，支持两位小数"/>
        </div>
        <div class="form-item">
            <label>商品库存：</label>
            <input type="number" name="stock" value="${goods.stock}" required placeholder="请输入商品库存数量"/>
        </div>
        <div class="form-item">
            <label>商品图片：</label>
            <input type="text" name="gimg" value="${goods.gimg}" placeholder="填写goods_img里的图片名，如：riize徽章.jpg"/>
        </div>
        <div class="form-item">
            <label>商品描述：</label>
            <textarea name="gdesc" placeholder="请输入商品详细描述信息">${goods.gdesc}</textarea>
        </div>
        <div class="btn-group">
            <button type="submit" class="submit-btn">✅ 保存修改</button>
            <a href="${pageContext.request.contextPath}/goods?action=manage" class="back-btn">← 返回商品管理</a>
        </div>
    </form>
</div>
</body>
</html>