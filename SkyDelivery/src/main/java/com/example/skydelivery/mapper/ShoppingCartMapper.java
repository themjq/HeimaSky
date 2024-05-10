package com.example.skydelivery.mapper;

import com.example.skydelivery.entity.ShoppingCart;
import com.example.skydelivery.result.Result;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 动态条件查询
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id修改商品数量
     */
    @Update("update shopping_cart set number = #{number} where id =#{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入菜品
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, user_id, dish_id, setmeal_id, dish_flavor, number,amount, image, create_time) " +
            "values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 查询购物车
     * @return
     */
    @Select("select * from shopping_cart where user_id=#{userId}")
    List<ShoppingCart> show(Long userId);

    /**
     * 清空
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void clean(Long userId);

    /**
     * 批量插入
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);

}
