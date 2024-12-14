<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    title="重置密码"
    width="400px"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <div class="user-info">
        <span class="label">用户：</span>
        <span class="value">{{ user?.nickname }} ({{ user?.username }})</span>
      </div>

      <el-form-item label="新密码" prop="password">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入新密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="请再次输入新密码"
          show-password
        />
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
import { resetPassword } from '@/api/user'
import type { User } from '@/api/user'

const props = defineProps({
  modelValue: Boolean,
  user: {
    type: Object as () => User,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 表单相关
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  password: '',
  confirmPassword: ''
})

// 密码验证函数
const validateConfirmPassword = (_rule: any, value: string, callback: Function) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, trigger: 'blur', validator: validateConfirmPassword }
  ]
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await resetPassword(props.user.id, form.password)
        ElMessage.success('密码重置成功')
        emit('success')
      } catch (error: any) {
        ElMessage.error(error.message || '重置失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.user-info {
  margin-bottom: 20px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}

.label {
  color: #606266;
  margin-right: 8px;
}

.value {
  font-weight: 500;
}
</style> 