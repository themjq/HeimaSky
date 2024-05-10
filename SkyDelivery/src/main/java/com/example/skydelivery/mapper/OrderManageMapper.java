package com.example.skydelivery.mapper;

import com.example.skydelivery.dto.OrdersPageQueryDTO;
import com.example.skydelivery.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderManageMapper {





    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 查询状态订单总数
     *
     * @return
     */
    @Select("select count(id) from orders where status=#{status}")
    Integer getStatus(Integer status);

    /**
     * 接单
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     */
    void update(Orders orders);
}
