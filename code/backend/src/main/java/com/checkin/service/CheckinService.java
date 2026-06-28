package com.checkin.service;

import com.checkin.model.*;
import com.checkin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Optional;

/**
 * 签到业务逻辑服务类
 * 处理签到会话的创建、签到码生成、学生签到等核心业务
 *
 * @author 明建超
 * @version 1.0.0
 */
@Service
public class CheckinService {

    private final CheckinSessionRepository sessionRepository;
    private final CheckinRecordRepository recordRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Value("${app.checkin-code-expire:5}")
    private int checkinCodeExpireMinutes;

    @Value("${app.checkin-code-length:6}")
    private int checkinCodeLength;

    @Autowired
    public CheckinService(CheckinSessionRepository sessionRepository,
                          CheckinRecordRepository recordRepository,
                          CourseRepository courseRepository,
                          UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    /**
     * 教师发起签到
     *
     * @param courseId 课程ID
     * @param teacherId 教师ID
     * @return 签到会话信息
     */
    public CheckinSession startCheckin(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));

        // 检查是否有进行中的签到
        List<CheckinSession> activeSessions = sessionRepository.findByCourseAndStatus(course, CheckinSession.SessionStatus.ACTIVE);
        if (!activeSessions.isEmpty()) {
            throw new RuntimeException("该课程已有进行中的签到");
        }

        // 创建签到会话
        CheckinSession session = new CheckinSession();
        session.setCourse(course);
        session.setTeacher(teacher);
        session.setCheckinCode(generateCheckinCode());
        session.setStartTime(LocalDateTime.now());
        session.setStatus(CheckinSession.SessionStatus.ACTIVE);
        session.setExpectedCount(course.getStudents().size());

        return sessionRepository.save(session);
    }

    /**
     * 生成签到码
     *
     * @return 签到码
     */
    private String generateCheckinCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < checkinCodeLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 学生签到
     *
     * @param checkinCode 签到码
     * @param studentId 学生ID
     * @return 签到记录
     */
    public CheckinRecord doCheckin(String checkinCode, Long studentId) {
        // 查找签到会话
        Optional<CheckinSession> sessionOpt = sessionRepository.findByCheckinCode(checkinCode);
        if (sessionOpt.isEmpty()) {
            throw new RuntimeException("签到码无效");
        }

        CheckinSession session = sessionOpt.get();

        // 检查签到会话状态
        if (session.getStatus() != CheckinSession.SessionStatus.ACTIVE) {
            throw new RuntimeException("签到已结束");
        }

        // 检查签到码是否过期
        if (session.getStartTime().plusMinutes(checkinCodeExpireMinutes).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("签到码已过期");
        }

        // 查找学生
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        // 检查学生是否已签到
        if (recordRepository.existsBySessionAndStudent(session, student)) {
            throw new RuntimeException("您已签到，请勿重复签到");
        }

        // 检查学生是否在课程中
        if (!session.getCourse().getStudents().contains(student)) {
            throw new RuntimeException("您未选修该课程");
        }

        // 创建签到记录
        CheckinRecord record = new CheckinRecord();
        record.setSession(session);
        record.setStudent(student);
        record.setCheckinTime(LocalDateTime.now());
        record.setStatus(CheckinRecord.CheckinStatus.SUCCESS);

        // 更新签到会话的实到人数
        session.setActualCount(session.getActualCount() + 1);
        sessionRepository.save(session);

        return recordRepository.save(record);
    }

    /**
     * 结束签到
     *
     * @param sessionId 签到会话ID
     * @param teacherId 教师ID
     * @return 签到会话信息
     */
    public CheckinSession endCheckin(Long sessionId, Long teacherId) {
        CheckinSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("签到会话不存在"));

        // 检查教师权限
        if (!session.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("无权操作此签到会话");
        }

        session.setStatus(CheckinSession.SessionStatus.CLOSED);
        session.setEndTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    /**
     * 根据ID查找签到会话
     *
     * @param id 签到会话ID
     * @return 签到会话信息
     */
    public CheckinSession findSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("签到会话不存在"));
    }

    /**
     * 获取课程的签到会话列表
     *
     * @param courseId 课程ID
     * @return 签到会话列表
     */
    public List<CheckinSession> findSessionsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
        return sessionRepository.findByCourseOrderByStartTimeDesc(course);
    }

    /**
     * 获取签到会话的签到记录
     *
     * @param sessionId 签到会话ID
     * @return 签到记录列表
     */
    public List<CheckinRecord> findRecordsBySession(Long sessionId) {
        CheckinSession session = findSessionById(sessionId);
        return recordRepository.findBySession(session);
    }

    /**
     * 获取学生的签到记录
     *
     * @param studentId 学生ID
     * @return 签到记录列表
     */
    public List<CheckinRecord> findRecordsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("学生不存在"));
        return recordRepository.findByStudentOrderByCheckinTimeDesc(student);
    }

    /**
     * 统计签到会话的签到情况
     *
     * @param sessionId 签到会话ID
     * @return 签到统计信息
     */
    public java.util.Map<String, Object> getCheckinStatistics(Long sessionId) {
        CheckinSession session = findSessionById(sessionId);
        List<CheckinRecord> records = recordRepository.findBySession(session);

        long successCount = recordRepository.countBySessionAndStatus(session, CheckinRecord.CheckinStatus.SUCCESS);
        long lateCount = recordRepository.countBySessionAndStatus(session, CheckinRecord.CheckinStatus.LATE);

        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("sessionId", sessionId);
        statistics.put("courseName", session.getCourse().getCourseName());
        statistics.put("expectedCount", session.getExpectedCount());
        statistics.put("actualCount", session.getActualCount());
        statistics.put("successCount", successCount);
        statistics.put("lateCount", lateCount);
        statistics.put("absentCount", session.getExpectedCount() - session.getActualCount());
        statistics.put("checkinRate", session.getExpectedCount() > 0 ?
                (double) session.getActualCount() / session.getExpectedCount() * 100 : 0);
        statistics.put("records", records);

        return statistics;
    }
}
