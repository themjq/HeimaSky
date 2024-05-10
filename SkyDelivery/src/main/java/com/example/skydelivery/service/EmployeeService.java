package com.example.skydelivery.service;


import com.example.skydelivery.dto.EmployeeDTO;
import com.example.skydelivery.dto.EmployeeLoginDTO;
import com.example.skydelivery.dto.EmployeePageQueryDTO;
import com.example.skydelivery.entity.Employee;
import com.example.skydelivery.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     *
     *新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 员工状态管理
     * @param status
     * @param id
     */
    public void employeeStatue(Integer status, Long id);

    Employee getById(Long id);

    /**
     * 更新员工
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
