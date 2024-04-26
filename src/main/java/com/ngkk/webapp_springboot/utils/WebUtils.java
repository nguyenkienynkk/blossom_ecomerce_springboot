package com.ngkk.webapp_springboot.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {
    public static HttpServletRequest getCurrentRequest(){ // Để sử dụng HttpServletRequest cho tất cả các request
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
