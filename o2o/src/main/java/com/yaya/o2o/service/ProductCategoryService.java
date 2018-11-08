package com.yaya.o2o.service;

import com.yaya.o2o.dto.ProductCategoryExecution;
import com.yaya.o2o.entity.ProductCategory;
import com.yaya.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    //查询指定某个店铺下的所有商品类别信息
    List<ProductCategory> getProductCategoryList(long shopId);

    //批量添加
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    //将此类别下的商品里的类别id置为空,再删除掉该商品类别
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
