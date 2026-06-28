package com.checkin.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程实体类
 * 存储课程基本信息
 *
 * @author 明建超
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "courses")
public class Course {

    /**
     * 课程ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程名称
     */
    @Column(nullable = false, length = 100)
    private String courseName;

    /**
     * 课程编号
     */
    @Column(nullable = false, unique = true, length = 20)
    private String courseCode;

    /**
     * 课程描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 授课教师ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /**
     * 上课时间（如：周一1-2节）
     */
    @Column(length = 50)
    private String classTime;

    /**
     * 上课地点
     */
    @Column(length = 100)
    private String location;

    /**
     * 选课学生列表
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_students",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students;

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
