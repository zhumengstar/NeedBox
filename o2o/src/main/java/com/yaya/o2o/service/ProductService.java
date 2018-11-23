package com.yaya.o2o.service;

import com.yaya.o2o.dto.ImageHolder;
import com.yaya.o2o.dto.ProductExecution;
import com.yaya.o2o.entity.Product;
import com.yaya.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {

    //添加商品信息以及图片处理
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

    //通过商品ID查询唯一的商品信息
    Product getProductById(long productId);

    //修改商品信息以及图片处理
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

    //获取商品列表
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

}
