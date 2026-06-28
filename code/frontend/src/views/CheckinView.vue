<template>
  <div class="checkin-container">
    <el-card class="checkin-card">
      <h2 class="checkin-title">课程签到</h2>
      <div class="checkin-info">
        <p>请输入教师提供的签到码完成签到</p>
      </div>
      <el-form :model="checkinForm" :rules="rules" ref="checkinForm" class="checkin-form">
        <el-form-item prop="checkinCode">
          <el-input 
            v-model="checkinForm.checkinCode" 
            placeholder="请输入6位签到码" 
            maxlength="6"
            size="large"
            class="checkin-input"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleCheckin" :loading="loading" size="large" class="checkin-button">
            签到
          </el-button>
        </el-form-item>
      </el-form>
      <div class="checkin-footer">
        <el-button type="text" @click="goBack">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CheckinView',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')),
      checkinForm: {
        checkinCode: ''
      },
      rules: {
        checkinCode: [
          { required: true, message: '请输入签到码', trigger: 'blur' },
          { len: 6, message: '签到码为6位数字', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    async handleCheckin() {
      this.$refs.checkinForm.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const res = await axios.post('/api/checkin/do', {
            checkinCode: this.checkinForm.checkinCode,
            studentId: this.user.id
          })
          if (res.data.code === 200) {
            this.$message.success('签到成功')
            this.$router.push('/student')
          } else {
            this.$message.error(res.data.message)
          }
        } catch (err) {
          this.$message.error(err.response?.data?.message || '签到失败')
        } finally {
          this.loading = false
        }
      })
    },
    goBack() {
      this.$router.push('/student')
    }
  }
}
</script>

<style scoped>
.checkin-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.checkin-card {
  width: 400px;
  padding: 40px;
  border-radius: 12px;
}

.checkin-title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.checkin-info {
  text-align: center;
  margin-bottom: 30px;
  color: #666;
}

.checkin-form {
  width: 100%;
}

.checkin-input {
  text-align: center;
}

.checkin-button {
  width: 100%;
}

.checkin-footer {
  text-align: center;
  margin-top: 20px;
}
</style>
