package com.ghw.maplpicturebackend.model.DTO.space;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改已创建的空间（用户本身）
 */
@Data
public class spaceEditRequest implements Serializable {

    /**
     * 空间 id
     */
    @NotNull(message = "未输入ID，请输入值")
    private Long id;

    /**
     * 空间名称
     */
    @Length(max = 20, message = "空间名称过长")
    private String spaceName;

    private static final long serialVersionUID = 1L;
}

