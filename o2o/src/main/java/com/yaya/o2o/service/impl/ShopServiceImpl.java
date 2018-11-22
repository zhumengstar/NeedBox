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
    @Transactional//抛出RuntimeException后,事务会回滚,实质是使用了JDBC的事务来进行事务控制的
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        //空值判断
        if(shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum <= 0) {
                //添加失败,抛异常
                throw new ShopOperationException("店铺创建失败");
            } else {
                if(thumbnail.getImage() != null) {
                    //添加成功,存储图片
                    try {
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    //添加店铺的图片地址更新店铺信息
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

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
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        //首先判空
        if(shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                //1.判断是否需要处理图片
                if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null && !thumbnail.getImageName().equals("")) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error:" + e.getMessage());
            }
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //将pageIndex转换成rowIndex
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        //返回shopList列表
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        //返回店铺总数
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
