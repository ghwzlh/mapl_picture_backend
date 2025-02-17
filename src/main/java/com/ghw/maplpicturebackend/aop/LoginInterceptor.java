package com.ghw.maplpicturebackend.aop;

import com.ghw.maplpicturebackend.Utils.UserMessageThreadLocalUtils;
import com.ghw.maplpicturebackend.common.Constant;
import com.ghw.maplpicturebackend.model.entity.User;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(1)
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object attribute = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        User loginUser = (User) attribute;
        UserMessageThreadLocalUtils.setUserMessage(loginUser);
        return true;
    }

    /**
     * 避免内存泄露
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserMessageThreadLocalUtils.clearUserMessage();
    }
}
