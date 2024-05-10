package com.example.skydelivery.mapper;

import com.example.skydelivery.annotation.AutoFill;
import com.example.skydelivery.dto.DishPageQueryDTO;
import com.example.skydelivery.entity.Dish;
import com.example.skydelivery.enumeration.OperationType;
import com.example.skydelivery.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);
    /**
     * 分页擦好像
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询指定菜品（id）
     * @param id
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 删除
     * @param id
     */
    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    /**批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改菜品
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);
    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据订单id查询对应的月销售量
     * select *  from order_detail left outer join orders on (orders.id =order_detail.order_id);
     */
    @Select("select count(*) from order_detail left outer join orders on (orders.id =order_detail.order_id) where dish_id=#{id} and order_time>#{start} and order_time<#{end} and pay_status=1")
    Integer getMonthCountById(Long id, LocalDate start, LocalDate end);
}
