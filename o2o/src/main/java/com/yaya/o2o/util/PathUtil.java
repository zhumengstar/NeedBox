package com.yaya.o2o.util;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");
    //图片经过处理后的保存路径---根路径
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")) {
            basePath = "D:/o2o/imagesave";
        } else {
            basePath = "/home/hehanyue/imagesave";
        }
        return basePath.replace("/" , seperator);
    }
    //图片经过处理后的保存路径---子路径
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

}
