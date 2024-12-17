<template>
  <basic-list
    title="题库列表"
    :loading="loading"
    :table-data="tableData"
    :total="total"
    @search="handleSearch"
    @reset="handleReset"
    @page-change="handlePageChange"
    @size-change="handleSizeChange"
  >
    <!-- 搜索表单 -->
    <template #search-form="{ form }">
      <el-form-item label="题库名称">
        <el-input v-model="form.keyword" placeholder="请输入题库名称" clearable />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
          <el-option
            v-for="item in categories"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
    </template>

    <!-- 操作按钮 -->
    <template #operations>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增题库
      </el-button>
      <el-button type="success" @click="handleImport">
        <el-icon><Upload /></el-icon>导入题库
      </el-button>
    </template>

    <!-- 表格列 -->
    <el-table-column prop="name" label="题库名称" min-width="150" show-overflow-tooltip />
    <el-table-column prop="category.name" label="所属分类" width="120" />
    <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
    <el-table-column prop="questionCount" label="题目数量" width="100" align="center" />
    <el-table-column prop="userCount" label="使用人数" width="100" align="center" />
    <el-table-column prop="createdAt" label="创建时间" width="180">
      <template #default="{ row }">
        {{ formatDate(row.createdAt) }}
      </template>
    </el-table-column>
    <el-table-column prop="status" label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'info'">
          {{ row.status === 1 ? '启用' : '禁用' }}
        </el-tag>
      </template>
    </el-table-column>

    <!-- 操作列 -->
    <template #table-operations="{ row }">
      <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
      <el-button type="success" link @click="handlePreview(row)">预览</el-button>
      <el-button
        type="danger"
        link
        @click="handleDelete(row)"
      >删除</el-button>
    </template>
  </basic-list>

  <!-- 导入对话框 -->
  <el-dialog
    v-model="importVisible"
    title="导入题库"
    width="500px"
  >
    <el-upload
      class="upload-demo"
      drag
      action="/api/banks/import"
      :headers="uploadHeaders"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      accept=".xlsx,.xls"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          只能上传 xlsx/xls 文件，且文件大小不超过 10MB
        </div>
      </template>
    </el-upload>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Upload, UploadFilled } from '@element-plus/icons-vue'
import BasicList from '@/components/BasicList.vue'
import { getQuestionBankList, deleteQuestionBank } from '@/api/questionBank'
import type { QuestionBank } from '@/api/questionBank'

const router = useRouter()
const loading = ref(false)
const tableData = ref<QuestionBank[]>([])
const total = ref(0)
const importVisible = ref(false)
const categories = ref([]) // TODO: 从后端获取分类列表

const uploadHeaders = {
  Authorization: `Bearer ${localStorage.getItem('token')}`
}

// 查询列表
const loadData = async (params = {}) => {
  try {
    loading.value = true
    const { data } = await getQuestionBankList(params)
    tableData.value = data.content
    total.value = data.totalElements
  } catch (error: any) {
    ElMessage.error(error.message || '获取列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
  // TODO: 加载分类列表
})

// 搜索相关
const handleSearch = (form: any) => {
  loadData(form)
}

const handleReset = () => {
  loadData()
}

const handlePageChange = (page: number) => {
  loadData({ page: page - 1 }) // Spring Data JPA 的页码从0开始
}

const handleSizeChange = (size: number) => {
  loadData({ size })
}

// 操作相关
const handleAdd = () => {
  router.push('/question-bank/add')
}

const handleEdit = (row: QuestionBank) => {
  router.push(`/question-bank/edit/${row.id}`)
}

const handlePreview = (row: QuestionBank) => {
  router.push(`/question-bank/preview/${row.id}`)
}

const handleDelete = async (row: QuestionBank) => {
  try {
    await ElMessageBox.confirm('确定要删除该题库吗？', '提示', {
      type: 'warning'
    })
    await deleteQuestionBank(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleImport = () => {
  importVisible.value = true
}

const handleUploadSuccess = () => {
  ElMessage.success('导入成功')
  importVisible.value = false
  loadData()
}

const handleUploadError = () => {
  ElMessage.error('导入失败')
}

const formatDate = (date: string) => {
  if (!date) return ''
  return new Date(date).toLocaleString()
}
</script>

<style lang="scss" scoped>
.upload-demo {
  text-align: center;
  
  .el-upload {
    width: 100%;
  }
  
  .el-upload-dragger {
    width: 100%;
  }
}
</style> 