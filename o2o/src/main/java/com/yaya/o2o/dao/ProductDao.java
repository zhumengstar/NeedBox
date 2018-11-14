package com.yaya.o2o.dao;

import com.yaya.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    //查询商品列表并分页,可输入的条件有:商品名(模糊),商品状态,店铺Id,商品类别
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
    @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    //查询对应的商品总数
    int queryProductCount(@Param("productCondition") Product productCondition);

    //通过productId查询唯一的商品信息
    Product queryProductById(long productId);

    //插入商品
    int insertProduct(Product product);

    //更新商品信息
    int updateProduct(Product product);
}
