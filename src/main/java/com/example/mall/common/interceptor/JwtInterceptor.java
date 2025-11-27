package com.example.mall.common.interceptor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.mall.common.Utils.JwtUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor{

    @Resource
    private JwtUtils jwtUtils;
    
    @Override
    public boolean preHandle (HttpServletRequest request, 
           HttpServletResponse response, Object handler) throws IOException {
        log.info("preHandle - 开始: 请求URI={}, 请求方法={}", request.getRequestURI(), request.getMethod());
        
        String authHeader = request.getHeader("Authorization");
        log.debug("preHandle - 获取Authorization头信息: {}", authHeader);
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.warn("preHandle - Authorization头信息缺失或格式错误");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("preHandle - 结束: 返回false，状态码={}", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);
        log.debug("preHandle - 提取Token: {}", token);
        
        try{
            jwtUtils.parseToken(token);
            log.info("preHandle - Token验证通过");
            log.info("preHandle - 结束: 返回true");
            return true;
        }catch(Exception e){
            log.error("preHandle - Token解析异常", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("preHandle - 结束: 返回false，状态码={}", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

    }
}