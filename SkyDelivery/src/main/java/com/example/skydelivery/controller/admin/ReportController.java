package com.example.skydelivery.controller.admin;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.ReportService;
import com.example.skydelivery.vo.OrderReportVO;
import com.example.skydelivery.vo.SalesTop10ReportVO;
import com.example.skydelivery.vo.TurnoverReportVO;
import com.example.skydelivery.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Base64;

@RestController
@Slf4j
@RequestMapping("/admin/report")
@Api(tags = "数据统计接口")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     *营业额统计
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> returnChartData(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
            ){
        log.info("统计营业额数据:{}--{}",begin,end);
        return Result.success(reportService.getData(begin,end));
    }
    /**
     * 用户统计
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> returnUsertData(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ){
        log.info("统计用户额数据:{}--{}",begin,end);
        return Result.success(reportService.getUserData(begin,end));
    }
    /**
     * 用户统计
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> returnOrdersData(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ){
        log.info("统计订单数据:{}--{}",begin,end);
        return Result.success(reportService.getOrdersData(begin,end));
    }

    /**
     * 销量前十统计
     */
    @GetMapping("/top10")
    @ApiOperation("销量前十统计")
    public Result<SalesTop10ReportVO> returntopTen(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ){
        log.info("统计销量前十数据:{}--{}",begin,end);
        return Result.success(reportService.getTop10Data(begin,end));
    }
    /**
     * 导出数据
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据")
    public void export(HttpServletResponse response){
        log.info("用户{}导出了运营数据", BaseContext.getCurrentId());
        reportService.exportBusinessData(response);
    }
}
