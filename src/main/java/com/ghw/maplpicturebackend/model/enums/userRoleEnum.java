package com.ghw.maplpicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum userRoleEnum {

    USER("用户", "user"),
    VIP("vip用户", "vip"),
    ADMIN("管理员", "admin");

    private final String text;

    private final String value;

    userRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     *根据Value值获取 userRoleEnum
     * @param value
     * @return
     */
    public static userRoleEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (userRoleEnum userRoleEnum : userRoleEnum.values()) {
            if(userRoleEnum.value.equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}