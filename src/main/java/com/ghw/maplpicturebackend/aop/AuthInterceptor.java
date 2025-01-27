package com.ghw.maplpicturebackend.aop;

import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.Exception.ThrowUtils;
import com.ghw.maplpicturebackend.annotation.AuthCheck;
import com.ghw.maplpicturebackend.model.entity.User;
import com.ghw.maplpicturebackend.model.enums.userRoleEnum;
import com.ghw.maplpicturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class AuthInterceptor {

    private UserService userService;

    public AuthInterceptor(UserService userService) {
        this.userService = userService;
    }

    /**
     * 执行拦截，进行权限校验
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "user is not login");
        // 该方法需要的权限
        userRoleEnum enumByValue = userRoleEnum.getEnumByValue(mustRole);
        // 方法不需要权限，直接放行
        if (enumByValue == null) {
            joinPoint.proceed();
        }
        // 用户为无权限，报错
        // 用户的权限
        userRoleEnum loginUserRole = userRoleEnum.getEnumByValue(loginUser.getUserRole());
        ThrowUtils.throwIf(loginUserRole == null, ErrorCode.NO_AUTH_ERROR, "无权限访问", "user no auth");
        // 需要vip权限但没有，报错
        ThrowUtils.throwIf(userRoleEnum.VIP.equals(enumByValue) && !userRoleEnum.VIP.equals(loginUserRole), ErrorCode.NO_AUTH_ERROR, "无权限访问", "user need vip");
        // 需要管理员权限，报错
        ThrowUtils.throwIf(userRoleEnum.ADMIN.equals(enumByValue) && !userRoleEnum.ADMIN.equals(loginUserRole), ErrorCode.NO_AUTH_ERROR, "无权限访问", "user need admin");
        return joinPoint.proceed();
    }
}
