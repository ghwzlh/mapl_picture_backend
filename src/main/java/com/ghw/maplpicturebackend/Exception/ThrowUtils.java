package com.ghw.maplpicturebackend.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 条件成立则抛异常，且输出日志
     *
     * @param condition 条件
     * @param errorCode 错误码
     * @param message   错误信息
     * @param errorlog  日志信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message, String errorlog) {
        if( condition ) {
            log.error(errorlog);
        }
        throwIf(condition, new BusinessException(errorCode, message));
    }
}