package com.yaya.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImgUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddr) {
        //1.为了防止图片重名，不采用用户上传的文件名，系统内部采用随机命名的方式
        String realFileName = getRandomFileName();
        //2.获取用户上传的文件扩展名，用于拼接新的文件名
        String extension = getFileExtension(thumbnail);
        //3.目标目录，不存在创建
        makeDirPath(targetAddr);
        //4.拼接新的文件名
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        //5.给源文件加水印后输出到目标文件
        try {
            Thumbnails.of(thumbnail.getInputStream()).size(200, 200)
            .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }
    //生成随机文件名，当前年月日小时分钟秒+五位随机数
    public static String getRandomFileName() {
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }
    //获取输入文件流的扩展名
    public static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFileName = cFile.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    //创建目标路径所涉及到的目录，
    public static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath();
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }
    public static void main(String[] args) throws IOException {
        System.out.println(basePath);
        Thumbnails.of(new File("/home/hehanyue/image/xiaohuangren.jpeg"))
                .size(200, 200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f).outputQuality(0.8f)
                .toFile("/home/hehanyue/image/xiaohuangrennew.jpeg");
    }
}
