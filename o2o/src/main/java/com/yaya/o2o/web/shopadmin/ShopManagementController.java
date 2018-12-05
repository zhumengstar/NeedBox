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
import java.io.IOException;
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
    //前端传来的request参数 Http请求头中的所有信息都封装在里面,通过这个对象的方法可以获得请求头中的所有信息
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //首先判断验证码是否正确
        if (!CodeUtil.checkVerifyCode(request)) {
            //put进去是否成功和错误信息
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //1.接收并转换相应的参数,包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        //JSON to POJO
        ObjectMapper mapper = new ObjectMapper();
        //去接收的实体类
        Shop shop = null;
        try {
            //接收前端传来的店铺相关的字符串信息,将它转换成shop实体类
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //获取前端传来的文件流,将其接收到shopImg
        CommonsMultipartFile shopImg = null;
        //文件上传解析器来解析request的文件信息
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request里是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //强制转换成HttpServletRequest的子类MultipartHttpServletRequest
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //使用multipartHttpServletRequest的getFile方法得到这个文件流
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
            if(shopImg == null) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } else {
            //没有文件流就报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        //2.注册店铺
        if (shop != null && shopImg != null && shop.getShopName() != null && !shop.getShopName().equals("") && shop.getShopAddr() != null && !shop.getShopAddr().equals("") && shop.getPhone() != null && !shop.getPhone().equals("")) {
            //通过session获取用户登录后的用户信息,并set进owner
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
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder( shopImg.getOriginalFilename(), shopImg.getInputStream());
                se = shopService.addShop(shop, imageHolder);
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
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
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            //shop为空或shopImg为空就报错
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺名称,详细地址和联系电话都要填写哦~");
            return modelMap;
        }
        //3.返回结果
    }


    @RequestMapping(value = "/getshopbyid", method = GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //1.接收并转换相应的参数,包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            //接收前端传来的店铺相关的字符串信息,将它转换成shop实体类
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //获取前端传来的文件流,将其接收到shopImg
        CommonsMultipartFile shopImg = null;
        //文件上传解析器来解析request的文件信息
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request里是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)) {
            //强制转换成HttpServletRequest的子类MultipartHttpServletRequest
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //使用multipartHttpServletRequest的getFile方法得到这个文件流
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2.修改店铺信息
        if (shop != null && shop.getShopId() != null && shop.getShopName() != null && !shop.getShopName().equals("") && shop.getShopAddr() != null && !shop.getShopAddr().equals("") && shop.getPhone() != null && !shop.getPhone().equals("")) {
            ShopExecution se;
            try {
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
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺名称,详细地址和联系电话都要填写哦~");
            return modelMap;
        }
        //3.返回结果
    }

    //店铺列表
    @RequestMapping(value = "/getshoplist", method = GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //通过session获取用户信息
        PersonInfo currentUser = (PersonInfo)request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(currentUser);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", se.getShopList());
            modelMap.put("user", currentUser);
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
