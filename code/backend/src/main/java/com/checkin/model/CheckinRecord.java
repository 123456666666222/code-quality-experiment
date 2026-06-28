package com.checkin.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 签到记录实体类
 * 记录学生的签到信息
 *
 * @author 明建超
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "checkin_records")
public class CheckinRecord {

    /**
     * 签到记录ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联签到会话
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private CheckinSession session;

    /**
     * 签到学生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * 签到时间
     */
    @Column(nullable = false)
    private LocalDateTime checkinTime;

    /**
     * 签到状态：SUCCESS-成功，LATE-迟到，FAILED-失败
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckinStatus status;

    /**
     * 签到IP地址（可选）
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 备注信息
     */
    @Column(length = 200)
    private String remark;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createTime;

    /**
     * 签到状态枚举
     */
    public enum CheckinStatus {
        SUCCESS,  // 成功
        LATE,     // 迟到
        FAILED    // 失败
    }

    /**
     * 创建时间自动填充
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (checkinTime == null) {
            checkinTime = LocalDateTime.now();
        }
    }
}
