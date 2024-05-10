package com.example.skydelivery.service;

import com.example.skydelivery.dto.CategoryDTO;
import com.example.skydelivery.dto.CategoryPageQueryDTO;
import com.example.skydelivery.entity.Category;
import com.example.skydelivery.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);
    /**
     * 分页查询
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    /**
     *删除分类(id)
     */
    void deleteById(Long id);
    /**
     *修改分类
     */
    void update(CategoryDTO categoryDTO);
    /**
     *启用，禁用分类
     */
    void categoryStatue(Integer status,Long id);
    /**
     *查询分类
     */
    List<Category> list(Integer type);
}
