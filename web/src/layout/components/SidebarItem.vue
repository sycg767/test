<template>
  <template v-if="!item.meta?.hidden">
    <!-- 有子菜单 -->
    <el-sub-menu
      v-if="item.children && item.children.length > 0"
      :index="resolvePath(item.path)"
    >
      <template #title>
        <el-icon v-if="item.meta?.icon">
          <component :is="item.meta.icon" />
        </el-icon>
        <span>{{ item.meta?.title }}</span>
      </template>
      
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(item.path)"
      />
    </el-sub-menu>

    <!-- 无子菜单 -->
    <el-menu-item
      v-else
      :index="resolvePath(item.path)"
    >
      <el-icon v-if="item.meta?.icon">
        <component :is="item.meta.icon" />
      </el-icon>
      <template #title>{{ item.meta?.title }}</template>
    </el-menu-item>
  </template>
</template>

<script setup lang="ts">
import { resolve } from 'path-browserify'
import type { RouteRecordRaw } from 'vue-router'

const props = defineProps({
  item: {
    type: Object as () => RouteRecordRaw,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  }
})

// 解析路由路径
const resolvePath = (routePath: string) => {
  return resolve(props.basePath, routePath)
}
</script> 