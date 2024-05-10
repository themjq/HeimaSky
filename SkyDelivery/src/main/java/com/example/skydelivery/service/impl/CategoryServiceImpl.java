package com.example.skydelivery.service.impl;

import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.constant.StatusConstant;
import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.CategoryDTO;
import com.example.skydelivery.dto.CategoryPageQueryDTO;
import com.example.skydelivery.entity.Category;
import com.example.skydelivery.exception.DeletionNotAllowedException;
import com.example.skydelivery.mapper.CategoryMapper;
import com.example.skydelivery.mapper.DishMapper;
import com.example.skydelivery.mapper.SetmealMapper;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     *新增分类
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);//属性拷贝
        //0禁用 1 启用
        category.setStatus(StatusConstant.DISABLE);

        //设置dto中没有的信息
        //已配置自动填充切面类
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }
    /**
     *分页查询
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
    /**
     *删除分类（id）
     */
    @Override
    public void deleteById(Long id) {
        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count =dishMapper.countByCategoryId(id);
        if(count>0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        //删除分类数据
        categoryMapper.deleteById(id);
    }
    /**
     *修改分类
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //设置修改时间、修改人
        //已配置自动填充切面类
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }
    /**
     *修改分类状态
     */
    @Override
    public void categoryStatue(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                //已配置自动填充切面类
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }
    /**
     *类型查询分类
     */
    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
