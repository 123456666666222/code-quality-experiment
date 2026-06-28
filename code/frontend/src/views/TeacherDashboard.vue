<template>
  <div class="dashboard-container">
    <el-header class="header">
      <div class="header-left">
        <h2>教师工作台</h2>
      </div>
      <div class="header-right">
        <span class="user-info">欢迎，{{ user.realName }}</span>
        <el-button type="text" @click="logout">退出登录</el-button>
      </div>
    </el-header>

    <el-main class="main-content">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 我的课程 -->
        <el-tab-pane label="我的课程" name="courses">
          <div class="tab-header">
            <el-button type="primary" @click="showCreateCourse">创建课程</el-button>
          </div>
          <el-table :data="courses" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="courseCode" label="课程编号" />
            <el-table-column prop="classTime" label="上课时间" />
            <el-table-column prop="location" label="上课地点" />
            <el-table-column label="操作" width="300">
              <template #default="{ row }">
                <el-button size="small" type="primary" @click="startCheckin(row)">发起签到</el-button>
                <el-button size="small" @click="viewStudents(row)">查看学生</el-button>
                <el-button size="small" type="danger" @click="deleteCourse(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 签到管理 -->
        <el-tab-pane label="签到管理" name="checkin">
          <el-table :data="checkinSessions" style="width: 100%">
            <el-table-column prop="course.courseName" label="课程名称" />
            <el-table-column prop="checkinCode" label="签到码" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
                  {{ row.status === 'ACTIVE' ? '进行中' : '已结束' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="actualCount" label="已签到" width="100" />
            <el-table-column prop="expectedCount" label="应签到" width="100" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button size="small" @click="viewStatistics(row)">查看统计</el-button>
                <el-button size="small" type="warning" @click="endCheckin(row)" v-if="row.status === 'ACTIVE'">结束签到</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-main>

    <!-- 创建课程对话框 -->
    <el-dialog title="创建课程" v-model="createCourseVisible" width="500px">
      <el-form :model="courseForm" :rules="courseRules" ref="courseForm">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="courseForm.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程编号" prop="courseCode">
          <el-input v-model="courseForm.courseCode" placeholder="请输入课程编号" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="courseForm.description" type="textarea" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="courseForm.classTime" placeholder="如：周一1-2节" />
        </el-form-item>
        <el-form-item label="上课地点">
          <el-input v-model="courseForm.location" placeholder="请输入上课地点" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createCourseVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateCourse">创建</el-button>
      </template>
    </el-dialog>

    <!-- 签到码显示对话框 -->
    <el-dialog title="签到码" v-model="checkinCodeVisible" width="400px">
      <div class="checkin-code-display">
        <div class="code-label">请将以下签到码发送给学生：</div>
        <div class="code-value">{{ currentCheckinCode }}</div>
        <div class="code-tip">签到码有效期：5分钟</div>
      </div>
      <template #footer>
        <el-button type="primary" @click="checkinCodeVisible = false">确定</el-button>
      </template>
    </el-dialog>

    <!-- 签到统计对话框 -->
    <el-dialog title="签到统计" v-model="statisticsVisible" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="课程名称">{{ statistics.courseName }}</el-descriptions-item>
        <el-descriptions-item label="应签到">{{ statistics.expectedCount }}</el-descriptions-item>
        <el-descriptions-item label="实签到">{{ statistics.actualCount }}</el-descriptions-item>
        <el-descriptions-item label="签到率">{{ statistics.checkinRate?.toFixed(1) }}%</el-descriptions-item>
        <el-descriptions-item label="迟到人数">{{ statistics.lateCount }}</el-descriptions-item>
        <el-descriptions-item label="缺勤人数">{{ statistics.absentCount }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="statistics.records" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="student.realName" label="学生姓名" />
        <el-table-column prop="student.username" label="学号" />
        <el-table-column prop="checkinTime" label="签到时间" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 'SUCCESS' ? 'success' : row.status === 'LATE' ? 'warning' : 'danger'">
              {{ row.status === 'SUCCESS' ? '正常' : row.status === 'LATE' ? '迟到' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="statisticsVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TeacherDashboard',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')),
      activeTab: 'courses',
      courses: [],
      checkinSessions: [],
      createCourseVisible: false,
      courseForm: {
        courseName: '',
        courseCode: '',
        description: '',
        classTime: '',
        location: ''
      },
      courseRules: {
        courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        courseCode: [{ required: true, message: '请输入课程编号', trigger: 'blur' }]
      },
      checkinCodeVisible: false,
      currentCheckinCode: '',
      statisticsVisible: false,
      statistics: {}
    }
  },
  mounted() {
    this.loadCourses()
    this.loadCheckinSessions()
  },
  methods: {
    async loadCourses() {
      try {
        const res = await axios.get(`/api/courses/teacher/${this.user.id}`)
        if (res.data.code === 200) {
          this.courses = res.data.data
        }
      } catch (err) {
        this.$message.error('加载课程失败')
      }
    },
    async loadCheckinSessions() {
      try {
        // 加载所有课程的签到会话
        for (const course of this.courses) {
          const res = await axios.get(`/api/checkin/sessions/course/${course.id}`)
          if (res.data.code === 200) {
            this.checkinSessions = [...this.checkinSessions, ...res.data.data]
          }
        }
      } catch (err) {
        this.$message.error('加载签到记录失败')
      }
    },
    showCreateCourse() {
      this.courseForm = {
        courseName: '',
        courseCode: '',
        description: '',
        classTime: '',
        location: ''
      }
      this.createCourseVisible = true
    },
    async handleCreateCourse() {
      this.$refs.courseForm.validate(async (valid) => {
        if (!valid) return
        try {
          const courseData = {
            ...this.courseForm,
            teacher: { id: this.user.id }
          }
          const res = await axios.post('/api/courses', courseData)
          if (res.data.code === 200) {
            this.$message.success('课程创建成功')
            this.createCourseVisible = false
            this.loadCourses()
          } else {
            this.$message.error(res.data.message)
          }
        } catch (err) {
          this.$message.error('创建课程失败')
        }
      })
    },
    async startCheckin(course) {
      try {
        const res = await axios.post('/api/checkin/start', {
          courseId: course.id,
          teacherId: this.user.id
        })
        if (res.data.code === 200) {
          this.currentCheckinCode = res.data.data.checkinCode
          this.checkinCodeVisible = true
          this.loadCheckinSessions()
        } else {
          this.$message.error(res.data.message)
        }
      } catch (err) {
        this.$message.error(err.response?.data?.message || '发起签到失败')
      }
    },
    async endCheckin(session) {
      try {
        const res = await axios.post(`/api/checkin/end/${session.id}/${this.user.id}`)
        if (res.data.code === 200) {
          this.$message.success('签到已结束')
          this.loadCheckinSessions()
        } else {
          this.$message.error(res.data.message)
        }
      } catch (err) {
        this.$message.error('结束签到失败')
      }
    },
    async viewStatistics(session) {
      try {
        const res = await axios.get(`/api/checkin/statistics/${session.id}`)
        if (res.data.code === 200) {
          this.statistics = res.data.data
          this.statisticsVisible = true
        }
      } catch (err) {
        this.$message.error('获取统计信息失败')
      }
    },
    async deleteCourse(course) {
      try {
        await this.$confirm('确定要删除该课程吗？', '提示', {
          type: 'warning'
        })
        const res = await axios.delete(`/api/courses/${course.id}`)
        if (res.data.code === 200) {
          this.$message.success('删除成功')
          this.loadCourses()
        }
      } catch (err) {
        if (err !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    viewStudents(course) {
      // TODO: 实现查看学生功能
      this.$message.info('查看学生功能开发中')
    },
    logout() {
      localStorage.removeItem('user')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  color: #666;
}

.main-content {
  padding: 20px;
}

.tab-header {
  margin-bottom: 20px;
}

.checkin-code-display {
  text-align: center;
  padding: 20px;
}

.code-label {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.code-value {
  font-size: 48px;
  font-weight: bold;
  color: #409eff;
  letter-spacing: 8px;
  margin-bottom: 20px;
}

.code-tip {
  font-size: 14px;
  color: #999;
}
</style>
