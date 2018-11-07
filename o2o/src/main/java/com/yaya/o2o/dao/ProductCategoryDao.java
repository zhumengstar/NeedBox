package com.yaya.o2o.dao;

import com.yaya.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    List<ProductCategory> queryProductCategoryList(long shopId);

    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
}
