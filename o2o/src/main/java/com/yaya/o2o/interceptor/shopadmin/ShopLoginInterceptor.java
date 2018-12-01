package com.yaya.o2o.interceptor.shopadmin;

import com.yaya.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中取出用户信息来
        Object userObj = request.getSession().getAttribute("user");
        if(userObj != null) {
            //若用户信息不为空则将session里的用户信息转换成PersonInfo实体类对象
            PersonInfo user = (PersonInfo)userObj;
            //做空值判断,确保userId不为空且帐号的可用状态为1
            if(user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1) {
                //若通过验证则返回true,拦截器返回true之后,用户接下的操作得以正常执行
                return true;
            }
        }
        //若不满足验证,则直接跳到登录页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open('" + request.getContextPath() + "/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }
}
