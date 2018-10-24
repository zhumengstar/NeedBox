package com.yaya.o2o.service;

import com.yaya.o2o.dto.ShopExecution;
import com.yaya.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    ShopExecution addShop(Shop shop,File shopImg);
}
