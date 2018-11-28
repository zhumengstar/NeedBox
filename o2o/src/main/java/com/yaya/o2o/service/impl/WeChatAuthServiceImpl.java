package com.yaya.o2o.service.impl;

import com.yaya.o2o.dao.PersonInfoDao;
import com.yaya.o2o.dao.WechatAuthDao;
import com.yaya.o2o.dto.WechatAuthExecution;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.entity.WechatAuth;
import com.yaya.o2o.enums.WechatAuthStateEnum;
import com.yaya.o2o.exceptions.WechatAuthOperationException;
import com.yaya.o2o.service.WeChatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WeChatAuthServiceImpl implements WeChatAuthService{
    private static Logger log = LoggerFactory.getLogger(WeChatAuthServiceImpl.class);

    @Autowired
    private PersonInfoDao personInfoDao;
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
        if(wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            wechatAuth.setCreateTime(new Date());
            //如果微信帐号里夹带着用户信息并且用户ID为空,则认为该用户第一次使用平台(且通过微信登录)
            //则自动创建用户信息
            if(wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
                try {
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    if(effectedNum <= 0) {
                        throw new WechatAuthOperationException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    log.error("insertPersonInfo error:" + e.toString());
                    throw new WechatAuthOperationException("insertPersonInfo error:" + e.getMessage());
                }
            }
            //创建专属于本平台的微信帐号
            int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
            if(effectedNum <= 0) {
                throw new WechatAuthOperationException("帐号创建失败");
            } else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
            }
        } catch (Exception e) {
            log.error("insertWechatAuth error:" + e.toString());
            throw new WechatAuthOperationException("insertWechatAuth error:" + e.getMessage());
        }
    }

}
