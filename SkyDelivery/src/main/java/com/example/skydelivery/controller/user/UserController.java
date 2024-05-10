package com.example.skydelivery.controller.user;

import cn.hutool.jwt.JWTUtil;
import com.example.skydelivery.constant.JwtClaimsConstant;
import com.example.skydelivery.dto.UserLoginDTO;
import com.example.skydelivery.entity.User;
import com.example.skydelivery.mapper.UserMapper;
import com.example.skydelivery.properties.JwtProperties;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.UserService;
import com.example.skydelivery.utils.JwtUtil;
import com.example.skydelivery.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "用户接口")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * 微信登录
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录:{}",userLoginDTO);
        User user=userService.wxLogin(userLoginDTO);

        //根据数据生成令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token= JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
}
