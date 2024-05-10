package com.example.skydelivery.mapper;

import com.example.skydelivery.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除
     * @param id
     */
    @Delete("delete from dish_flavor where id=#{id}")
    void deleteById(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询具体信息
     * @param id
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getById(Long id);
}
