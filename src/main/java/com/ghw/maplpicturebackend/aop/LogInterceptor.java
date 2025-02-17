package com.ghw.maplpicturebackend.aop;

import com.ghw.maplpicturebackend.Utils.UserMessageThreadLocalUtils;
import com.ghw.maplpicturebackend.model.VO.User.LoginUserVO;
import com.ghw.maplpicturebackend.model.entity.AdminLog;
import com.ghw.maplpicturebackend.model.entity.User;
import com.ghw.maplpicturebackend.model.entity.UserLog;
import com.ghw.maplpicturebackend.model.enums.userRoleEnum;
import com.ghw.maplpicturebackend.service.AdminLogService;
import com.ghw.maplpicturebackend.service.UserLogService;
import com.ghw.maplpicturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
public class LogInterceptor {

    private final AdminLogService adminLogService;
    private final UserLogService userLogService;
    private UserService userService;

    public LogInterceptor(AdminLogService adminLogService, UserLogService userLogService, UserService userService) {
        this.adminLogService = adminLogService;
        this.userLogService = userLogService;
        this.userService = userService;
    }


    @Pointcut("@annotation(com.ghw.maplpicturebackend.annotation.LogAdd)")
    public void pt(){}

    /**
     * 执行拦截，进行日志记录
     * @param joinPoint
     * @return
     */
    @Around("pt()")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 拿到当前登录的用户ID和身份
        User userMessage = UserMessageThreadLocalUtils.getUserMessage();
        String userRole = userMessage.getUserRole();
        Long userId = userMessage.getId();
        // 拿到当前时间
        LocalDateTime now = LocalDateTime.now();
        // 拿到操作的方法名称
        String functionName = joinPoint.getSignature().getName();
        if (userRoleEnum.ADMIN.getValue().equals(userRole)) {
            AdminLog adminLog = new AdminLog(userId, functionName);
            adminLogService.save(adminLog);
        } else {
            UserLog userLog = new UserLog(userId, functionName);
            userLogService.save(userLog);
        }
        Object proceed = joinPoint.proceed();
        return proceed;
    }

}
