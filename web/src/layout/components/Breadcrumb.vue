<template>
  <el-breadcrumb separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item
        v-for="(item, index) in breadcrumbs"
        :key="item.path"
      >
        <span
          v-if="item.redirect === 'noRedirect' || index === breadcrumbs.length - 1"
          class="no-redirect"
        >
          {{ item.meta?.title }}
        </span>
        <a
          v-else
          @click.prevent="handleLink(item)"
        >
          {{ item.meta?.title }}
        </a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { RouteLocationMatched } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 面包屑数据
const breadcrumbs = ref<RouteLocationMatched[]>([])

// 获取面包屑数据
const getBreadcrumb = () => {
  let matched = route.matched.filter(item => item.meta && item.meta.title)
  
  // 如果第一个不是首页，则在前面添加首页
  const first = matched[0]
  if (first && first.path !== '/') {
    matched = [{
      path: '/',
      meta: { title: '首页' }
    } as RouteLocationMatched].concat(matched)
  }
  
  breadcrumbs.value = matched
}

// 处理链接点击
const handleLink = (item: RouteLocationMatched) => {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect.toString())
    return
  }
  router.push(path)
}

// 监听路由变化
watch(
  () => route.path,
  () => getBreadcrumb(),
  {
    immediate: true
  }
)
</script>

<style lang="scss" scoped>
.el-breadcrumb {
  line-height: 50px;
  
  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}

// 面包屑动画
.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all 0.5s;
}

.breadcrumb-enter-from,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style> 