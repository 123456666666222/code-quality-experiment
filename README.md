# 课程签到系统

一个基于 Spring Boot + Vue.js 的课程签到管理系统，支持教师创建课程、发起签到，学生扫码签到等功能。

## 功能特性

- 用户注册登录（教师/学生角色）
- 课程管理（创建、查询、删除）
- 签到管理（发起签到、学生签到、签到统计）
- 实时签到状态显示

## 技术栈

**后端：**
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Lombok

**前端：**
- Vue 3
- Vue Router
- Vite
- Axios

## 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+

## 快速开始

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE course_checkin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化脚本
source code/backend/sql/init.sql
```

### 2. 启动后端

```bash
cd code/backend

# 修改数据库配置（如果需要）
# 编辑 src/main/resources/application.yml

# 编译打包
mvn clean package -DskipTests

# 启动应用
java -jar target/course-checkin-system-1.0.0.jar
```

后端将运行在 http://localhost:8080

### 3. 启动前端

```bash
cd code/frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端将运行在 http://localhost:5173

### 4. 访问系统

打开浏览器访问 http://localhost:5173

**测试账号：**
- 教师账号：teacher / 123456
- 学生账号：student / 123456

## 项目结构

```
course-checkin-system/
├── code/
│   ├── backend/                    # 后端代码
│   │   ├── pom.xml                # Maven配置
│   │   ├── sql/                   # 数据库脚本
│   │   └── src/
│   │       └── main/
│   │           ├── java/com/checkin/
│   │           │   ├── controller/    # 控制器层
│   │           │   ├── model/         # 实体类
│   │           │   ├── repository/    # 数据访问层
│   │           │   └── service/       # 业务逻辑层
│   │           └── resources/
│   │               └── application.yml
│   └── frontend/                   # 前端代码
│       ├── package.json
│       ├── vite.config.js
│       └── src/
│           ├── App.vue
│           ├── main.js
│           ├── router/            # 路由配置
│           └── views/             # 页面组件
├── docs/                          # 实验报告
└── README.md                      # 项目说明
```

## API 接口

### 用户模块
- POST /api/users/register - 用户注册
- POST /api/users/login - 用户登录
- GET /api/users/{id} - 获取用户信息

### 课程模块
- POST /api/courses - 创建课程
- GET /api/courses - 获取课程列表
- GET /api/courses/{id} - 获取课程详情
- DELETE /api/courses/{id} - 删除课程

### 签到模块
- POST /api/checkin/sessions - 创建签到会话
- POST /api/checkin/sessions/{sessionId}/checkin - 学生签到
- GET /api/checkin/sessions/{sessionId}/records - 获取签到记录

## 常见问题

### Q: 数据库连接失败？
A: 检查 `application.yml` 中的数据库配置，确保 MySQL 服务已启动。

### Q: 前端无法连接后端？
A: 检查后端是否正常运行，查看浏览器控制台是否有跨域错误。

### Q: 端口被占用？
A: 修改 `application.yml` 中的 `server.port` 或 `vite.config.js` 中的 `server.port`。

## 开发说明

- 后端使用 Spring Data JPA，启动时会自动创建表结构
- 前端使用 Vite 开发服务器，支持热重载
- 签到码默认5分钟过期，可在配置文件中修改

## License

MIT License
