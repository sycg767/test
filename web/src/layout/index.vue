<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="logo">
        <span>题库管理系统</span>
      </div>
      <el-menu
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <sidebar-item
          v-for="route in filteredRoutes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </div>

    <!-- 主要内容区 -->
    <div class="main-container">
      <!-- 头部导航 -->
      <div class="navbar">
        <div class="left-menu">
          <breadcrumb />
        </div>
        <div class="right-menu">
          <el-dropdown trigger="click">
            <div class="avatar-wrapper">
              <el-avatar :size="32" :src="userInfo.avatar">
                {{ userInfo.nickname?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="user-name">{{ userInfo.nickname }}</span>
              <el-icon><CaretBottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleChangePassword">
                  <el-icon><Key /></el-icon>修改密码
                </el-dropdown-item>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 主要内容 -->
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <change-password-dialog
      v-model="changePasswordVisible"
      @success="handlePasswordChanged"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { CaretBottom, Key, SwitchButton } from '@element-plus/icons-vue'
import { routes } from '@/router/routes'
import SidebarItem from './components/SidebarItem.vue'
import Breadcrumb from './components/Breadcrumb.vue'
import ChangePasswordDialog from './components/ChangePasswordDialog.vue'

const router = useRouter()
const route = useRoute()

const userInfo = ref({
  nickname: '',
  avatar: ''
})

const changePasswordVisible = ref(false)

// 过滤掉不需要显示在菜单中的路由
const filteredRoutes = computed(() => {
  return routes.filter(route => !route.meta?.hidden)
})

// 从localStorage获取用户信息
const initUserInfo = () => {
  const storedInfo = localStorage.getItem('userInfo')
  if (storedInfo) {
    userInfo.value = JSON.parse(storedInfo)
  }
}

// 修改密码
const handleChangePassword = () => {
  changePasswordVisible.value = true
}

const handlePasswordChanged = () => {
  ElMessage.success('密码修改成功')
  changePasswordVisible.value = false
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    })
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
    ElMessage.success('退出成功')
  } catch {
    // 取消退出
  }
}

// 初始化
initUserInfo()
</script>

<style lang="scss" scoped>
.app-wrapper {
  height: 100vh;
  display: flex;

  .sidebar-container {
    width: 210px;
    height: 100%;
    background-color: #304156;
    transition: width 0.3s;
    overflow-x: hidden;
    
    .logo {
      height: 50px;
      line-height: 50px;
      text-align: center;
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      border-bottom: 1px solid #1f2d3d;
    }
  }

  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    background-color: #f0f2f5;

    .navbar {
      height: 50px;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0,21,41,.08);
      display: flex;
      align-items: center;
      padding: 0 16px;

      .left-menu {
        flex: 1;
      }

      .right-menu {
        .avatar-wrapper {
          display: flex;
          align-items: center;
          cursor: pointer;
          padding: 0 8px;
          
          .user-name {
            margin: 0 8px;
            color: #333;
          }
        }
      }
    }

    .app-main {
      flex: 1;
      padding: 16px;
      overflow-y: auto;
    }
  }
}

// 路由切换动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style> 