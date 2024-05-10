package com.example.skydelivery.service;

import com.example.skydelivery.dto.SetmealDTO;
import com.example.skydelivery.dto.SetmealPageQueryDTO;
import com.example.skydelivery.entity.Setmeal;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.vo.DishItemVO;
import com.example.skydelivery.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
    /**
     * 根据id查询套餐和关联的菜品数据
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);
    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    /**
     * 批量删除套餐
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);
    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

}
