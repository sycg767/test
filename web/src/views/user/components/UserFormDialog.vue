<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="type === 'add' ? '新增用户' : '编辑用户'"
    width="500px"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="用户名" prop="username" v-if="type === 'add'">
        <el-input
          v-model="form.username"
          placeholder="请输入用户名"
        />
      </el-form-item>

      <el-form-item label="密码" prop="password" v-if="type === 'add'">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="form.nickname"
          placeholder="请输入昵称"
        />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="form.email"
          placeholder="请输入邮箱"
        />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="form.phone"
          placeholder="请输入手机号"
        />
      </el-form-item>

      <el-form-item label="角色" prop="roles">
        <el-select
          v-model="form.roles"
          multiple
          placeholder="请选择角色"
        >
          <el-option label="管理员" value="ADMIN" />
          <el-option label="普通用户" value="USER" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { createUser, updateUser } from '@/api/user'
import type { User } from '@/api/user'

const props = defineProps({
  modelValue: Boolean,
  type: {
    type: String as () => 'add' | 'edit',
    required: true
  },
  data: {
    type: Object as () => User,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 表单相关
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: '',
  roles: [] as string[]
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  roles: [
    { required: true, message: '请选择角色', trigger: 'change' },
    { type: 'array', min: 1, message: '请至少选择一个角色', trigger: 'change' }
  ]
}

// 初始化表单数据
const initForm = () => {
  if (props.type === 'edit' && props.data) {
    form.nickname = props.data.nickname
    form.email = props.data.email || ''
    form.phone = props.data.phone || ''
    form.roles = props.data.roles
  } else {
    form.username = ''
    form.password = ''
    form.nickname = ''
    form.email = ''
    form.phone = ''
    form.roles = []
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (props.type === 'add') {
          await createUser(form)
          ElMessage.success('新增成功')
        } else {
          await updateUser(props.data!.id, {
            nickname: form.nickname,
            email: form.email,
            phone: form.phone,
            roles: form.roles
          })
          ElMessage.success('更新成功')
        }
        emit('success')
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 初始化
initForm()
</script> 