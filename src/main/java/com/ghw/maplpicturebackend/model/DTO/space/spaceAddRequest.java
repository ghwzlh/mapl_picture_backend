package com.ghw.maplpicturebackend.model.DTO.space;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 新建空间请求参数
 */
@Data
public class spaceAddRequest implements Serializable {

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

