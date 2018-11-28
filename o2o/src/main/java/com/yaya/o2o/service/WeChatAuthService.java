package com.yaya.o2o.service;

import com.yaya.o2o.dto.WechatAuthExecution;
import com.yaya.o2o.entity.WechatAuth;
import com.yaya.o2o.exceptions.WechatAuthOperationException;

public interface WeChatAuthService {

    //通过openId查找平台对应的微信账号
    WechatAuth getWechatAuthByOpenId(String openId);

    //注册平台的微信帐号
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;


}
