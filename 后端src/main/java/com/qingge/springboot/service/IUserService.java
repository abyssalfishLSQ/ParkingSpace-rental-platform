package com.qingge.springboot.service;

import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 李思琪
 * @since 2023-06-05
 */
public interface IUserService extends IService<User> {
    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
