package com.example.skydelivery.mapper;

import com.example.skydelivery.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 提交订单
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    /**
     * 查询指定状态订单数目
     */
    @Select("select count(id) from orders where status=#{status}")
    Integer countByStatus(Integer status);
    /**
     * 根据订单状态和下单时间查询超时订单
     */
    @Select("select * from orders where status=#{status} and order_time <#{orderTime}")
    List<Orders> selectTimeoutOrders(Integer status, LocalDateTime orderTime);
    /**
     * 根据id查订单
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);
    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据id完成订单

     */
    @Update("update orders set status=#{completed},pay_status=1 where id=#{id}")
    void completeOrderById(Long id, Integer completed);
}
