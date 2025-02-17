package com.ghw.maplpicturebackend.model.DTO.space;

import com.ghw.maplpicturebackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 查询已创建的空间
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class spaceQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 空间名称
     */
    @Length(max = 20, message = "空间名称过长")
    private String spaceName;

    /**
     * 空间级别：0-普通版 1-专业版 2-旗舰版
     */
    private Integer spaceLevel;

    /**
     * 空间类型：0-私有 1-团队
     */
    private Integer spaceType;


    private static final long serialVersionUID = 1L;
}
