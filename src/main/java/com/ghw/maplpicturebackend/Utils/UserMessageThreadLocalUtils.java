package com.ghw.maplpicturebackend.Utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.ghw.maplpicturebackend.model.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMessageThreadLocalUtils {

    private static final ThreadLocal<User> userMessage = new TransmittableThreadLocal<>();

    public static void setUserMessage(User user) {
        userMessage.set(user);
    }

    public static User getUserMessage() {
        return userMessage.get();
    }

    public static void clearUserMessage() {
        userMessage.remove();
    }
}
