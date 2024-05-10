package com.example.skydelivery.service;

import com.example.skydelivery.vo.OrderReportVO;
import com.example.skydelivery.vo.SalesTop10ReportVO;
import com.example.skydelivery.vo.TurnoverReportVO;
import com.example.skydelivery.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {
    /**
     * 营业额统计
     */
    TurnoverReportVO getData(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     */
    UserReportVO getUserData(LocalDate begin, LocalDate end);

    /**
     *订单统计
     */
    OrderReportVO getOrdersData(LocalDate begin, LocalDate end);

    /**
     * 销量统计
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10Data(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
