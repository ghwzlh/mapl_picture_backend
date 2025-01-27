package com.ghw.maplpicturebackend.model.DTO.User;

import com.ghw.maplpicturebackend.common.PageRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, vip, admin
     */
    private String userRole;

    /**
     * 是否删除
     */
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}
