package com.yaya.o2o.dto;

import com.yaya.o2o.entity.LocalAuth;
import com.yaya.o2o.enums.LocalAuthStateEnum;

import java.util.List;

public class LocalAuthExecution {
    private int state;
    private String stateInfo;
    private int count;
    private LocalAuth localAuth;
    private List<LocalAuth> localAuthList;

    public LocalAuthExecution() {

    }

    //店铺操作失败的时候使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
    //店铺操作成功的时候使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, LocalAuth localAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuth = localAuth;
    }
    //店铺操作成功的时候使用的构造器
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, List<LocalAuth> localAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.localAuthList = localAuthList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth LocalAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> LocalAuthList) {
        this.localAuthList = localAuthList;
    }

}
