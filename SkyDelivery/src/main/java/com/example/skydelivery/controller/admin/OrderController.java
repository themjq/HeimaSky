package com.example.skydelivery.controller.admin;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.*;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.OrderService;
import com.example.skydelivery.vo.OrderOverViewVO;
import com.example.skydelivery.vo.OrderStatisticsVO;
import com.example.skydelivery.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端的订单管理
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "用户订单接口")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/details/{id}")
    @ApiOperation("根据id查询订单详情")
    public Result<OrderVO> getDeatilById(@PathVariable("id") Long id){
        log.info("{}用户查看id为{}的订单详细信息.", BaseContext.getCurrentId(),id);
        OrderVO ordersVO=orderService.getDetailById(id);
        return Result.success(ordersVO);
    }

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("用户{}进行了搜索操作:{}",BaseContext.getCurrentId(),ordersPageQueryDTO);
        PageResult pageResult=orderService.conditionSearch(ordersPageQueryDTO);
        /**
         * 为什么日期使用java从数据库中取出时是2023-10-17 10:24:31的格式，但返回给前端时变成了[2023,10,11,16,57]？
         * 这是因为在返回给前端时，日期对象被转换为了一个数组形式的JSON格式。默认情况下，Spring Boot使用Jackson库来进行JSON序列化和反序列化操作。
         *
         * 当将日期对象序列化为JSON时，默认情况下，Jackson库会将日期对象转换为一个数组，其中包含年、月、日、小时、分钟和秒等字段。这就是为什么你看到的返回给前端的日期格式是[2023,10,11,16,57]。
         *
         * 如果你希望返回给前端的日期格式保持为字符串形式，你可以在OrdersPageQueryDTO类中的日期字段上添加@JsonFormat注解，指定日期的格式。例如：
         * @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
         * private LocalDateTime beginTime;
         */
        return Result.success(pageResult);
    }

    /**
     * 查询对应状态订单数量
     */
    @GetMapping("/statistics")
    @ApiOperation("查询对应状态订单数目")
    public Result<OrderStatisticsVO> getStatus(){
        OrderStatisticsVO orderStatisticsVO= orderService.getStatus();
        log.info("用户{}查询总订单状态{}",BaseContext.getCurrentId(),orderStatisticsVO);
        return Result.success(orderStatisticsVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("根据id接单")
    public Result acceptOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("管理员{}接单,订单id{}",BaseContext.getCurrentId(),ordersConfirmDTO.getId());
        orderService.acceptOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("根据id拒单")
    public Result refuseOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("管理员{}拒单,订单id{}",BaseContext.getCurrentId(),ordersRejectionDTO.getId());
        orderService.refuseOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("根据id拒单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("管理员{}取消订单,订单id{}",BaseContext.getCurrentId(),ordersCancelDTO.getId());
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable("id") Long id) {
        log.info("管理员{}派送订单,订单id{}",BaseContext.getCurrentId(),id);
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        log.info("管理员{}完成订单,订单id{}",BaseContext.getCurrentId(),id);
        orderService.complete(id);
        return Result.success();
    }
}
