package com.example.skydelivery.controller.user;

import com.alibaba.fastjson.JSON;
import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.OrdersPageQueryDTO;
import com.example.skydelivery.dto.OrdersPaymentDTO;
import com.example.skydelivery.dto.OrdersSubmitDTO;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.OrderService;
import com.example.skydelivery.vo.OrderPaymentVO;
import com.example.skydelivery.vo.OrderSubmitVO;
import com.example.skydelivery.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户订单接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 提交订单
     */
    @PostMapping("/submit")
    @ApiOperation("用户下订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单,详细信息:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO= orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
//        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
//        return Result.success(orderPaymentVO);

        log.info("订单支付：{}", ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        //HashMap<Object, Object> map = new HashMap<>();
        return Result.success( new OrderPaymentVO());
    }

    /**
     * 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单支付")
    public Result<OrderVO> getOrderDetailById(@PathVariable("id")Long  id){
        log.info("用户{}查询订单详情,订单ID:{}", BaseContext.getCurrentId(),id);
        OrderVO orderVO=orderService.getOrderDetailById(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("订单支付")
    public Result<OrderVO> cancel(@PathVariable("id")Long  id){
        log.info("用户{}取消订单,订单ID:{}", BaseContext.getCurrentId(),id);
        orderService.cancel(id);
        return Result.success();
    }
    /**
     * 催单
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }
    /**
     * 再来一单
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }
    /**
     * 历史订单
     */
    @GetMapping("/historyOrders")
    @ApiOperation("再来一单")
    public Result<PageResult> historyOrders(int page, int pageSize, Integer status){
        Long userId=BaseContext.getCurrentId();
        log.info("用户{}查看了历史订单,参数page={} size={} status={}",userId,page,pageSize,status);
        PageResult pageResult=orderService.historyOrders(page,pageSize,status);
        return Result.success(pageResult);
    }
    /**
     * 完成订单
     */
    @PostMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result completeOrder(@PathVariable("id") Long id){
        Long userId=BaseContext.getCurrentId();
        log.info("订单已完成，订单id:{}",id);
        orderService.completeOrderById(id);
        return Result.success();
    }
}
