package com.yaya.o2o.service;

import com.yaya.o2o.dto.ImageHolder;
import com.yaya.o2o.dto.ProductExecution;
import com.yaya.o2o.entity.Product;
import com.yaya.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {
    //添加商品信息以及图片处理
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;
}
