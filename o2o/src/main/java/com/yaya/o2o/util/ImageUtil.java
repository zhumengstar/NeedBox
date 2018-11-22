package com.yaya.o2o.util;

import com.yaya.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
        private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    //生成缩略图
    //参数:图片文件,保存路径
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        //1.目标目录,不存在创建,即/upload/item/shop/+shopId
        makeDirPath(targetAddr);

        //2.为了防止图片重名,不采用用户上传的文件名,系统内部采用随机命名的方式
        //随机文件名
        String realFileName = getRandomFileName();

        //3.获取用户上传的文件扩展名,用于拼接新的文件名
        //文件扩展名
        String extension = getFileExtension(thumbnail.getImageName());

        //4.拼接新的文件名
        //相对路径===目标路径+随机文件名+扩展名,即/upload/item/shop/shopId/****.***
        String relativeAddr = targetAddr + realFileName + extension;
        //目标文件===根路径+相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        //5.给源文件加水印后输出到目标文件
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)//图片大小
                    //添加水印
                    .watermark(Positions.BOTTOM_RIGHT,//水印位置---右下
                            //水印路径,水印透明度
                            ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
                    //图片压缩80%
                    .outputQuality(0.8f)
                    //输出到目标文件
                    .toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回相对路径
        return relativeAddr;
    }

    //处理详情图,并返回新生成图片的相对值路径
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        //1.目标目录,不存在创建,即/upload/item/shop/+shopId
        makeDirPath(targetAddr);

        //2.为了防止图片重名,不采用用户上传的文件名,系统内部采用随机命名的方式
        //随机文件名
        String realFileName = getRandomFileName();

        //3.获取用户上传的文件扩展名,用于拼接新的文件名
        //文件扩展名
        String extension = getFileExtension(thumbnail.getImageName());

        //4.拼接新的文件名
        //相对路径===目标路径+随机文件名+扩展名,即/upload/item/shop/shopId/****.***
        String relativeAddr = targetAddr + realFileName + extension;
        //目标文件===根路径+相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        //5.给源文件加水印后输出到目标文件
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)//图片大小
                    //添加水印
                    .watermark(Positions.BOTTOM_RIGHT,//水印位置---右下
                            //水印路径,水印透明度
                            ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
                    //图片压缩80%
                    .outputQuality(0.9f)
                    //输出到目标文件
                    .toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回相对路径
        return relativeAddr;
    }


    //生成随机文件名,当前年月日小时分钟秒+五位随机数
    public static String getRandomFileName() {
        int rn = r.nextInt(89999) + 10000; //10000-99999
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rn;
    }

    //获取输入文件流的扩展名
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //创建目标路径所涉及到的目录
    public static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath();
        File dirPath = new File(realFileParentPath + targetAddr);
        if(!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }


    //storePath是文件路径还是目录路径
    //文件则删除该文件
    //目录则删除目录下所有的文件
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if(fileOrPath.exists()) {
            if(fileOrPath.isDirectory()) {
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(basePath);
        Thumbnails.of(new File("/home/hehanyue/image/xiaohuangren.jpeg"))
                .size(200, 200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f).outputQuality(0.8f)
                .toFile("/home/hehanyue/image/xiaohuangrennew.jpeg");
    }
}
