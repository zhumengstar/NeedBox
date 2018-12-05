package com.yaya.o2o.service.impl;

import com.yaya.o2o.dao.ShopDao;
import com.yaya.o2o.dto.ImageHolder;
import com.yaya.o2o.dto.ShopExecution;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.enums.ShopStateEnum;
import com.yaya.o2o.exceptions.ShopOperationException;
import com.yaya.o2o.service.ShopService;
import com.yaya.o2o.util.ImageUtil;
import com.yaya.o2o.util.PageCalculator;
import com.yaya.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        try {
            //给店铺信息初始值
            shop.setEnableStatus(1);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum <= 0) {
                //添加失败,抛异常
                throw new ShopOperationException("创建店铺失败");
            } else {
                //添加成功,存储图片
                if (thumbnail == null || thumbnail.getImage() == null || thumbnail.getImageName() == null || thumbnail.getImageName().equals("")) {
                    throw new ShopOperationException("获取图片失败");
                }
                try {
                    addShopImg(shop, thumbnail);
                } catch (Exception e) {
                    throw new ShopOperationException("添加图片失败");
                }
                //添加店铺的图片地址更新店铺信息
                effectedNum = shopDao.updateShop(shop);
                if(effectedNum <= 0) {
                    throw new ShopOperationException("更新图片地址失败");
                }
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        try {
            //1.判断是否需要处理图片 thumbnail为空不处理
            if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null && !thumbnail.getImageName().equals("")) {
                try {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);
                } catch (Exception e) {
                    throw new ShopOperationException("处理图片失败");
                }
            }
            //2.更新店铺信息
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("更新店铺信息失败");
            }
            return new ShopExecution(ShopStateEnum.SUCCESS, shop);
        } catch (Exception e) {
            throw new ShopOperationException("modifyShop error:" + e.getMessage());
        }
    }

    //添加店铺缩略图
    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        //获取shop图片目录的子路径,即/upload/item/shop/+shopId
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        //添加图片水印===>处理为缩略图,返回缩略图目录(相对)
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        //添加更新后的图片地址到Shop
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if(shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }
}
