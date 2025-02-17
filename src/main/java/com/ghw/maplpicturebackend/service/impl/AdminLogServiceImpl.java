package com.ghw.maplpicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghw.maplpicturebackend.model.entity.AdminLog;
import com.ghw.maplpicturebackend.service.AdminLogService;
import com.ghw.maplpicturebackend.mapper.AdminLogMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【admin_log(管理员操作日志)】的数据库操作Service实现
* @createDate 2025-02-15 14:58:12
*/
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
    implements AdminLogService{

}




