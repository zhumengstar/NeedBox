package com.yaya.o2o.service;

import com.yaya.o2o.dto.ShopExecution;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputSream, String fileName) throws ShopOperationException;
}
