package com.ghw.maplpicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghw.maplpicturebackend.model.entity.UserLog;
import com.ghw.maplpicturebackend.service.UserLogService;
import com.ghw.maplpicturebackend.mapper.UserLogMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_log(用户操作日志)】的数据库操作Service实现
* @createDate 2025-02-15 14:58:17
*/
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog>
    implements UserLogService{

}




