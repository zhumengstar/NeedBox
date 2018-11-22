package com.yaya.o2o.util;


public class PageCalculator {
    //将pageIndex转换成rowIndex
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0)?(pageIndex-1)*pageSize:0;
        //如果pageSize为5,一页有5条数据的话
        //如果页数为1 则应从第0条开始选取
        //如果页数为2 则应从第5条开始选取,即第一页是第0-4条
    }
}
