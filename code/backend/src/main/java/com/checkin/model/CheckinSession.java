package com.checkin.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 签到会话实体类
 * 记录每次签到活动的信息
 *
 * @author 明建超
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "checkin_sessions")
public class CheckinSession {

    /**
     * 签到会话ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联课程
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * 发起签到的教师
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /**
     * 签到码（6位数字）
     */
    @Column(nullable = false, length = 10)
    private String checkinCode;

    /**
     * 签到开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 签到结束时间
     */
    private LocalDateTime endTime;

    /**
     * 签到状态：ACTIVE-进行中，CLOSED-已结束
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    /**
     * 应到人数
     */
    private Integer expectedCount;

    /**
     * 实到人数
     */
    private Integer actualCount = 0;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 签到会话状态枚举
     */
    public enum SessionStatus {
        ACTIVE,   // 进行中
        CLOSED    // 已结束
    }

    /**
     * 创建时间自动填充
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (status == null) {
            status = SessionStatus.ACTIVE;
        }
    }
}
