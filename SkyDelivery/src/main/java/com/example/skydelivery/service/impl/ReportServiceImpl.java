package com.example.skydelivery.service.impl;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.GoodsSalesDTO;
import com.example.skydelivery.entity.Orders;
import com.example.skydelivery.mapper.ReportMapper;
import com.example.skydelivery.service.ReportService;
import com.example.skydelivery.service.WorkspaceService;
import com.example.skydelivery.vo.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private WorkspaceService workspaceService;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getData(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> dataList=new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime =LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(localDate, LocalTime.MAX);
            Map map= new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.CANCELLED);
            Double dayData=reportMapper.getSumDataByDateMap(map);
            dayData=dayData==null?0.0:dayData;
            dataList.add(dayData);
        }
        //StringUtils.join(dateList,",");//连接字符，分隔符为','

        //reportMapper.getData(begin,end);
        return TurnoverReportVO.builder().dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(dataList,","))
                .build();
    }

    /**
     * 用户统计
     */
    @Override
    public UserReportVO getUserData(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> userDayList=new ArrayList<>();
        List<Integer> userTotalList=new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime =LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(localDate, LocalTime.MAX);
            Map map= new HashMap<>();

            map.put("end",endTime);
            Integer intDataTotal=reportMapper.getUserDataByDateMap(map);
            intDataTotal=intDataTotal==null?0:intDataTotal;
            userTotalList.add(intDataTotal);

            map.put("begin",beginTime);
            Integer intDataDay=reportMapper.getUserDataByDateMap(map);
            intDataDay=intDataDay==null?0:intDataDay;
            userDayList.add(intDataDay);
        }
        //StringUtils.join(dateList,",");//连接字符，分隔符为','

        //reportMapper.getData(begin,end);
        return UserReportVO.builder().dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(userTotalList,","))
                .newUserList(StringUtils.join(userDayList,","))
                .build();
    }
    /**
     * 订单统计
     */
    @Override
    public OrderReportVO getOrdersData(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> ReallyList=new ArrayList<>();
        List<Integer> TotalList=new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime =LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(localDate, LocalTime.MAX);
            Map map= new HashMap<>();
            map.put("end",endTime);
            map.put("begin",beginTime);
            //总订单
            Integer intTotalDay=reportMapper.getOrdersData(map);
            intTotalDay=intTotalDay==null?0:intTotalDay;
            TotalList.add(intTotalDay);

            //有效订单
            map.put("status", Orders.CANCELLED);
            Integer intDataDay=reportMapper.getOrdersData(map);
            intDataDay=intDataDay==null?0:intDataDay;
            ReallyList.add(intDataDay);
        }
        //计算订单总数
        Integer total_orders=TotalList.stream().reduce(Integer::sum).get();
        //计算有效订单数目
        Integer total_really=ReallyList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate=0.0;
        if(total_orders!=0)
            orderCompletionRate=total_really.doubleValue()/total_orders;

        return OrderReportVO.builder().dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(TotalList,","))
                .validOrderCountList(StringUtils.join(ReallyList,","))
                .validOrderCount(total_really)
                .totalOrderCount(total_orders)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }
    /**
     * 销量前10统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10Data(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime =LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime =LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesDTOS=reportMapper.getTop10Data(beginTime,endTime);
        List<String> names=salesDTOS.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String namelist=StringUtils.join(names,",");

        List<Integer> numbers=salesDTOS.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberlist=StringUtils.join(numbers,",");

        return SalesTop10ReportVO.builder()
                .nameList(namelist)
                .numberList(numberlist)
                .build();
    }

    /**
     * 导出运营数据
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate beginTime =LocalDate.now().minusDays(30);
        LocalDate endTime =LocalDate.now().minusDays(1);
        //概览数据
        BusinessDataVO businessDataVO=workspaceService.getBusinessData(LocalDateTime.of(beginTime,LocalTime.MIN),LocalDateTime.of(endTime,LocalTime.MAX));

        InputStream in=this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(in);
            //填充数据
            XSSFSheet sheet=sheets.getSheet("Sheet1");//具体得表

            //填充时间
            sheet.getRow(1).getCell(1).setCellValue("时间"+beginTime+"至"+endTime);//第二行第二个单元格

            XSSFRow row=sheet.getRow(3);//获得第四行
            row.getCell(2).setCellValue(businessDataVO.getTurnover());//营业额
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());//订单完成率
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());//新用户

            row=sheet.getRow(4);//获得第五行
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());//有效顶单
            row.getCell(5).setCellValue(businessDataVO.getUnitPrice());//平均客单价

            //填充明细
            for (int i = 0; i <30; i++) {
                LocalDate date=beginTime.plusDays(i);
                BusinessDataVO businessData=workspaceService.getBusinessData(LocalDateTime.of(date,LocalTime.MIN),LocalDateTime.of(date,LocalTime.MAX));
                row=sheet.getRow(7+i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());

            }

            ServletOutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            outputStream.close();
            sheets.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
