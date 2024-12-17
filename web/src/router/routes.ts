import type { RouteRecordRaw } from 'vue-router'

// 布局组件采用动态导入
const Layout = () => import('@/layout/index.vue')

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { hidden: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { title: '首页', icon: 'House' },
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '控制台', icon: 'Monitor' }
      }
    ]
  },
  {
    path: '/question-bank',
    component: Layout,
    meta: { title: '题库管理', icon: 'Document' },
    children: [
      {
        path: 'list',
        component: () => import('@/views/question-bank/list.vue'),
        meta: { title: '题库列表', icon: 'List' }
      },
      {
        path: 'add',
        component: () => import('@/views/question-bank/edit.vue'),
        meta: { title: '添加题库', icon: 'Plus' }
      }
    ]
  },
  {
    path: '/knowledge',
    component: Layout,
    meta: { title: '知识点管理', icon: 'Collection' },
    children: [
      {
        path: 'list',
        component: () => import('@/views/knowledge/list.vue'),
        meta: { title: '知识点列表', icon: 'List' }
      },
      {
        path: 'add',
        component: () => import('@/views/knowledge/edit.vue'),
        meta: { title: '添加知识点', icon: 'Plus' }
      }
    ]
  },
  {
    path: '/category',
    component: Layout,
    meta: { title: '分类管理', icon: 'Files' },
    children: [
      {
        path: 'list',
        component: () => import('@/views/category/list.vue'),
        meta: { title: '分类列表', icon: 'List' }
      },
      {
        path: 'add',
        component: () => import('@/views/category/edit.vue'),
        meta: { title: '添加分类', icon: 'Plus' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/system/user.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'role',
        component: () => import('@/views/system/role.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      }
    ]
  }
] 