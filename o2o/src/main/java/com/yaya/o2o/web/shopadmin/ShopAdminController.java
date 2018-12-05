package com.yaya.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

//因为页面都是放在WEB-INF下的,不允许外界直接访问
//所以要定义路由
@Controller
@RequestMapping(value = "shopadmin", method = GET)
public class ShopAdminController {
    @RequestMapping(value = "/shoplist")
    private String shopList() {
        //转发至店铺列表页面
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopoperation")
    private String shopOperation() {
        //转发至店铺注册/编辑页面
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shopmanagement")
    private String shopManagement() {
        //转发至店铺管理页面
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productmanagement")
    private String productManagement() {
        //转发至商品管理页面
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement")
    private String productCategoryManagement() {
        //转发至商品类别管理页面
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productoperation")
    private String productOperation() {
        //转发至商品添加/编辑页面
        return "shop/productoperation";
    }
}
