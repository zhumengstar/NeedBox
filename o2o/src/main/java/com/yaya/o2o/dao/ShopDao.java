package com.yaya.o2o.dao;

import com.yaya.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    //新增店铺
    int insertShop(Shop shop);

    //更新店铺信息
    int updateShop(Shop shop);

    //通过shopId查询店铺信息
    Shop queryByShopId(long shopId);

    //分页查询店铺
    //可输入的条件:店铺名(模糊),店铺状态,店铺类别,区域ID,owner
    //rowIndex从第几行开始取数据
    //pageSize返回的条数
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex,
                             @Param("pageSize")int pageSize);

    //返回queryList总数
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
