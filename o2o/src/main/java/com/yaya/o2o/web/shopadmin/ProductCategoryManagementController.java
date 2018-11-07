package com.yaya.o2o.web.shopadmin;

import com.yaya.o2o.dto.Result;
import com.yaya.o2o.entity.ProductCategory;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.enums.ProductCategoryStateEnum;
import com.yaya.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist", method = GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        Shop shop = new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop", shop);

        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if(currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
        }
    }

}
