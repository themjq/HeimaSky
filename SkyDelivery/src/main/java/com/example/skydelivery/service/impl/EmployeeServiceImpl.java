package com.example.skydelivery.service.impl;


import com.example.skydelivery.constant.MessageConstant;
import com.example.skydelivery.constant.PasswordConstant;
import com.example.skydelivery.constant.StatusConstant;
import com.example.skydelivery.context.BaseContext;
import com.example.skydelivery.dto.EmployeeDTO;
import com.example.skydelivery.dto.EmployeeLoginDTO;
import com.example.skydelivery.dto.EmployeePageQueryDTO;
import com.example.skydelivery.entity.Employee;
import com.example.skydelivery.exception.AccountLockedException;
import com.example.skydelivery.exception.AccountNotFoundException;
import com.example.skydelivery.exception.PasswordErrorException;
import com.example.skydelivery.mapper.EmployeeMapper;
import com.example.skydelivery.result.PageResult;
import com.example.skydelivery.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     *
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();

        //方式一
        //employee.setName(employeeDTO.getName());

        //方式二  对象属性拷贝   要求属性名称一致
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置employee其余属性
        employee.setStatus(StatusConstant.ENABLE);  //调用封装的常量类
        //employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //已配置自动填充切面类
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //注释标记，用于标识代码中需要完成或需要进一步处理的部分
//        //TODO
//        employee.setCreateUser(10L);
//        employee.setUpdateUser(10L);
        //已配置自动填充切面类
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //log.info("员工分页查询，参数3:{}",employeePageQueryDTO);
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        //log.info("员工分页查询，参数4:{}",page);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 员工状态管理
     * @param status
     * @param id
     */
    @Override
    public void employeeStatue(Integer status, Long id) {
        Employee employee=Employee.builder().status(status).id(id).build();
        employeeMapper.employeeStatue(employee);
    }

    /**
     * 员工查询
     */
    @Override
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("******");
        return employee;
    }

    /**
     * 更新员工
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        //已配置自动填充切面类
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.employeeStatue(employee);
    }
}
