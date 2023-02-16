package com.yb.aiot.module.auth.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.auth.entity.dto.AddUser;
import com.yb.aiot.module.auth.entity.dto.LoginUser;
import com.yb.aiot.module.auth.entity.dto.UpdateUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface IUserService extends IService<User> {

    /**
     * 登陆
     *
     * @param loginUser 登陆实体
     * @return common.com.yb.aiot.Result
     */
    Result login(LoginUser loginUser);

    /**
     * 查询
     *
     * @param userId userId
     * @return common.com.yb.aiot.Result
     */
    Result select(Integer userId);

    /**
     * 查询所有用户
     *
     * @return common.com.yb.aiot.Result
     */
    Result selectList();

    /**
     * 添加
     *
     * @param addUser 添加实体
     * @return common.com.yb.aiot.Result
     */
    Result add(AddUser addUser);

    /**
     * add Master
     */
    void addMaster();

    /**
     * 更新
     *
     * @param updateUser 更新实体
     * @return common.com.yb.aiot.Result
     */
    Result update(UpdateUser updateUser);

    /**
     * 修改用户状态
     *
     * @param userId 用户id
     * @return common.com.yb.aiot.Result
     */
    Result updateStatus(Integer userId);

    /**
     * 修改密码
     *
     * @param userId   用户id
     * @param password 新密码
     * @return common.com.yb.aiot.Result
     */
    Result updatePwd(Integer userId, String password);

    /**
     * 删除
     *
     * @param userId userId
     * @return common.com.yb.aiot.Result
     */
    Result delete(Integer userId);

}
