package com.yaya.o2o.dto;

//封装json对象,所有返回结果都使用它
public class Result<T> {
    //是否成功标识
    private boolean success;
    //成功时返回的数据
    private T data;
    //错误信息
    private String errMsg;
    private int errorCode;

    public Result() {

    }
    //成功时的构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
    //错误时的构造器
    public Result(boolean success, int errorCode, String errMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
