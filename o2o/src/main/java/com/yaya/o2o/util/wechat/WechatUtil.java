package com.yaya.o2o.util.wechat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaya.o2o.dto.UserAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WechatUtil {

    private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

    //获取UserAccessToken实体类
    public static UserAccessToken getUserAccessToken(String code) throws IOException {
        //测试号信息里的appid
        String appId = "wx299e19661fcdf7d2";
        String appsecret = "a9de3d95950862fb3654604fd836c439";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
        //向相应URL发送请求获取token_json字符串
        String tokenStr = httpRequest(url, "GET", null);
        log.debug("userAccessToken:" + tokenStr);
        UserAccessToken token = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //将json字符串转换成相应对象
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (JsonParseException e) {
            log.error("获取用户accessToken失败:" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户accessToken失败:" + e.getMessage());
        } catch (IOException e) {
            log.error("获取用户accessToken失败:" + e.getMessage());
        }
    }

}
