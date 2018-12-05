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

    @RequestMapping(value = "/registerlocal", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> register(HttpServletRequest request) throws LocalAuthOperationException {
          Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String localAuthStr = HttpServletRequestUtil.getString(request, "localAuthStr");
        ObjectMapper mapper = new ObjectMapper();
        LocalAuth localAuth = null;
        try {
            localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (localAuth == null || localAuth.getUsername() == null || localAuth.getUsername().equals("") || localAuth.getPassword() == null || localAuth.getPassword().equals("") || localAuth.getPersonInfo().getName() == null || localAuth.getPersonInfo().getName().equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入注册信息");
            return modelMap;
        }
        CommonsMultipartFile profileImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                profileImg = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (profileImg == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请上传头像");
            return modelMap;
        }
        try {
            LocalAuthExecution le = localAuthService.register(localAuth, profileImg);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", le.getStateInfo());
            }
        } catch (LocalAuthOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        return modelMap;
    }

    //将用户信息与平台帐号绑定
    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(user == null || user.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "当前用户信息失效，请退出系统后重试");
            return modelMap;
        }
        if(username == null || username.equals("") || password == null || password.equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入绑定信息");
            return modelMap;
        }
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setPersonInfo(user);
        try {
            LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
            if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", le.getStateInfo());
            }
        } catch (LocalAuthOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/changelocalpwd", method = POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if(user == null || user.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "当前用户信息失效，请退出系统后重试");
            return modelMap;
        }
        if(username == null || username.equals("") || password == null || password.equals("") || newPassword == null || newPassword.equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入修改信息");
            return modelMap;
        }
        try {
            LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
            if (localAuth == null || !localAuth.getUsername().equals(username)) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "请输入当前登录帐号");
                return modelMap;
            }
            LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newPassword);
            if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "修改密码失败");
            }
        } catch (LocalAuthOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
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
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        if(username == null || username.equals("") || password == null || password.equals("")) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入登录信息");
            return modelMap;
        }
        try {
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
            if(localAuth != null) {
                modelMap.put("success", true);
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } catch (LocalAuthOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
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
