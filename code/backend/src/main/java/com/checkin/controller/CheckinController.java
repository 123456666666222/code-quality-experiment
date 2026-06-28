package com.checkin.controller;

import com.checkin.model.CheckinRecord;
import com.checkin.model.CheckinSession;
import com.checkin.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 签到控制器
 * 处理签到会话创建、学生签到、签到记录查询等请求
 *
 * @author 明建超
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/checkin")
@CrossOrigin(origins = "*")
public class CheckinController {

    private final CheckinService checkinService;

    @Autowired
    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    /**
     * 教师发起签到
     *
     * @param request 请求参数（包含课程ID和教师ID）
     * @return 签到会话信息
     */
    @PostMapping("/start")
    public ResponseEntity<?> startCheckin(@RequestBody Map<String, Long> request) {
        try {
            Long courseId = request.get("courseId");
            Long teacherId = request.get("teacherId");
            CheckinSession session = checkinService.startCheckin(courseId, teacherId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "签到已发起",
                    "data", session
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 学生签到
     *
     * @param request 请求参数（包含签到码和学生ID）
     * @return 签到记录
     */
    @PostMapping("/do")
    public ResponseEntity<?> doCheckin(@RequestBody Map<String, Object> request) {
        try {
            String checkinCode = (String) request.get("checkinCode");
            Long studentId = Long.valueOf(request.get("studentId").toString());
            CheckinRecord record = checkinService.doCheckin(checkinCode, studentId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "签到成功",
                    "data", record
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 结束签到
     *
     * @param sessionId 签到会话ID
     * @param teacherId 教师ID
     * @return 签到会话信息
     */
    @PostMapping("/end/{sessionId}/{teacherId}")
    public ResponseEntity<?> endCheckin(@PathVariable Long sessionId, @PathVariable Long teacherId) {
        try {
            CheckinSession session = checkinService.endCheckin(sessionId, teacherId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "签到已结束",
                    "data", session
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取签到会话信息
     *
     * @param sessionId 签到会话ID
     * @return 签到会话信息
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable Long sessionId) {
        try {
            CheckinSession session = checkinService.findSessionById(sessionId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", session
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取课程的签到会话列表
     *
     * @param courseId 课程ID
     * @return 签到会话列表
     */
    @GetMapping("/sessions/course/{courseId}")
    public ResponseEntity<?> getSessionsByCourse(@PathVariable Long courseId) {
        try {
            List<CheckinSession> sessions = checkinService.findSessionsByCourse(courseId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", sessions
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取签到会话的签到记录
     *
     * @param sessionId 签到会话ID
     * @return 签到记录列表
     */
    @GetMapping("/records/session/{sessionId}")
    public ResponseEntity<?> getRecordsBySession(@PathVariable Long sessionId) {
        try {
            List<CheckinRecord> records = checkinService.findRecordsBySession(sessionId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", records
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取学生的签到记录
     *
     * @param studentId 学生ID
     * @return 签到记录列表
     */
    @GetMapping("/records/student/{studentId}")
    public ResponseEntity<?> getRecordsByStudent(@PathVariable Long studentId) {
        try {
            List<CheckinRecord> records = checkinService.findRecordsByStudent(studentId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", records
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取签到统计信息
     *
     * @param sessionId 签到会话ID
     * @return 签到统计信息
     */
    @GetMapping("/statistics/{sessionId}")
    public ResponseEntity<?> getStatistics(@PathVariable Long sessionId) {
        try {
            Map<String, Object> statistics = checkinService.getCheckinStatistics(sessionId);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", statistics
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }
}
