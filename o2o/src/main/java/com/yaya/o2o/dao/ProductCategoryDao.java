package com.yaya.o2o.dao;

import com.yaya.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {

    //通过shopid查询商品类别列表
    List<ProductCategory> queryProductCategoryList(long shopId);

    //批量新增商品类别
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    //删除商品类别
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId, @Param("shopId")long shopId);

}
