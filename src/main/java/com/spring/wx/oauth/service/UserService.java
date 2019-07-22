package com.spring.wx.oauth.service;

import com.spring.wx.oauth.dao.UserMapper;
import com.spring.wx.oauth.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public UserEntity getOpenid(String openid) {
        return userMapper.getOpenid(openid);
    }

    public int insert(UserEntity userEntity) {
        return userMapper.insert(userEntity);
    }

    public int update(UserEntity userEntity) {
        return userMapper.update(userEntity);
    }

    public int register(String openid, String phone, String email) {
        return userMapper.register(openid, phone, email);
    }

}
