package com.checkin.controller;

import com.checkin.model.Course;
import com.checkin.model.User;
import com.checkin.service.CourseService;
import com.checkin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 课程控制器
 * 处理课程创建、查询、学生管理等请求
 *
 * @author 明建超
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    /**
     * 创建课程
     *
     * @param course 课程信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.create(course);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "课程创建成功",
                    "data", createdCourse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取课程信息
     *
     * @param id 课程ID
     * @return 课程信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        try {
            Course course = courseService.findById(id);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", course
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取所有课程
     *
     * @return 课程列表
     */
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<Course> courses = courseService.findAll();
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "data", courses
        ));
    }

    /**
     * 获取教师的课程列表
     *
     * @param teacherId 教师ID
     * @return 课程列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable Long teacherId) {
        try {
            User teacher = userService.findById(teacherId);
            List<Course> courses = courseService.findByTeacher(teacher);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", courses
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 获取学生的课程列表
     *
     * @param studentId 学生ID
     * @return 课程列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getCoursesByStudent(@PathVariable Long studentId) {
        try {
            User student = userService.findById(studentId);
            List<Course> courses = courseService.findByStudent(student);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "data", courses
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 更新课程信息
     *
     * @param id 课程ID
     * @param courseDetails 更新的课程信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        try {
            Course course = courseService.update(id, courseDetails);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "课程更新成功",
                    "data", course
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 添加学生到课程
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    @PostMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<?> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            User student = userService.findById(studentId);
            Course course = courseService.addStudent(courseId, student);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "学生添加成功",
                    "data", course
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 从课程中移除学生
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            User student = userService.findById(studentId);
            Course course = courseService.removeStudent(courseId, student);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "学生移除成功",
                    "data", course
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 删除课程
     *
     * @param id 课程ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.delete(id);
            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "message", "课程删除成功"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", e.getMessage()
            ));
        }
    }
}
