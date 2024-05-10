package com.example.skydelivery.interceptor;

import com.example.skydelivery.constant.JwtClaimsConstant;
import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.properties.JwtProperties;
import com.example.skydelivery.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //2、校验令牌
        try {
            log.info("jwt校验{}",token);
            Claims claims= JwtUtil.parseJWT(jwtProperties.getUserSecretKey(),token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            log.info("当前用户id:{}", userId);
            //3、通过，放行
            return true;
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }
}
