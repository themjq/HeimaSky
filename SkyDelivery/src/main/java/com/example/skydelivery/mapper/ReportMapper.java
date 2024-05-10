package com.example.skydelivery.mapper;


import com.example.skydelivery.dto.GoodsSalesDTO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {


    /**
     * 根据日期Map获得营业额
     */
    Double getSumDataByDateMap(Map map);

    /**
     * 查询用户数目
     * @param map
     * @return
     */
    Integer getUserDataByDateMap(Map map);

    /**
     * 查询订单总额
     * @param map
     * @return
     */
    Integer getOrdersData(Map map);

    /**
     * 获取销量前十
     */
    List<GoodsSalesDTO> getTop10Data(LocalDateTime begin, LocalDateTime end);
}
