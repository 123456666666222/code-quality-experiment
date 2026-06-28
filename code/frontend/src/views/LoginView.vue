<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">课程签到系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名（工号/学号）" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" class="login-button">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <el-button type="text" @click="showRegister">注册新账号</el-button>
      </div>
    </div>

    <!-- 注册对话框 -->
    <el-dialog title="注册新账号" v-model="registerVisible" width="400px">
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名（工号/学号）" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="registerForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择角色">
            <el-option label="教师" value="TEACHER" />
            <el-option label="学生" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="className" v-if="registerForm.role === 'STUDENT'">
          <el-input v-model="registerForm.className" placeholder="请输入班级" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="registerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegister" :loading="registerLoading">注册</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginView',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false,
      registerVisible: false,
      registerForm: {
        username: '',
        password: '',
        realName: '',
        role: '',
        className: ''
      },
      registerRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      registerLoading: false
    }
  },
  methods: {
    async handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const res = await axios.post('/api/users/login', this.loginForm)
          if (res.data.code === 200) {
            localStorage.setItem('user', JSON.stringify(res.data.data))
            this.$message.success('登录成功')
            if (res.data.data.role === 'TEACHER') {
              this.$router.push('/teacher')
            } else {
              this.$router.push('/student')
            }
          } else {
            this.$message.error(res.data.message)
          }
        } catch (err) {
          this.$message.error(err.response?.data?.message || '登录失败')
        } finally {
          this.loading = false
        }
      })
    },
    showRegister() {
      this.registerVisible = true
    },
    async handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (!valid) return
        this.registerLoading = true
        try {
          const res = await axios.post('/api/users/register', this.registerForm)
          if (res.data.code === 200) {
            this.$message.success('注册成功，请登录')
            this.registerVisible = false
            this.loginForm.username = this.registerForm.username
          } else {
            this.$message.error(res.data.message)
          }
        } catch (err) {
          this.$message.error(err.response?.data?.message || '注册失败')
        } finally {
          this.registerLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}
</style>
