package com.yaya.o2o.util;

import javax.servlet.http.HttpServletRequest;

//处理request里的参数
public class HttpServletRequestUtil {
    //从request里提取key,将key转换成整型
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.parseInt(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }
    //长整型
    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }
    }
    //double
    public static double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1D;
        }
    }
    //布尔
    public static boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }
    //String
    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            //如果从request获取到的字符是iso-8859-1格式的，就转为utf-8
            if(result.equals(new String(result.getBytes("iso-8859-1"), "iso-8859-1"))) {
                result = new String(result.getBytes("iso-8859-1"),"utf-8");
            }
            //判断request里属性值是否为空
            if(result != null) {
                //不为空就trim处理
                result = result.trim();
            }
            //判断是否为空字符串
            if(result.equals("")) {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
