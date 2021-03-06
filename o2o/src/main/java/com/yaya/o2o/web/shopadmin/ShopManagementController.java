package com.yaya.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaya.o2o.dto.ImageHolder;
import com.yaya.o2o.dto.ShopExecution;
import com.yaya.o2o.entity.Area;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.entity.Shop;
import com.yaya.o2o.entity.ShopCategory;
import com.yaya.o2o.enums.ShopStateEnum;
import com.yaya.o2o.exceptions.ShopOperationException;
import com.yaya.o2o.service.AreaService;
import com.yaya.o2o.service.ShopCategoryService;
import com.yaya.o2o.service.ShopService;
import com.yaya.o2o.util.CodeUtil;
import com.yaya.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 店家管理的相关Controller
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;


    @RequestMapping(value = "/getshopinitinfo", method = GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    //注册店铺
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            //put进去是否成功和错误信息
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (shop == null || shop.getShopName() == null || shop.getShopName().equals("") || shop.getShopAddr() == null || shop.getShopAddr().equals("") || shop.getPhone() == null || shop.getPhone().equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if(shopImg == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请上传店铺缩略图");
            return modelMap;
        }
        PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
        shop.setOwner(owner);
//            File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
//            try {
                //创建空白文件
//                shopImgFile.createNewFile();
//            } catch (IOException e) {
//                modelMap.put("success", false);
//                modelMap.put("errMsg", e.getMessage());
//                return modelMap;
//            }
//            try {
            //给空白文件写入传送过来的文件流
//                inputStreamToFile(shopImg.getInputStream(), shopImgFile);
//            } catch (IOException e) {
//                modelMap.put("success", false);
//                modelMap.put("errMsg", e.getMessage());
//                return modelMap;
//            }
        try {
            ShopExecution se = null;
            ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
            se = shopService.addShop(shop, imageHolder);
            if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                //用户可操作店铺列表
                //当注册操作成功,将店铺添加到当前session owner的店铺列表中
                List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                if(shopList == null || shopList.size() == 0) {
                    shopList = new ArrayList<>();
                }
                shopList.add(se.getShop());
                request.getSession().setAttribute("shopList", shopList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
        } catch (ShopOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if(shop == null || shop.getShopId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "没有获取到店铺信息");
            return modelMap;
        }
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        if(shop.getShopId() != currentShop.getShopId()) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "该店铺非当前用户可操作店铺");
            return modelMap;
        }
        if (shop.getShopName() == null || shop.getShopName().equals("") || shop.getShopAddr() == null || shop.getShopAddr().equals("") || shop.getPhone() == null || shop.getPhone().equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        try {
            ShopExecution se = null;
            //shopImg为空,不修改图片
            if(shopImg == null) {
                se = shopService.modifyShop(shop, null);
            } else {
                //修改图片
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                se = shopService.modifyShop(shop, imageHolder);
            }
            //修改成功
            if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
        } catch (ShopOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    //店铺列表
    @RequestMapping(value = "/getshoplist", method = GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //通过session获取用户信息
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
            request.getSession().setAttribute("shopList", se.getShopList());
            modelMap.put("shopList", se.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    //店铺管理
    @RequestMapping(value = "/getshopmanagementinfo", method = GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取店铺ID
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //如果前端没有传来shopId
        if(shopId <= 0) {
            //从session中获取
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            //还是获取不到,重定向到之前的店铺列表页面
            if(currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirct", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid", method = GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        try {
            if (shopId > 0) {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "shopId不合法");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
//将InputStream转换为File类型
//    private static void inputStreamToFile(InputStream ins, File file) {
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while((bytesRead = ins.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("调用inputStreamToFile产生异常:" + e.getMessage());
//        } finally {
//            try {
//                if(os != null) {
//                    os.close();
//                }
//                if(ins != null) {
//                    ins.close();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("inputStreamToFile关闭io产生异常:" + e.getMessage());
//            }
//        }
//    }
//}
