package com.checkin.repository;

import com.checkin.model.CheckinRecord;
import com.checkin.model.CheckinSession;
import com.checkin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 签到记录数据访问接口
 *
 * @author 明建超
 * @version 1.0.0
 */
@Repository
public interface CheckinRecordRepository extends JpaRepository<CheckinRecord, Long> {

    /**
     * 根据签到会话查找签到记录
     *
     * @param session 签到会话
     * @return 签到记录列表
     */
    List<CheckinRecord> findBySession(CheckinSession session);

    /**
     * 根据签到会话和学生查找签到记录
     *
     * @param session 签到会话
     * @param student 学生信息
     * @return 签到记录
     */
    Optional<CheckinRecord> findBySessionAndStudent(CheckinSession session, User student);

    /**
     * 根据学生查找签到记录
     *
     * @param student 学生信息
     * @return 签到记录列表
     */
    List<CheckinRecord> findByStudentOrderByCheckinTimeDesc(User student);

    /**
     * 根据签到会话统计签到人数
     *
     * @param session 签到会话
     * @return 签到人数
     */
    long countBySession(CheckinSession session);

    /**
     * 根据签到会话和状态统计签到人数
     *
     * @param session 签到会话
     * @param status 签到状态
     * @return 签到人数
     */
    long countBySessionAndStatus(CheckinSession session, CheckinRecord.CheckinStatus status);

    /**
     * 检查学生是否已签到
     *
     * @param session 签到会话
     * @param student 学生信息
     * @return 是否已签到
     */
    boolean existsBySessionAndStudent(CheckinSession session, User student);
}
