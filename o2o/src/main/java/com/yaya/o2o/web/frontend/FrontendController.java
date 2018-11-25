package com.yaya.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/frontend", method = RequestMethod.GET)
public class FrontendController {

    //前端首页
    @RequestMapping("/index")
    private String index() {
        return "frontend/index";
    }

    //店铺类别列表页
    @RequestMapping("/shoplist")
    private String showShopList() {
        return "frontend/shoplist";
    }

    //店铺详情页
    @RequestMapping("/shopdetail")
    private String showShopDetail() {
        return "frontend/shopdetail";
    }

    //店铺详情页
    @RequestMapping("/productdetail")
    private String showProductDetail() {
        return "frontend/productdetail";
    }
}
