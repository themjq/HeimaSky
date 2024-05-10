package com.example.skydelivery.controller.admin;

import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.DishDTO;
import com.example.skydelivery.dto.DishPageQueryDTO;
import com.example.skydelivery.entity.Dish;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.DishService;
import com.example.skydelivery.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.save(dishDTO);

        //清理缓存
        String key="dish_"+dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult>  page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){//@RequestParam必须加
        log.info("删除菜品:{}",ids);
        dishService.delete(ids);

        //清理缓存
        Set keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    /**
     * 菜品详细信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品");
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }
    /**
     * 更改菜品
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishService.update(dishDTO);

        cleanCache("dish_*");
        return Result.success();
    }
    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){
        log.info("更改商品编号为{}的状态为{}",id,status);
        dishService.startOrStop(status,id);

        //清理缓存
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据菜品分类查询对应的菜品信息
     */
    @GetMapping("/list")
    @ApiOperation("根据菜品分类查询对应的菜品信息")
    public Result<List<Dish>> getListByIdF(Long categoryId){
        log.info("管理员{}查询了菜品分类id为{}的菜品信息", BaseContext.getCurrentId(),categoryId);
        List<Dish> dishList=dishService.getListByIdF(categoryId);
        return Result.success(dishList);
    }

    private void cleanCache(String pattern){
        Set keys=redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
