package com.example.skydelivery.mapper;

import com.example.skydelivery.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 查询菜品(根据id)
     * @param id
     * @return
     */
    @Select("select * from order_detail where id=#{id}")
    List<OrderDetail> getByOrderId(Long id);
}
