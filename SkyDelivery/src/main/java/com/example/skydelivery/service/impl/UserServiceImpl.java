package com.example.skydelivery.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.dto.UserLoginDTO;
import com.example.skydelivery.entity.User;
import com.example.skydelivery.exception.LoginFailedException;
import com.example.skydelivery.mapper.UserMapper;
import com.example.skydelivery.properties.WeChatProperties;
import com.example.skydelivery.service.UserService;
import com.example.skydelivery.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //接口地址
    public static final String LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //与传统不同，调用微信的登录接口
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");

        String json=HttpClientUtil.doGet(LOGIN,map);

        JSONObject jsonObject= JSON.parseObject(json);
        String openid=jsonObject.getString("openid");
        log.info("openid "+ openid);
        //System.out.println("openid "+ openid);
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user=userMapper.getByOpenid(openid);
        if(user==null){
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
