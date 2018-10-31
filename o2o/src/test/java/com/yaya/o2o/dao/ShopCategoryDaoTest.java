package com.yaya.o2o.dao;

import com.yaya.o2o.BaseTest;
import com.yaya.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {
        // shopCategoryCondition不为null的情况, 查询parent_id is not null 的数据
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
        assertEquals(3, shopCategoryList.size());

        // shopCategoryCondition.parent 不为null的情况, 查询parent=1的店铺目录
        ShopCategory child = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(1L);
        child.setParent(parent);
        shopCategoryList = shopCategoryDao.queryShopCategory(child);
        assertEquals(2, shopCategoryList.size());
//        System.out.println(shopCategoryList.get(0).getShopCategoryName());
//        System.out.println(shopCategoryList.get(1).getShopCategoryName());
    }
}
