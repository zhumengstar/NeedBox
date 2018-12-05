package com.yaya.o2o.interceptor;

import com.yaya.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //从session中获取当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
        //非空判断
        if(currentShop != null && shopList != null) {
            //遍历可操作的店铺列表
            for (Shop shop : shopList) {
                //如果当前店铺在可操作的列表里返回true,进行接下来的用户操作
                if(shop.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
