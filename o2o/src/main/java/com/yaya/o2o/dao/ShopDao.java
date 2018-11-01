package com.yaya.o2o.dao;

import com.yaya.o2o.entity.Shop;

public interface ShopDao {

    //通过shop id查询店铺
    Shop queryByShopId(long shopId);
    //新增店铺
    int insertShop(Shop shop);
    //更新店铺信息
    int updateShop(Shop shop);

}
