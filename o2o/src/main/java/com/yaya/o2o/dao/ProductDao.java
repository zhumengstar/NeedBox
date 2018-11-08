package com.yaya.o2o.dao;

import com.yaya.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    //查询商品列表并分页,可输入的条件有:商品名(模糊),商品状态,店铺Id,商品类别
    List<Product> queryProductList(@Param("productCondition")Product productCondition, @Param("pageSize")int pageSize);

    //插入商品
    int insertProduct(Product product);
}
