package com.ghw.maplpicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghw.maplpicturebackend.model.entity.Category;
import com.ghw.maplpicturebackend.service.CategoryService;
import com.ghw.maplpicturebackend.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【category(分类)】的数据库操作Service实现
* @createDate 2025-02-12 13:51:24
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




