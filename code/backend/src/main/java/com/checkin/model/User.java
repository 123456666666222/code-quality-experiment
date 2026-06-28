package com.checkin.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储教师和学生信息
 *
 * @author 明建超
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "users")
public class User {

    /**
     * 用户ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名（工号/学号）
     */
    @Column(nullable = false, unique = true, length = 20)
    private String username;

    /**
     * 密码（实际项目中应加密存储）
     */
    @Column(nullable = false)
    private String password;

    /**
     * 真实姓名
     */
    @Column(nullable = false, length = 50)
    private String realName;

    /**
     * 用户角色：TEACHER-教师，STUDENT-学生
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * 所属班级（学生专用）
     */
    @Column(length = 50)
    private String className;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否启用
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * 用户角色枚举
     */
    public enum UserRole {
        TEACHER,  // 教师
        STUDENT   // 学生
    }

    /**
     * 创建时间自动填充
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 更新时间自动填充
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
