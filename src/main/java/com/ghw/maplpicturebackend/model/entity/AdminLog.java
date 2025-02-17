package com.ghw.maplpicturebackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ghw.maplpicturebackend.service.AdminLogService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员操作日志
 * @TableName admin_log
 */
@TableName(value ="admin_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLog implements Serializable {
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调用方法名称
     */
    private String functionName;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public AdminLog(Long id, String functionName) {
        this.id = id;
        this.functionName = functionName;
    }
}