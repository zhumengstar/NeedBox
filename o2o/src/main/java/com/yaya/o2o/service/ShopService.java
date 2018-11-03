package com.yaya.o2o.service;

import com.yaya.o2o.dto.ShopExecution;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {

    //根据shopCondition分页返回相应店铺列表
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    //根据店铺Id获取店铺信息
    Shop getByShopId(long shopId);

    //更新店铺信息
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;

    //注册店铺信息,包括图片处理
    ShopExecution addShop(Shop shop, InputStream shopImgInputSream, String fileName) throws ShopOperationException;
}
