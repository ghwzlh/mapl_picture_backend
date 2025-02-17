package com.ghw.maplpicturebackend.model.DTO.space;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 空间更新
 */
@Data
public class spaceUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "未输入ID，请输入值")
    private Long id;

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
     * 空间图片的最大总大小
     */
    private Long maxSize;

    /**
     * 空间图片的最大数量
     */
    private Long maxCount;

    private static final long serialVersionUID = 1L;
}
