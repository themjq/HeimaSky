package com.example.skydelivery.service;

import com.example.skydelivery.dto.DishDTO;
import com.example.skydelivery.dto.DishPageQueryDTO;
import com.example.skydelivery.entity.Dish;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    /**
     * 新增菜品
     */
    public void save(DishDTO dishDTO);

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据id查询菜品具体信息
     * @param id
     * @return
     */
     DishVO getById(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);
    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 根据菜品分类查询对应的菜品信息
     */
    List<Dish> getListByIdF(Long category);
}
