package com.example.skydelivery.controller.admin;

import com.example.skydelivery.constant.JwtClaimsConstant;
import com.example.skydelivery.dto.EmployeeDTO;
import com.example.skydelivery.dto.EmployeeLoginDTO;
import com.example.skydelivery.dto.EmployeePageQueryDTO;
import com.example.skydelivery.entity.Employee;
import com.example.skydelivery.properties.JwtProperties;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.result.Result;
import com.example.skydelivery.service.EmployeeService;
import com.example.skydelivery.utils.JwtUtil;
import com.example.skydelivery.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
//@CrossOrigin(origins = "http://127.0.0.1:4567")
@Api(tags = "员工")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     *
     * 登录控制
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工{}登录.",employeeLoginDTO);
        Employee employee =employeeService.login(employeeLoginDTO);

        //JWT令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        String token= JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );
        EmployeeLoginVO employeeLoginVO=EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }
    @PostMapping("/logout")
    public Result<String> logout(){
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增一个员工:{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     *员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("员工表分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数:{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        //log.info("员工分页查询，参数2:{}",pageResult);
        return Result.success(pageResult);
    }

    @ApiOperation("员工状态")
    @PostMapping("/status/{status}")
    public Result employeeStatue(@PathVariable Integer status,Long id){
        log.info("员工账号状态管理:{} {}",status,id);
        employeeService.employeeStatue(status,id);
        return Result.success();
    }

    @ApiOperation("查询员工")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
