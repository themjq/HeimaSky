package com.example.skydelivery.service.impl;

import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.constant.StatusConstant;
import com.example.skydelivery.dto.DishDTO;
import com.example.skydelivery.dto.DishPageQueryDTO;
import com.example.skydelivery.entity.Dish;
import com.example.skydelivery.entity.DishFlavor;
import com.example.skydelivery.entity.Setmeal;
import com.example.skydelivery.exception.DeletionNotAllowedException;
import com.example.skydelivery.mapper.DishFlavorMapper;
import com.example.skydelivery.mapper.DishMapper;
import com.example.skydelivery.mapper.SetmealDishMapper;
import com.example.skydelivery.mapper.SetmealMapper;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.service.DishService;
import com.example.skydelivery.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    /**
     * 新增菜品和口味
     * @param dishDTO
     */
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Transactional
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        Long dishId=dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null&& flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除菜品
     */
    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE)//判断是否有菜品在售
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //判断是否有关联的套餐
        List<Long> setmealIdsByDishIDs=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsByDishIDs!=null&&setmealIdsByDishIDs.size()>0)
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
//        for (Long id: ids) {
//            dishMapper.deleteById(id);
//        }
        //优化
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByIds(ids);
    }

    /**
     * 查询具体信息（id）
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish=dishMapper.getById(id);
        List<DishFlavor> dishFlavors=dishFlavorMapper.getById(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
//        BeanUtils.copyProperties(dishFlavors,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
    /**
     * 修改菜品
     * @param dishDTO
     */
    public void update(DishDTO dishDTO){
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        dishFlavorMapper.deleteById(dishDTO.getId());

        List<DishFlavor> dishFlavorList=dishDTO.getFlavors();
        if(dishFlavorList !=null && dishFlavorList.size()>0)
            dishFlavorList.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
        dishFlavorMapper.insertBatch(dishFlavorList);
    }
    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealDishMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getById(d.getId());
            Integer count=dishMapper.getMonthCountById(d.getId(), LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
            dishVO.setCount(count);
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 根据菜品分类查询对应的菜品信息
     */
    @Override
    public List<Dish> getListByIdF(Long categoryId) {
        return dishMapper.list(Dish.builder().categoryId(categoryId).status(StatusConstant.ENABLE).build());
    }
}
