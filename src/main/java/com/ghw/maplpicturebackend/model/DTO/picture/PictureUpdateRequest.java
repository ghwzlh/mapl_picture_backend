package com.ghw.maplpicturebackend.model.DTO.picture;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 给管理员使用,更新图片信息
 */
@Data
public class PictureUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "未输入ID，请输入值")
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    @Length(max = 65535, message = "简介过长，请重新输入")
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}

