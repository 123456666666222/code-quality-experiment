package com.checkin.service;

import com.checkin.model.Course;
import com.checkin.model.User;
import com.checkin.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 课程业务逻辑服务类
 *
 * @author 明建超
 * @version 1.0.0
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * 创建课程
     *
     * @param course 课程信息
     * @return 创建成功的课程
     * @throws RuntimeException 如果课程编号已存在
     */
    public Course create(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("课程编号已存在");
        }
        return courseRepository.save(course);
    }

    /**
     * 根据ID查找课程
     *
     * @param id 课程ID
     * @return 课程信息
     */
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
    }

    /**
     * 根据课程编号查找课程
     *
     * @param courseCode 课程编号
     * @return 课程信息
     */
    public Course findByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        return course;
    }

    /**
     * 获取教师的所有课程
     *
     * @param teacher 教师信息
     * @return 课程列表
     */
    public List<Course> findByTeacher(User teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    /**
     * 获取学生的所有课程
     *
     * @param student 学生信息
     * @return 课程列表
     */
    public List<Course> findByStudent(User student) {
        return courseRepository.findByStudent(student);
    }

    /**
     * 获取所有课程
     *
     * @return 课程列表
     */
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    /**
     * 更新课程信息
     *
     * @param id 课程ID
     * @param courseDetails 更新的课程信息
     * @return 更新后的课程
     */
    public Course update(Long id, Course courseDetails) {
        Course course = findById(id);
        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setClassTime(courseDetails.getClassTime());
        course.setLocation(courseDetails.getLocation());
        course.setEnabled(courseDetails.getEnabled());
        return courseRepository.save(course);
    }

    /**
     * 添加学生到课程
     *
     * @param courseId 课程ID
     * @param student 学生信息
     * @return 更新后的课程
     */
    public Course addStudent(Long courseId, User student) {
        Course course = findById(courseId);
        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
            return courseRepository.save(course);
        }
        return course;
    }

    /**
     * 从课程中移除学生
     *
     * @param courseId 课程ID
     * @param student 学生信息
     * @return 更新后的课程
     */
    public Course removeStudent(Long courseId, User student) {
        Course course = findById(courseId);
        course.getStudents().remove(student);
        return courseRepository.save(course);
    }

    /**
     * 删除课程
     *
     * @param id 课程ID
     */
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
