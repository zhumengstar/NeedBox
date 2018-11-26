package com.yaya.o2o.web.wechat;

import com.yaya.o2o.dto.UserAccessToken;
import com.yaya.o2o.dto.WechatUser;
import com.yaya.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

    @RequestMapping(value = "/logincheck", method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        //获取微信公众号传输过来的code,通过code可以获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        //这个state可以用来传我们自定义的信息,方便程序调用,这里也可以不用
        //String roleType = request.getParameter("state");
        log.debug("weixin login code: " + code);
        WechatUser user = null;
        String openId = null;
        if(code != null) {
            UserAccessToken token;
            try {
                //通过code获取access_token
                token = WechatUtil.getUserAccessToken(coed);
                log.debug("weixin login token:" + token.toString());
                //通过token获取accessToken
                String accessToken = token.getAccessToken();
                //通过token获取openId
                openId = token.getOpenId();
                //通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId:" + e.toString());
                e.printStackTrace();
            }
        }

        //前面获取到了openId后,可以通过它去数据库判断该微信账号是否在我们网站里有对应的帐号了
        //没有的话这里可以自动创建,直接实现微信与网站的无缝对接
        if(user != null) {
            //获取到微信验证的信息后返回到指定的路由(需要自己设定)
            return "/frontend/index";
        } else {
            return null;
        }
    }

}
