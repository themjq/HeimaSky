package com.example.skydelivery.controller.admin;

import com.example.skydelivery.dto.CategoryDTO;
import com.example.skydelivery.dto.CategoryPageQueryDTO;
import com.example.skydelivery.entity.Category;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类接口")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 新增分类
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询:{}",categoryPageQueryDTO);
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除分类
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String> deleteById(Long id){
        log.info("删除分类:{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 分类状态设置
     */
    @PostMapping("/status/{status}")
    @ApiOperation("分类状态设置")
    public Result<String> categoryStatue(@PathVariable("status") Integer status,Long id){
        categoryService.categoryStatue(status,id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     */
    @GetMapping("/list")
    @ApiOperation("类型查询")
    public Result<List<Category>> list(Integer type){
        List<Category> list =categoryService.list(type);
        return Result.success(list);
    }
}
