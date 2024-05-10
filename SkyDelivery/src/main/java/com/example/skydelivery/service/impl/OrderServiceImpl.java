package com.example.skydelivery.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.*;
import com.example.skydelivery.entity.*;
import com.example.skydelivery.exception.AddressBookBusinessException;
import com.example.skydelivery.exception.OrderBusinessException;
import com.example.skydelivery.mapper.*;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.service.OrderService;
import com.example.skydelivery.utils.WeChatPayUtil;
import com.example.skydelivery.vo.*;
import com.example.skydelivery.websocket.WebSocketServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper  orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private OrderManageMapper orderManageMapper;
    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional  //事务
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBook=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null)//地址为空
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        Long userId= BaseContext.getCurrentId();

        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList=shoppingCartMapper.list(shoppingCart);

        if(shoppingCartList==null || shoppingCartList.size()==0)//购物车为空
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);

        //属性设置
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);    //否
        orders.setStatus(Orders.PENDING_PAYMENT);//待付款
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        AddressBook address=addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        String province_name=address.getProvinceName();
        String city_name=address.getCityName();
        String district_name=address.getDistrictName();
        String detail_name=address.getDetail();
        orders.setAddress(province_name+city_name+district_name+detail_name);
        //插入数据
        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList=new ArrayList<>();//批量插入
        for (ShoppingCart cart:shoppingCartList){
            OrderDetail orderDetail=new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());//设置关联的id
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);//插入
        shoppingCartMapper.clean(userId);//清空购物车

        //封装属性
        OrderSubmitVO orderSubmitVO= OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
//        Orders ordersDB = orderMapper.getByNumber(outTradeNo);
        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .number(outTradeNo)
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);

        //向微信小程序返回
        //TODO
//        try {
//            HttpServletResponse response=null;
//            responseToWeixin(response);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        
        
        //向管理网页推送消息
        Map map=new HashMap();
        map.put("type",1);
        map.put("orderId",outTradeNo);
        map.put("content","订单号");

        String json=JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }
    /**
     * 给微信响应
     * @param response
     */
    private void responseToWeixin(HttpServletResponse response) throws Exception{
        response.setStatus(200);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
        response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }

    /**
     * 催单
     * @param id
     */
    @Override
    public void reminder(Long id) {
        Orders ordersDB=orderMapper.getById(id);
        if(ordersDB ==null)//判断订单是否存在
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        Map map=new HashMap();
        map.put("typre",2);
        map.put("orderId",id);
        map.put("content","订单号:"+ordersDB.getNumber());

        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public OrderVO getDetailById(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }
    /**
     * 订单搜索
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page=orderManageMapper.pageQuery(ordersPageQueryDTO);
        //根据订单显示具体的菜品内容 //根据page的内容得到对应的数据，并转换为OrderVO返回
        List<OrderVO> orderVOList =new ArrayList<>();

        List<Orders> ordersList=page.getResult();
        if(!CollectionUtils.isEmpty(ordersList)){
            for(Orders orders:ordersList){
                //TODO
                OrderVO orderVO=new OrderVO();
                //在使用IDE进行debug时，IDE会自动展开对象的属性，以便更方便地查看对象的内部结构。
                BeanUtils.copyProperties(orders,orderVO);

                String orderDishes="";//用于存放具体的菜品信息
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
                //问：该类中只有两个变量，但为什么我使用idea工具进行debug时，对应显示了orderDetailList内的变量？
                // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
                List<String> orderDishList = orderDetailList.stream().map(x -> {
                    String orderDish = x.getName() + "*" + x.getNumber() + ";";
                    return orderDish;
                }).collect(Collectors.toList());

                orderDishes=String.join("",orderDishList);

                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                //System.out.println(orderDishes);
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(),orderVOList);
    }

    /**
     * 查询状态订单总数
     * @return
     */
    @Override
    public OrderStatisticsVO getStatus() {
        OrderStatisticsVO orderStatisticsVO=new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(orderManageMapper.getStatus(Orders.CONFIRMED));
        orderStatisticsVO.setToBeConfirmed(orderManageMapper.getStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(orderManageMapper.getStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    /**
     * 接单
     */
    @Override
    public void acceptOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderManageMapper.update(orders);
    }


    /**
     * 拒单
     */
    @Override
    public void refuseOrder(OrdersRejectionDTO ordersRejectionDTO) throws Exception{
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());
        // 订单只有存在且状态为2（待接单）才可以拒单
        if(ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            log.info("退款,订单{}",ordersDB);
        }

        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .rejectionReason("拒单")
                .cancelReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .build();
        orderManageMapper.update(orders);
    }

    /**
     * 取消订单
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        Orders orders=orderMapper.getById(ordersCancelDTO.getId());
        log.info("退款,订单{}",orders);
        //TODO 取消订单操作
        //

        orderManageMapper.update(
                Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(Orders.CANCELLED)
                .cancelReason(ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .build());
    }

    /**
     * 派送
     * @param id
     */
    @Override
    public void delivery(Long id) {
// 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);


        // 校验订单是否存在，并且状态为3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderManageMapper.update(orders);
    }

    /**
     * 完成订单
     * @param id
     */
    @Override
    public void complete(Long id) {
// 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderManageMapper.update(orders);
    }


    /**
     * 订单详情
     */
    @Override
    public OrderVO getOrderDetailById(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancel(Long id) {
        Orders ordersDB=orderMapper.getById(id);
        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        //TODO 退款操作
        //
        orders.setPayStatus(Orders.REFUND);//设置状态为退款
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelTime(LocalDateTime.now());
        orderManageMapper.update(orders);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repetition(Long id) {
        Long userId=BaseContext.getCurrentId();

        List<OrderDetail> orderDetailList=orderDetailMapper.getByOrderId(id);//获得订单具体信息

        List<ShoppingCart> shoppingCartList=orderDetailList.stream().map(x->{
            ShoppingCart shoppingCart=new ShoppingCart();

            BeanUtils.copyProperties(x,shoppingCart,"id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 历史订单
     */
    @Override
    public PageResult historyOrders(int page, int pageSize, Integer status) {
        PageHelper.startPage(page,pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        //分页查询
        Page<Orders> pages=orderManageMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList();
        if(pages!=null &&pages.size()>0){
            for(Orders orders:pages){
                Long orderId=orders.getId();
                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
                OrderVO orderVO=new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                orderVO.setOrderDetailList(orderDetails);
                list.add(orderVO);
            }
        }
        return new PageResult(pages.getTotal(),list);
    }

    /**
     * 根据id完成订单
     * @param id
     */
    @Override
    public void completeOrderById(Long id) {
        orderMapper.completeOrderById(id,Orders.COMPLETED);
    }
}
