<template>
  <div class="user-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增用户
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stat-cards">
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>用户总数</span>
            <el-tag>{{ stats.total || 0 }}</el-tag>
          </div>
        </template>
        <div class="card-content">
          <div class="stat-item">
            <span>活跃用户</span>
            <span>{{ stats.activeCount || 0 }}</span>
          </div>
          <div class="stat-item">
            <span>今日活跃</span>
            <span>{{ stats.todayActiveCount || 0 }}</span>
          </div>
        </div>
      </el-card>

      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>角色分布</span>
          </div>
        </template>
        <div class="card-content">
          <v-chart :option="roleChartOption" autoresize />
        </div>
      </el-card>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams">
        <el-form-item>
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索用户名/昵称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryParams.status" placeholder="用户状态" clearable>
            <el-option label="正常" value="ACTIVE" />
            <el-option label="禁用" value="DISABLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryParams.role" placeholder="用户角色" clearable>
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 用户列表 -->
    <el-table
      v-loading="loading"
      :data="userList"
      border
      style="width: 100%"
    >
      <el-table-column label="用户信息" min-width="200">
        <template #default="{ row }">
          <div class="user-info">
            <el-avatar :size="32" :src="row.avatar">
              {{ row.nickname.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="user-detail">
              <div class="nickname">{{ row.nickname }}</div>
              <div class="username">{{ row.username }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="角色" width="150">
        <template #default="{ row }">
          <el-tag 
            v-for="role in row.roles" 
            :key="role"
            :type="role === 'ADMIN' ? 'danger' : ''"
            size="small"
            class="role-tag"
          >
            {{ getRoleLabel(role) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column prop="phone" label="手机号" width="120" />
      
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
            {{ row.status === 'ACTIVE' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="lastLoginTime" label="最后登录" width="180" />
      
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="warning" link @click="handleResetPassword(row)">
            重置密码
          </el-button>
          <el-popconfirm
            :title="`确定要${row.status === 'ACTIVE' ? '禁用' : '启用'}该用户吗？`"
            @confirm="handleToggleStatus(row)"
          >
            <template #reference>
              <el-button :type="row.status === 'ACTIVE' ? 'danger' : 'success'" link>
                {{ row.status === 'ACTIVE' ? '禁用' : '启用' }}
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 用户表单对话框 -->
    <user-form-dialog
      v-if="dialogVisible"
      v-model="dialogVisible"
      :type="dialogType"
      :data="currentUser"
      @success="handleFormSuccess"
    />

    <!-- 重置密码对话框 -->
    <reset-password-dialog
      v-if="resetPasswordVisible"
      v-model="resetPasswordVisible"
      :user="currentUser"
      @success="handleResetSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUsers, getUserStats, updateUser } from '@/api/user'
import type { User } from '@/api/user'
import UserFormDialog from './components/UserFormDialog.vue'
import ResetPasswordDialog from './components/ResetPasswordDialog.vue'
import { use } from 'echarts/core'
import { PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import VChart from 'vue-echarts'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
])

// 数据
const loading = ref(false)
const userList = ref<User[]>([])
const total = ref(0)
const stats = ref({
  total: 0,
  activeCount: 0,
  todayActiveCount: 0,
  roleDistribution: {} as Record<string, number>
})

// 查询参数
const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  status: undefined as string | undefined,
  role: undefined as string | undefined
})

// 对话框控制
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const currentUser = ref<User>()
const resetPasswordVisible = ref(false)

// 角色分布图表配置
const roleChartOption = computed(() => {
  const { roleDistribution } = stats.value
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: Object.entries(roleDistribution).map(([role, count]) => ({
          name: getRoleLabel(role),
          value: count
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})

// 加载用户列表
const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getUsers(queryParams)
    userList.value = res.content
    total.value = res.total
  } catch (error: any) {
    ElMessage.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 加载统计数据
const loadStats = async () => {
  try {
    const data = await getUserStats()
    stats.value = data
  } catch (error: any) {
    ElMessage.error(error.message || '获取统计数据失败')
  }
}

// 搜索
const handleSearch = () => {
  queryParams.page = 1
  loadUsers()
}

// 分页处理
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  queryParams.page = 1
  loadUsers()
}

const handleCurrentChange = (val: number) => {
  queryParams.page = val
  loadUsers()
}

// 新增用户
const handleAdd = () => {
  dialogType.value = 'add'
  currentUser.value = undefined
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (user: User) => {
  dialogType.value = 'edit'
  currentUser.value = user
  dialogVisible.value = true
}

// 重置密码
const handleResetPassword = (user: User) => {
  currentUser.value = user
  resetPasswordVisible.value = true
}

// 启用/禁用用户
const handleToggleStatus = async (user: User) => {
  try {
    await updateUser(user.id, {
      status: user.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE'
    })
    ElMessage.success('操作成功')
    loadUsers()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

// 表单提交成功
const handleFormSuccess = () => {
  dialogVisible.value = false
  loadUsers()
  loadStats()
}

// 重置密码成功
const handleResetSuccess = () => {
  resetPasswordVisible.value = false
}

// 获取角色标签
const getRoleLabel = (role: string) => {
  const roles = {
    ADMIN: '管理员',
    USER: '普通用户'
  }
  return roles[role] || role
}

onMounted(() => {
  loadUsers()
  loadStats()
})
</script>

<style scoped>
.user-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .card-content {
    min-height: 200px;
  }
  
  .stat-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.search-bar {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-detail {
  .nickname {
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .username {
    font-size: 12px;
    color: #909399;
  }
}

.role-tag {
  margin-right: 4px;
  
  &:last-child {
    margin-right: 0;
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 