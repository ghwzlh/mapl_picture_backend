package com.ghw.maplpicturebackend.model.DTO.User;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户请求参数封装类
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1173150413084921948L;
    /**
     * 账号
     */
    @NotNull(message = "未输入账号，请输入值")
    @NotEmpty(message = "输入账号为空字符串，请重新输入")
    @Length(max = 12, min = 6, message = "输入账号长度有误，请重新输入")
    private String userAccount;
    /**
     * 密码
     */
    @NotNull(message = "未输入密码，请输入值")
    @NotEmpty(message = "输入密码为空字符串，请重新输入")
    @Length(max = 12, min = 6, message = "输入密码长度有误，请重新输入")
    private String userPassword;
    /**
     * 确认密码
     */
    @NotNull(message = "未输入确认密码，请输入值")
    @NotEmpty(message = "输入确认密码为空字符串，请重新输入")
    @Length(max = 12, min = 6, message = "输入密码长度有误，请重新输入")
    private String checkPassword;
}
