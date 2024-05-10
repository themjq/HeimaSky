package com.example.skydelivery.service;

import com.example.skydelivery.dto.*;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.vo.*;

public interface OrderService {
    /**
     * 用户下单
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);

    /**
     * 根据id查询对应的order
     * @param id
     * @return
     */
    OrderVO getDetailById(Long id);

    /**
     * 搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询状态订单总数
     * @return
     */
    OrderStatisticsVO getStatus();

    /**
     * 接单
     */
    void acceptOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersConfirmDTO
     */
    void refuseOrder(OrdersRejectionDTO ordersConfirmDTO) throws Exception;

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 订单详情
     * @param id
     * @return
     */
    OrderVO getOrderDetailById(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 查看历史订单
     * @return
    */
    PageResult historyOrders(int page, int pageSize, Integer status);

    /**
     * 根据id完成订单
     * @param id
     */
    void completeOrderById(Long id);
}
