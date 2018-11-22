package com.yaya.o2o.exceptions;

//之所以继承RuntimeException(非受检异常),是希望程序停止
//因为在运行时出现了无法继续运算的情况,希望停止程序后,对代码修正
public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = 4871604422223393329L;

    public ShopOperationException(String msg) {
        super(msg);
    }

}
