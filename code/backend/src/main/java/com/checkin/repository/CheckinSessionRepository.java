package com.checkin.repository;

import com.checkin.model.CheckinSession;
import com.checkin.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 签到会话数据访问接口
 *
 * @author 明建超
 * @version 1.0.0
 */
@Repository
public interface CheckinSessionRepository extends JpaRepository<CheckinSession, Long> {

    /**
     * 根据课程查找签到会话列表
     *
     * @param course 课程信息
     * @return 签到会话列表
     */
    List<CheckinSession> findByCourseOrderByStartTimeDesc(Course course);

    /**
     * 根据课程和状态查找签到会话
     *
     * @param course 课程信息
     * @param status 签到状态
     * @return 签到会话列表
     */
    List<CheckinSession> findByCourseAndStatus(Course course, CheckinSession.SessionStatus status);

    /**
     * 根据签到码查找签到会话
     *
     * @param checkinCode 签到码
     * @return 签到会话信息
     */
    Optional<CheckinSession> findByCheckinCode(String checkinCode);

    /**
     * 查找进行中的签到会话
     *
     * @param status 签到状态
     * @return 签到会话列表
     */
    List<CheckinSession> findByStatus(CheckinSession.SessionStatus status);
}
