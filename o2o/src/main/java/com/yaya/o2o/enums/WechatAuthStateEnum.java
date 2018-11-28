package com.yaya.o2o.enums;

public enum WechatAuthStateEnum {
    //默认为public static final
    LOGINFAIL(-1, "open输入有误"),
    SUCCESS(0, "操作成功"),
    NULL_AUTH_INFO(-1003, "wechatauth信息为空");

    private int state;

    private String stateInfo;


    private WechatAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    //根据传入的state返回相应的enum
    public static WechatAuthStateEnum stateOf(int state) {
        //提供values()方法,遍历所有枚举值
        for(WechatAuthStateEnum stateEnum : values()) {
            if(stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }
}
