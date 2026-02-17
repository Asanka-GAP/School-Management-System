package com.school.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.util.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogApiAspect {

    private static final Logger log = LogUtil.getLog(LogApiAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(com.school.annotation.LogApi)")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String apiName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        log.info("=== API Request ===");
        log.info("API Name: {}", apiName);
        log.info("Method: {} {}", method, uri);
        log.info("Request: {}", toJson(args));
        
        long startTime = System.currentTimeMillis();
        Object response = joinPoint.proceed();
        long responseTime = System.currentTimeMillis() - startTime;
        
        log.info("Response: {}", toJson(response));
        log.info("Response Time: {}ms", responseTime);
        log.info("===================\n");
        
        return response;
    }
    
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj != null ? obj.toString() : "null";
        }
    }
}
