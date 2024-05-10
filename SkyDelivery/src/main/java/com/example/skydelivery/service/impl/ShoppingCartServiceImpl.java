package com.example.skydelivery.service.impl;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.ShoppingCartDTO;
import com.example.skydelivery.entity.Dish;
import com.example.skydelivery.entity.Setmeal;
import com.example.skydelivery.entity.ShoppingCart;
import com.example.skydelivery.mapper.DishMapper;
import com.example.skydelivery.mapper.SetmealMapper;
import com.example.skydelivery.mapper.ShoppingCartMapper;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShopping(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list= shoppingCartMapper.list(shoppingCart);

        //如果已存在，则数量加一
        if(list != null && list.size()>0){
            ShoppingCart cart=list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumberById(cart);
        }else {
            Long dishId=shoppingCartDTO.getDishId();
            if(dishId !=null){//菜品
                Dish dish=dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                Long setmealId=shoppingCartDTO.getSetmealId();
                Setmeal setmeal=setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> show() {
        Long userId=BaseContext.getCurrentId();
        return shoppingCartMapper.show(userId);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean(Long userId) {
        shoppingCartMapper.clean(userId);
    }
}
