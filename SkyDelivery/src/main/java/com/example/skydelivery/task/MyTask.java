package com.example.skydelivery.task;

import com.alibaba.fastjson.JSON;
import com.example.skydelivery.entity.Orders;
import com.example.skydelivery.mapper.OrderMapper;
import com.example.skydelivery.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MyTask {
//    /**
//     * 定时任务
//     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void executeTask(){
//        log.info("当前时间:{}",new Date());
//    }
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WebSocketServer webSocketServer;
    /**
     * 超时订单处理
     */
    @Scheduled(cron ="0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("时间:{}.处理超时订单", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList=orderMapper.selectTimeoutOrders(Orders.PENDING_PAYMENT,localDateTime);
        for (Orders orders:ordersList){
            orders.setStatus(Orders.CANCELLED);//取消订单
            orders.setCancelReason("订单超时");
            orders.setCancelTime(LocalDateTime.now());

            orderMapper.update(orders);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")//每日凌晨一点触发
    public void processDeliveryOrder(){
        log.info("时间:{}处理派送中的订单",LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList=orderMapper.selectTimeoutOrders(Orders.DELIVERY_IN_PROGRESS,localDateTime);
        for (Orders orders:ordersList){
            orders.setStatus(Orders.COMPLETED);//完成订单
            orderMapper.update(orders);
        }
    }

    //向管理网页推送消息
//    @Scheduled(cron ="0/1 * * * * ?")
//    public void process(){
//        Map map=new HashMap();
//        map.put("type",1);
//        map.put("orderId",4564816);
//        map.put("content","订单号");
//
//        String json= JSON.toJSONString(map);
//        webSocketServer.sendToAllClient(json);
//    }


}
