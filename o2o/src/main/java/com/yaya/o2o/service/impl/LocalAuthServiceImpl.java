package com.yaya.o2o.service.impl;

import com.yaya.o2o.dao.LocalAuthDao;
import com.yaya.o2o.dao.PersonInfoDao;
import com.yaya.o2o.dto.ImageHolder;
import com.yaya.o2o.dto.LocalAuthExecution;
import com.yaya.o2o.entity.LocalAuth;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.enums.LocalAuthStateEnum;
import com.yaya.o2o.exceptions.LocalAuthOperationException;
import com.yaya.o2o.service.LocalAuthService;
import com.yaya.o2o.util.ImageUtil;
import com.yaya.o2o.util.MD5Util;
import com.yaya.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String useruame, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(useruame, MD5Util.getMD5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution register(LocalAuth localAuth, CommonsMultipartFile profileImg) throws LocalAuthOperationException {
        if (localAuth == null || localAuth.getPassword() == null || localAuth.getPassword().equals("") || localAuth.getUsername() == null || localAuth.getUsername().equals("")) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        int usernameCount = localAuthDao.queryLocalByUserName(localAuth.getUsername());
        if(usernameCount != 0) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_LOCALAUTH);
        }
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5Util.getMD5(localAuth.getPassword()));
            if (localAuth.getPersonInfo() != null && localAuth.getPersonInfo().getUserId() == null) {
                if (profileImg != null) {
                    localAuth.getPersonInfo().setUserType(1);
                    localAuth.getPersonInfo().setCreateTime(new Date());
                    localAuth.getPersonInfo().setLastEditTime(new Date());
                    localAuth.getPersonInfo().setEnableStatus(1);
                    try {
                        addProfileImg(localAuth, profileImg);
                    } catch (Exception e) {
                        throw new LocalAuthOperationException("addUserProfileImg error: " + e.getMessage());
                    }
                }
                try {
                    PersonInfo personInfo = localAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    if (effectedNum <= 0) {
                        throw new LocalAuthOperationException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    throw new LocalAuthOperationException("insertPersonInfo error: " + e.getMessage());
                }
            }
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectedNum <= 0) {
                throw new LocalAuthOperationException("帐号创建失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
        }
    }

    private void addProfileImg(LocalAuth localAuth, CommonsMultipartFile profileImg) throws IOException {
        String dest = PathUtil.getPersonInfoImagePath();
        ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
        String profileImgAddr = ImageUtil.generateThumbnail(imageHolder, dest);
        localAuth.getPersonInfo().setProfileImg(profileImgAddr);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        //空值判断,传入的localAuth帐号密码,用户信息特别是userId不能为空
        if(localAuth == null || localAuth.getUsername() == null || localAuth.getUsername().equals("") || localAuth.getPassword() == null || localAuth.getPassword().equals("") || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        //查询此用户是否已经绑定过平台帐号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        //如果绑定过则直接退出,以保证平台帐号的唯一性
        if(tempAuth != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            //如果之前没有绑定过平台账号,则创建一个平台帐号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            //对密码进行MD5加密
            localAuth.setPassword(MD5Util.getMD5(localAuth.getPassword()));
            int effectedNum = localAuthDao.insertLocalAuth(localAuth);
            //判断创建是否成功
            if(effectedNum <= 0) {
                throw new LocalAuthOperationException("账号绑定失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("insetLocalAuth error:" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        //非空判断
        if(userId == null || username == null || username.equals("") || password == null || password.equals("") || newPassword == null || newPassword.equals("")) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }

        try {
            //更新密码
            int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5Util.getMD5(password), MD5Util.getMD5(newPassword), new Date());
            //判断是否成功
            if(effectedNum <= 0) {
                throw new LocalAuthOperationException("更新密码失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new LocalAuthOperationException("updateLocalAuth error:" + e.toString());
        }
    }
}
