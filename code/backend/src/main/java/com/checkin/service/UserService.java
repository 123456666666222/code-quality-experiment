package com.checkin.service;

import com.checkin.model.User;
import com.checkin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 用户业务逻辑服务类
 *
 * @author 明建超
 * @version 1.0.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册成功的用户
     * @throws RuntimeException 如果用户名已存在
     */
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }

    /**
     * 用户登录（简化版，实际项目应使用Spring Security）
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户
     * @throws RuntimeException 如果用户名或密码错误
     */
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        if (!user.getEnabled()) {
            throw new RuntimeException("用户已被禁用");
        }
        return user;
    }

    /**
     * 根据ID查找用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据角色获取用户列表
     *
     * @param role 用户角色
     * @return 用户列表
     */
    public List<User> findByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param userDetails 更新的用户信息
     * @return 更新后的用户
     */
    public User update(Long id, User userDetails) {
        User user = findById(id);
        user.setRealName(userDetails.getRealName());
        user.setClassName(userDetails.getClassName());
        user.setEnabled(userDetails.getEnabled());
        return userRepository.save(user);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
