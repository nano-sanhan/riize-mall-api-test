package org.example.shoppingstore.servlet;

import org.example.shoppingstore.dao.GoodsDao;
import org.example.shoppingstore.entity.Goods;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/goods")
public class GoodsServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 统一设置请求参数编码为UTF-8，解决中文参数乱码问题（仅对POST请求体有效）
        req.setCharacterEncoding("utf-8");

        // 获取前端传递的action参数，区分具体操作类型
        // 示例：goods?action=list → 商品列表；goods?action=add → 新增商品
        String action = req.getParameter("action");

        // 创建GoodsDao对象，调用商品数据库操作方法
        GoodsDao dao = new GoodsDao();

        // ========== 分支1：前台-商品列表展示（action=list） ==========
        if ("list".equals(action)) {
            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：查询所有商品列表
            // 归属：应抽离到 GoodsService.findAllGoods() 方法中
            // 业务规则：无额外校验，仅查询所有商品（实际应支持分页、筛选）
            List<Goods> goodsList = dao.findAll();
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：将商品列表存入请求域，供首页（index.jsp）渲染
            req.setAttribute("goodsList", goodsList);
            // 控制层代码：请求转发到首页，展示商品列表
            req.getRequestDispatcher("index.jsp").forward(req, resp);

            // ========== 分支2：前台-商品详情查看（action=detail） ==========
        } else if ("detail".equals(action)) {
            // 控制层代码：解析前端传递的商品ID参数（gid）
            int gid = Integer.parseInt(req.getParameter("gid"));

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：根据商品ID查询商品详情
            // 归属：应抽离到 GoodsService.getGoodsDetail(int gid) 方法中
            // 业务规则：仅查询单条商品记录（实际应校验商品是否上架、是否有库存）
            Goods goods = dao.findById(gid);
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：将商品详情存入请求域，供详情页渲染
            req.setAttribute("goods", goods);
            // 控制层代码：转发到商品详情页
            req.getRequestDispatcher("goods_detail.jsp").forward(req, resp);

            // ========== 分支3：后台-跳转到新增商品页面（action=toAdd） ==========
        } else if ("toAdd".equals(action)) {
            // 控制层代码：无业务逻辑，仅转发到后台新增商品表单页
            req.getRequestDispatcher("admin/goods_add.jsp").forward(req, resp);

            // ========== 分支4：后台-新增商品（action=add） ==========
        } else if ("add".equals(action)) {
            // 控制层代码：解析前端表单提交的商品参数
            String gname = req.getParameter("gname");          // 商品名称
            // 控制层代码：价格转换：前端传递String类型，转为BigDecimal避免浮点精度丢失
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            int stock = Integer.parseInt(req.getParameter("stock")); // 库存
            String gdesc = req.getParameter("gdesc");          // 商品描述
            String gimg = req.getParameter("gimg");            // 【新增】商品图片名称/路径

            // 控制层代码：封装Goods实体对象
            Goods goods = new Goods();
            goods.setGname(gname);
            goods.setPrice(price);
            goods.setStock(stock);
            goods.setGdesc(gdesc);
            goods.setGimg(gimg); // 【核心】设置商品图片字段

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：新增商品到数据库
            // 归属：应抽离到 GoodsService.addGoods(Goods goods) 方法中
            // 业务规则：仅入库（实际应校验商品名称唯一、价格/库存为正数、图片路径合法）
            dao.addGoods(goods);
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：重定向到商品管理列表页（避免表单重复提交）
            resp.sendRedirect("goods?action=manage");

            // ========== 分支5：后台-商品管理列表展示（action=manage） ==========
        } else if ("manage".equals(action)) {
            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：查询所有商品用于后台管理
            // 归属：应抽离到 GoodsService.findAllGoods() 方法中（与前台列表复用）
            // 业务规则：无额外校验（实际应区分管理员权限、支持分页）
            List<Goods> goodsList = dao.findAll();
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：将商品列表存入请求域，供后台管理页渲染
            req.setAttribute("goodsList", goodsList);
            // 控制层代码：转发到后台商品管理页
            req.getRequestDispatcher("admin/goods_manage.jsp").forward(req, resp);

            // ========== 分支6：后台-跳转到编辑商品页面（action=toEdit） ==========
        } else if ("toEdit".equals(action)) {
            // 控制层代码：解析要编辑的商品ID
            int gid = Integer.parseInt(req.getParameter("gid"));

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：查询商品原始信息用于编辑回显
            // 归属：应抽离到 GoodsService.getGoodsDetail(int gid) 方法中
            // 业务规则：仅查询（实际应校验管理员是否有权限编辑该商品）
            Goods goods = dao.findById(gid);
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：将商品信息存入请求域，供编辑页回显
            req.setAttribute("goods", goods);
            // 控制层代码：转发到后台商品编辑页
            req.getRequestDispatcher("admin/goods_edit.jsp").forward(req, resp);

            // ========== 分支7：后台-编辑商品（action=edit） ==========
        } else if ("edit".equals(action)) {
            // 控制层代码：解析前端提交的编辑参数（包含商品主键gid）
            int gid = Integer.parseInt(req.getParameter("gid"));     // 商品主键（必传，定位要修改的商品）
            String gname = req.getParameter("gname");                // 新商品名称
            BigDecimal price = new BigDecimal(req.getParameter("price")); // 新价格
            int stock = Integer.parseInt(req.getParameter("stock")); // 新库存
            String gdesc = req.getParameter("gdesc");                // 新描述
            String gimg = req.getParameter("gimg");                  // 【新增】新商品图片名称/路径

            // 控制层代码：封装修改后的Goods对象（必须包含gid主键）
            Goods goods = new Goods();
            goods.setGid(gid);          // 主键，定位修改记录
            goods.setGname(gname);
            goods.setPrice(price);
            goods.setStock(stock);
            goods.setGdesc(gdesc);
            goods.setGimg(gimg);       // 【核心】设置修改后的图片字段

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：更新商品信息
            // 归属：应抽离到 GoodsService.updateGoods(Goods goods) 方法中
            // 业务规则：仅更新（实际应校验商品归属、价格/库存合法性）
            dao.updateGoods(goods);
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：重定向到商品管理列表页，刷新数据
            resp.sendRedirect("goods?action=manage");

            // ========== 分支8：后台-删除商品（action=delete） ==========
        } else if ("delete".equals(action)) {
            // 控制层代码：解析要删除的商品ID
            int gid = Integer.parseInt(req.getParameter("gid"));

            // ==================== 【逻辑业务层代码开始】====================
            // 核心业务逻辑：删除指定商品
            // 归属：应抽离到 GoodsService.deleteGoods(int gid) 方法中
            // 业务规则：仅物理删除（实际应做逻辑删除、校验是否有未完成订单）
            dao.deleteGoods(gid);
            // ==================== 【逻辑业务层代码结束】====================

            // 控制层代码：重定向到商品管理列表页，刷新数据
            resp.sendRedirect("goods?action=manage");
        }
        // 补充：若action参数为空/不匹配，Servlet无响应，前端返回空白页
    }

    /**
     * 处理所有POST类型的商品请求
     * 设计思路：POST请求逻辑与GET完全一致，直接调用doGet，避免重复编码
     * 应用场景：前端表单提交（如新增/编辑商品）使用POST方式时触发
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}