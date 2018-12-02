package com.yaya.o2o.web.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaya.o2o.dto.LocalAuthExecution;
import com.yaya.o2o.entity.LocalAuth;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.enums.LocalAuthStateEnum;
import com.yaya.o2o.exceptions.LocalAuthOperationException;
import com.yaya.o2o.service.LocalAuthService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/local", method = {GET, POST})
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> register(HttpServletRequest request) throws LocalAuthOperationException {
          Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        LocalAuth localAuth = null;
        String localAuthStr = HttpServletRequestUtil.getString(request, "localAuthStr");
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartFile profileImg = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            profileImg = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        try {
            localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (localAuth != null && localAuth.getPassword() != null && localAuth.getUsername() != null) {
            try {
                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                if (user != null && localAuth.getPersonInfo() != null) {
                    localAuth.getPersonInfo().setUserId(user.getUserId());
                }
                LocalAuthExecution le = localAuthService.register(localAuth, profileImg);
                if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入注册信息");
        }
        return modelMap;
    }

    //将用户信息与平台帐号绑定
    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //获取输入的帐号
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(username != null && password != null && user != null ) {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
            if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", le.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "/changelocalpwd", method = POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(username != null && password != null && newPassword != null && user != null && user.getUserId() != null) {
            try {
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                //查看原先帐号,看看与输入的帐号是否一致,buyizhi则认为是非法操作
                if(localAuth == null || !localAuth.getUsername().equals(username)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的帐号非本次登录的帐号");
                    return modelMap;
                }
            //修改平台帐号的用户密码
            LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newPassword);
                if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (LocalAuthOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck", method = POST)
    @ResponseBody
    private Map<String, Object> logincheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if(needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request, "password");
        if(username != null && password != null) {
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
            if(localAuth != null) {
                modelMap.put("success", true);
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    //当用户点击退出系统的时候注销session
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user", null);
        request.getSession().setAttribute("shopList", null);
        request.getSession().setAttribute("currentShop", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
