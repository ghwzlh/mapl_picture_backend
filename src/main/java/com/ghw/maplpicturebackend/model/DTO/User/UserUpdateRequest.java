package com.ghw.maplpicturebackend.model.DTO.User;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "未输入ID，请输入值")
    private Long id;

    /**
     * 用户昵称
     */
    @NotNull(message = "未输入昵称，请输入值")
    @NotEmpty(message = "输入昵称为空字符串，请重新输入")
    @Length(max = 20, message = "输入昵称长度有误，请重新输入")
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, vip, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
