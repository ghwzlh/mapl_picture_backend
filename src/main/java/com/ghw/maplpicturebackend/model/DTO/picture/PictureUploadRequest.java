package com.ghw.maplpicturebackend.model.DTO.picture;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片 id（用于修改）
     */
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

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
     * 标签（JSON 数组）
     */
    private List<String> tags;

    /**
     * 图片名称
     */
    private String picName;

    /**
     * 空间ID
     */
    private Long spaceId;


    private static final long serialVersionUID = 1L;
}