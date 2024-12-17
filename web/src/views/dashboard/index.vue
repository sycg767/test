<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 欢迎卡片 -->
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统信息</span>
            </div>
          </template>
          <div class="welcome-info">
            <h2>欢迎使用题库管理系统</h2>
            <p>当前用户：{{ userInfo.nickname }}</p>
            <p>登录时间：{{ formatDate(userInfo.lastLoginTime) }}</p>
            <p>角色权限：{{ formatRole(userInfo.roles) }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <!-- 快速导航卡片 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快速导航</span>
            </div>
          </template>
          <div class="quick-nav">
            <el-button-group>
              <el-button type="primary" @click="handleNavClick('题库管理')">
                <el-icon><Document /></el-icon>
                题���管理
              </el-button>
              <el-button type="success" @click="handleNavClick('知识点管理')">
                <el-icon><Collection /></el-icon>
                知识点管理
              </el-button>
              <el-button type="warning" @click="handleNavClick('分类管理')">
                <el-icon><Files /></el-icon>
                分类管理
              </el-button>
            </el-button-group>
          </div>
        </el-card>
      </el-col>

      <!-- 系统状态卡片 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统状态</span>
            </div>
          </template>
          <div class="system-status">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="系统版本">V1.0.0</el-descriptions-item>
              <el-descriptions-item label="运行环境">{{ env }}</el-descriptions-item>
              <el-descriptions-item label="更新时间">{{ formatDate(new Date()) }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Document, Collection, Files } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userInfo = ref({
  nickname: '',
  lastLoginTime: '',
  roles: ''
})

const env = process.env.NODE_ENV || 'development'

onMounted(() => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
})

const formatDate = (date: string | Date) => {
  if (!date) return ''
  return new Date(date).toLocaleString()
}

const formatRole = (roles: string) => {
  if (!roles) return ''
  const roleMap: Record<string, string> = {
    'ADMIN': '超级管理员',
    'OPERATOR': '运营人员'
  }
  return roleMap[roles] || roles
}

const handleNavClick = (target: string) => {
  const routeMap: Record<string, string> = {
    '题库管理': '/question-bank/list',
    '知识点管理': '/knowledge/list',
    '分类管理': '/category/list'
  }
  
  const route = routeMap[target]
  if (route) {
    router.push(route)
  } else {
    ElMessage.warning('功能开发中...')
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  .mt-20 {
    margin-top: 20px;
  }

  .welcome-info {
    h2 {
      margin-bottom: 20px;
      color: #303133;
    }
    p {
      margin: 10px 0;
      color: #606266;
      font-size: 14px;
    }
  }

  .quick-nav {
    .el-button-group {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
      
      .el-button {
        display: flex;
        align-items: center;
        gap: 5px;
        padding: 12px 20px;
        
        .el-icon {
          margin-right: 5px;
        }
      }
    }
  }

  .system-status {
    .el-descriptions {
      margin: 0;
    }
  }

  :deep(.el-card__header) {
    padding: 15px 20px;
    border-bottom: 1px solid #ebeef5;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      span {
        font-size: 16px;
        font-weight: bold;
        color: #303133;
      }
    }
  }
}
</style> 