package com.yaya.o2o.service;

import com.yaya.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    //根据查询条件获取ShopCategory列表
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
