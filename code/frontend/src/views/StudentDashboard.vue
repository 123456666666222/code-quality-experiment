<template>
  <div class="dashboard-container">
    <el-header class="header">
      <div class="header-left">
        <h2>学生工作台</h2>
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
          <el-table :data="courses" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="courseCode" label="课程编号" />
            <el-table-column prop="teacher.realName" label="授课教师" />
            <el-table-column prop="classTime" label="上课时间" />
            <el-table-column prop="location" label="上课地点" />
          </el-table>
        </el-tab-pane>

        <!-- 签到记录 -->
        <el-tab-pane label="签到记录" name="records">
          <el-table :data="checkinRecords" style="width: 100%">
            <el-table-column prop="session.course.courseName" label="课程名称" />
            <el-table-column prop="checkinTime" label="签到时间" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === 'SUCCESS' ? 'success' : row.status === 'LATE' ? 'warning' : 'danger'">
                  {{ row.status === 'SUCCESS' ? '正常' : row.status === 'LATE' ? '迟到' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 签到入口 -->
        <el-tab-pane label="签到" name="checkin">
          <div class="checkin-entry">
            <h3>输入签到码进行签到</h3>
            <el-input v-model="checkinCode" placeholder="请输入6位签到码" maxlength="6" class="checkin-input" />
            <el-button type="primary" @click="doCheckin" :loading="checkinLoading" class="checkin-button">
              签到
            </el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'StudentDashboard',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')),
      activeTab: 'courses',
      courses: [],
      checkinRecords: [],
      checkinCode: '',
      checkinLoading: false
    }
  },
  mounted() {
    this.loadCourses()
    this.loadCheckinRecords()
  },
  methods: {
    async loadCourses() {
      try {
        const res = await axios.get(`/api/courses/student/${this.user.id}`)
        if (res.data.code === 200) {
          this.courses = res.data.data
        }
      } catch (err) {
        this.$message.error('加载课程失败')
      }
    },
    async loadCheckinRecords() {
      try {
        const res = await axios.get(`/api/checkin/records/student/${this.user.id}`)
        if (res.data.code === 200) {
          this.checkinRecords = res.data.data
        }
      } catch (err) {
        this.$message.error('加载签到记录失败')
      }
    },
    async doCheckin() {
      if (!this.checkinCode || this.checkinCode.length !== 6) {
        this.$message.warning('请输入6位签到码')
        return
      }
      this.checkinLoading = true
      try {
        const res = await axios.post('/api/checkin/do', {
          checkinCode: this.checkinCode,
          studentId: this.user.id
        })
        if (res.data.code === 200) {
          this.$message.success('签到成功')
          this.checkinCode = ''
          this.loadCheckinRecords()
        } else {
          this.$message.error(res.data.message)
        }
      } catch (err) {
        this.$message.error(err.response?.data?.message || '签到失败')
      } finally {
        this.checkinLoading = false
      }
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

.checkin-entry {
  text-align: center;
  padding: 40px;
}

.checkin-entry h3 {
  margin-bottom: 30px;
  color: #333;
}

.checkin-input {
  width: 300px;
  margin-right: 20px;
}

.checkin-button {
  width: 120px;
}
</style>
