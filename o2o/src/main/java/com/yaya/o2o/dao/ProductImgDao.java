package com.yaya.o2o.dao;

import com.yaya.o2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {

    List<ProductImg> queryProductImgList(long productId);

    //批量添加商品详情图片
    int batchInsertProductImg(List<ProductImg> productImgList);

    //删除指定商品下的所有详情图
    int deleteProductImgByProductId(long productId);
}
