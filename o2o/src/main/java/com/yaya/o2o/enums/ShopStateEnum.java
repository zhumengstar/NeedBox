package com.yaya.o2o.enums;

public enum ShopStateEnum {

    SUCCESS(1,"操作成功"), INNER_ERROR(0, "错误"), CHECK(-1, "审核");

    private int state;

    private String stateInfo;

    //私有构造器,阻止对象的生成(不声明private,则默认使用private修饰)
    private ShopStateEnum(int state, String stateInfo) {
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
    public static ShopStateEnum stateOf(int state) {
        //提供values()方法,遍历所有枚举值
        for(ShopStateEnum stateEnum : values()) {
            if(stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }
}
