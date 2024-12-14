<template>
  <div class="error-page">
    <div class="error-code">404</div>
    <div class="error-desc">抱歉，你访问的页面不存在</div>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<style lang="scss" scoped>
.error-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
  
  .error-code {
    font-size: 120px;
    font-weight: bold;
    color: #1890ff;
  }
  
  .error-desc {
    font-size: 24px;
    color: #666;
    margin: 24px 0 32px;
  }
}
</style> 