package com.checkin.controller;

import com.checkin.model.User;
import com.checkin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 处理用户注册、登录、信息管理等请求
 *
 * @author 明建超
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "注册成功",
                    "data", registeredUser
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求（包含用户名和密码）
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            User user = userService.login(username, password);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "登录成功",
                    "data", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "data", users
        ));
    }

    /**
     * 根据角色获取用户列表
     *
     * @param role 用户角色
     * @return 用户列表
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable User.UserRole role) {
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "data", users
        ));
    }

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param userDetails 更新的用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User user = userService.update(id, userDetails);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "更新成功",
                    "data", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "删除成功"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }
}
