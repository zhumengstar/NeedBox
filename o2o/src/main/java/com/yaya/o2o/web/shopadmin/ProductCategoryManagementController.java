package com.yaya.o2o.web.shopadmin;

import com.yaya.o2o.dto.ProductCategoryExecution;
import com.yaya.o2o.entity.ProductCategory;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.enums.ProductCategoryStateEnum;
import com.yaya.o2o.exceptions.ProductCategoryOperationException;
import com.yaya.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

//    @RequestMapping(value = "/getproductcategorylist", method = GET)
//    @ResponseBody
//    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
//        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
//        List<ProductCategory> list = null;
//        if(currentShop != null && currentShop.getShopId() > 0) {
//            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
//            return new Result<List<ProductCategory>>(true, list);
//        } else {
//            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
//            return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
//        }
//    }
    @RequestMapping(value = "/getproductcategorylist", method = GET)
    @ResponseBody
    private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if(currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            modelMap.put("success", true);
            modelMap.put("productCategoryList", list);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            modelMap.put("success", false);
            modelMap.put("errCode", ps.getState());
            modelMap.put("errMsg", ps.getStateInfo());
        }
        return modelMap;
    }

    @RequestMapping(value = "/addproductcategorys", method = POST)
    @ResponseBody
    //@RequestBody 将HTTP请求正文写入某个对象
    private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //遍历这个批量的商品类别,逐一设置shopId
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());
        }
        if(productCategoryList != null || productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeproductcategory", method = POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if(productCategoryId != null || productCategoryId > 0 ) {
            try {
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }

}
