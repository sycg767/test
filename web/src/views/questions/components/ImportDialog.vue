<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    title="批量导入题目"
    width="500px"
  >
    <div class="import-dialog">
      <!-- 上传说明 -->
      <div class="import-tips">
        <h4>导入说明：</h4>
        <ol>
          <li>请下载模板文件，按照模板格式填写题目</li>
          <li>支持的题目类型：单选题(SINGLE)、多选题(MULTIPLE)、判断题(JUDGE)</li>
          <li>选项答案用英文逗号分隔，如：A,B,C</li>
          <li>判断题答案填写：T(正确)或F(错误)</li>
        </ol>
      </div>

      <!-- 下载模板 -->
      <div class="template-download">
        <el-button type="primary" link @click="downloadTemplate">
          <el-icon><Download /></el-icon>
          下载导入模板
        </el-button>
      </div>

      <!-- 文件上传 -->
      <el-upload
        class="upload-area"
        :action="uploadAction"
        :headers="uploadHeaders"
        :data="uploadData"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        :show-file-list="false"
        accept=".xlsx,.xls"
      >
        <div class="upload-trigger" :class="{ 'is-loading': loading }">
          <template v-if="!loading">
            <el-icon class="upload-icon"><Upload /></el-icon>
            <div class="upload-text">
              <span>点击或拖拽文件到此处上传</span>
              <span class="upload-hint">支持 .xlsx, .xls 格式</span>
            </div>
          </template>
          <template v-else>
            <el-progress 
              type="circle" 
              :percentage="uploadProgress"
              :status="uploadStatus"
            />
          </template>
        </div>
      </el-upload>

      <!-- 导入结果 -->
      <div v-if="importResult" class="import-result">
        <div class="result-header">
          <el-icon :class="resultIcon.class" class="result-icon">
            <component :is="resultIcon.name" />
          </el-icon>
          <span class="result-text">{{ resultText }}</span>
        </div>
        <div v-if="importResult.errors?.length" class="error-list">
          <div v-for="(error, index) in importResult.errors" :key="index" class="error-item">
            <el-icon><Warning /></el-icon>
            <span>{{ error }}</span>
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="handleClose">关闭</el-button>
      <el-button 
        type="primary" 
        @click="handleConfirm"
        v-if="importResult?.success"
      >
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'

const props = defineProps({
  modelValue: Boolean,
  bankId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 上传相关
const loading = ref(false)
const uploadProgress = ref(0)
const uploadStatus = ref<'success' | 'exception'>('success')
const importResult = ref<{
  success: boolean
  total?: number
  imported?: number
  errors?: string[]
}>()

// 上传配置
const uploadAction = `${import.meta.env.VITE_API_URL}/questions/import`
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`
}))
const uploadData = computed(() => ({
  bankId: props.bankId
}))

// 下载模板
const downloadTemplate = () => {
  const link = document.createElement('a')
  link.href = `${import.meta.env.VITE_API_URL}/questions/template`
  link.download = '题目导入模板.xlsx'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 上传前校验
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  const isExcel = [
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  ].includes(file.type)

  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件!')
    return false
  }

  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('文件大小不能超过 2MB!')
    return false
  }

  loading.value = true
  uploadProgress.value = 0
  uploadStatus.value = 'success'
  importResult.value = undefined

  // 模拟上传进度
  const timer = setInterval(() => {
    if (uploadProgress.value < 90) {
      uploadProgress.value += 10
    }
  }, 300)

  return true
}

// 上传成功
const handleSuccess = (response: any) => {
  loading.value = false
  uploadProgress.value = 100
  
  importResult.value = {
    success: true,
    total: response.total,
    imported: response.imported,
    errors: response.errors
  }
}

// 上传失败
const handleError = () => {
  loading.value = false
  uploadProgress.value = 100
  uploadStatus.value = 'exception'
  
  importResult.value = {
    success: false,
    errors: ['文件上传失败，请重试']
  }
}

// 计算结果图标
const resultIcon = computed(() => {
  if (!importResult.value) return {}
  
  return importResult.value.success 
    ? { name: 'CircleCheckFilled', class: 'success' }
    : { name: 'CircleCloseFilled', class: 'error' }
})

// 计算结果文本
const resultText = computed(() => {
  if (!importResult.value) return ''
  
  if (importResult.value.success) {
    return `成功导入 ${importResult.value.imported} 道题目`
  }
  return '导入失败'
})

// 关闭对话框
const handleClose = () => {
  emit('update:modelValue', false)
}

// 确认导入
const handleConfirm = () => {
  emit('success')
  handleClose()
}
</script>

<style scoped>
.import-dialog {
  padding: 0 20px;
}

.import-tips {
  margin-bottom: 20px;
}

.import-tips h4 {
  margin: 0 0 8px;
  color: #333;
}

.import-tips ol {
  margin: 0;
  padding-left: 20px;
  color: #666;
  font-size: 14px;
}

.import-tips li {
  margin-bottom: 4px;
}

.template-download {
  margin-bottom: 20px;
}

.upload-area {
  width: 100%;
}

.upload-trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 180px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-trigger:hover {
  border-color: #409eff;
}

.upload-trigger.is-loading {
  cursor: default;
}

.upload-icon {
  font-size: 48px;
  color: #8c939d;
  margin-bottom: 8px;
}

.upload-text {
  text-align: center;
}

.upload-hint {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: #999;
}

.import-result {
  margin-top: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.result-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.result-icon {
  font-size: 20px;
  margin-right: 8px;
}

.result-icon.success {
  color: #67c23a;
}

.result-icon.error {
  color: #f56c6c;
}

.result-text {
  font-size: 16px;
  font-weight: 500;
}

.error-list {
  margin-top: 8px;
}

.error-item {
  display: flex;
  align-items: center;
  color: #f56c6c;
  font-size: 14px;
  margin-bottom: 4px;
}

.error-item .el-icon {
  margin-right: 4px;
}
</style> 