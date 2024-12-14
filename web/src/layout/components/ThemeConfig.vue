<template>
  <el-drawer
    v-model="visible"
    title="主题配置"
    size="300px"
  >
    <div class="theme-config">
      <h3>主题色</h3>
      <el-color-picker
        v-model="primaryColor"
        :predefine="predefineColors"
        show-alpha
        @change="handleColorChange"
      />

      <h3>导航模式</h3>
      <el-radio-group v-model="layout">
        <el-radio-button label="side">侧边菜单</el-radio-button>
        <el-radio-button label="top">顶部菜单</el-radio-button>
      </el-radio-group>

      <h3>系统风格</h3>
      <el-radio-group v-model="style">
        <el-radio-button label="light">明亮模式</el-radio-button>
        <el-radio-button label="dark">暗黑模式</el-radio-button>
      </el-radio-group>

      <h3>界面功能</h3>
      <el-checkbox v-model="fixedHeader">固定 Header</el-checkbox>
      <el-checkbox v-model="sidebarLogo">显示 Logo</el-checkbox>
      <el-checkbox v-model="breadcrumb">显示面包屑</el-checkbox>
      <el-checkbox v-model="tagsView">显示标签栏</el-checkbox>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useTheme } from '@/styles/theme'

const visible = ref(false)
const { primaryColor, setPrimaryColor } = useTheme()

// 预定义颜色
const predefineColors = [
  '#1890ff',
  '#409EFF',
  '#13C2C2',
  '#52C41A',
  '#F5222D',
  '#FA541C',
  '#FA8C16',
  '#FAAD14'
]

// 布局设置
const layout = ref('side')
const style = ref('light')
const fixedHeader = ref(true)
const sidebarLogo = ref(true)
const breadcrumb = ref(true)
const tagsView = ref(true)

// 主题色变化
const handleColorChange = (color: string) => {
  setPrimaryColor(color)
}

defineExpose({
  show: () => visible.value = true
})
</script>

<style lang="scss" scoped>
.theme-config {
  padding: 20px;
  
  h3 {
    margin: 20px 0 12px;
    font-size: 14px;
    color: #606266;
  }
  
  .el-radio-group {
    width: 100%;
    margin-bottom: 20px;
    
    .el-radio-button {
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
  
  .el-checkbox {
    display: block;
    margin-bottom: 12px;
  }
}
</style> 