package com.yaya.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
    //与接口配置信息中的Token一致
    private static String token = "myo2o";

    //验证签名
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        //将token,timestamp,nonce三个参数进行字典序排序
        Arrays.sort(arr);
        //content为三个参数的字典序拼接字符串
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            //加密对象获取到SHA1实例
            md = MessageDigest.getInstance("SHA-1");
            //将拼接字符串进行SHA1加密
            byte[] digest = md.digest(content.toString().getBytes());
            //将加密后的字节数组转为十六进制字符串
            tmpStr = bytesToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //将sha1加密后的字符串与signature的大写形态对比,标识该请求来源于微信
        //通过校验返回true
        if(tmpStr != null) {
            return tmpStr.equals(signature.toUpperCase());
        } else {
            return false;
        }
    }


    /**
     * 解释为什么要与0XFF?
     *
     * 因为从byte类型转为int类型的时候,即8位扩充到32位,是根据符号位在高位扩充,0则补0,1则补1
     * 举例来说,一个byte的-1(0XFF),会被转换成int的-1(0XFFFFFFFF),那么转化出的结果就不是我们想要的了
     * 所以为了保证二进制数据的一致性,我们可以这个将这个int值和0XFF进行与运算,将高24位清0,保留低8位
     * 这样结果就是我们想要的值
     */
    //将字节数组转换为十六进制字符串
    private static String bytesToHexString(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < byteArray.length; i++) {
            int b = byteArray[i] & 0XFF;
            String hexb = Integer.toHexString(b);
            if(hexb.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hexb);
        }
        return stringBuilder.toString();
    }


}
