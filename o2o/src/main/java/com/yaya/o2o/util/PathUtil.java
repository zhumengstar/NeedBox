package com.yaya.o2o.util;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")) {
            basePath = "D:/o2o/image/";
        } else {
            basePath = "/home/hehanyue/image/";
        }
        return basePath.replace("/" , seperator);;
    }
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        int aaa = 1;
        return imagePath.replace("/", seperator);
    }}}}}}}}}}}}}}}}}}}}}}}
}
