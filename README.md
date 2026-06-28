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