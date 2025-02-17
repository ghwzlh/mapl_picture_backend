package com.ghw.maplpicturebackend.aop;

import com.ghw.maplpicturebackend.Exception.BusinessException;
import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.Utils.RedisUtils;
import com.ghw.maplpicturebackend.Utils.UserMessageThreadLocalUtils;
import com.ghw.maplpicturebackend.annotation.rateLimited;
import com.ghw.maplpicturebackend.model.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class rateLimitInterceptor {

    private RedisUtils redisUtils;

    public rateLimitInterceptor(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Pointcut("@annotation(com.ghw.maplpicturebackend.annotation.rateLimited)")
    public void pt(){}

    /**
     * 执行拦截，进行日志记录
     * @param joinPoint
     * @return
     */
    @Around("pt() && @annotation(rateLimited)")
    public Object ratelimit(ProceedingJoinPoint joinPoint, rateLimited rateLimited) throws Throwable {
        User userMessage = UserMessageThreadLocalUtils.getUserMessage();
        String redisKey = "rate_limit" + userMessage.getId();
        // 查询是否流量过大
        if(!redisUtils.allowRequest(redisKey, rateLimited.value(), rateLimited.limit())) {
            Object proceed = joinPoint.proceed();
            return proceed;
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作太快，请稍后重试");
        }
    }
}
