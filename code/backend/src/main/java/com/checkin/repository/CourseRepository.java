package com.checkin.repository;

import com.checkin.model.Course;
import com.checkin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 课程数据访问接口
 *
 * @author 明建超
 * @version 1.0.0
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * 根据教师查找课程列表
     *
     * @param teacher 教师信息
     * @return 课程列表
     */
    List<Course> findByTeacher(User teacher);

    /**
     * 根据课程编号查找课程
     *
     * @param courseCode 课程编号
     * @return 课程信息
     */
    Course findByCourseCode(String courseCode);

    /**
     * 根据学生查找课程列表
     *
     * @param student 学生信息
     * @return 课程列表
     */
    @org.springframework.data.jpa.repository.Query("SELECT c FROM Course c JOIN c.students s WHERE s = :student")
    List<Course> findByStudent(@org.springframework.data.repository.query.Param("student") User student);

    /**
     * 检查课程编号是否存在
     *
     * @param courseCode 课程编号
     * @return 是否存在
     */
    boolean existsByCourseCode(String courseCode);
}
