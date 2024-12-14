<template>
  <div class="question-banks-container">
    <!-- 搜索和操作栏 -->
    <div class="action-bar">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item>
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索题库名称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryParams.categoryId" placeholder="选择分类" clearable>
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
      
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增题库
      </el-button>
    </div>

    <!-- 题库列表 -->
    <el-table
      v-loading="loading"
      :data="bankList"
      border
      style="width: 100%"
    >
      <el-table-column prop="name" label="题库名称" />
      <el-table-column prop="category.name" label="分类" />
      <el-table-column prop="questionCount" label="题目数量" width="100" />
      <el-table-column prop="userCount" label="做题人数" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="primary" link @click="handleManageQuestions(row)">
            管理题目
          </el-button>
          <el-popconfirm
            title="确定要删除该题库吗？"
            @confirm="handleDelete(row)"
          >
            <template #reference>
              <el-button type="danger" link>删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增题库' : '编辑题库'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入题库名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入题库描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { formatDate } from '@/utils/format'
import { getQuestionBanks, createQuestionBank, updateQuestionBank, deleteQuestionBank } from '@/api/questionBank'
import { getCategories } from '@/api/category'
import { useRouter } from 'vue-router'

const router = useRouter()

// 查询参数
const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  categoryId: undefined
})

// 数据列表
const bankList = ref([])
const total = ref(0)
const loading = ref(false)
const categories = ref([])

// 表单相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const form = reactive({
  id: undefined,
  name: '',
  categoryId: undefined,
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入题库名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ]
}

// 获取题库列表
const loadBanks = async () => {
  loading.value = true
  try {
    const res = await getQuestionBanks(queryParams)
    bankList.value = res.content
    total.value = res.total
  } catch (error: any) {
    ElMessage.error(error.message || '获取题库列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res
  } catch (error: any) {
    ElMessage.error(error.message || '获取分类列表失��')
  }
}

// 搜索
const handleSearch = () => {
  queryParams.page = 1
  loadBanks()
}

// 分页操作
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  loadBanks()
}

const handleCurrentChange = (val: number) => {
  queryParams.page = val
  loadBanks()
}

// 新增题库
const handleAdd = () => {
  dialogType.value = 'add'
  form.id = undefined
  form.name = ''
  form.categoryId = undefined
  form.description = ''
  dialogVisible.value = true
}

// 编辑题库
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  form.id = row.id
  form.name = row.name
  form.categoryId = row.category.id
  form.description = row.description
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (dialogType.value === 'add') {
          await createQuestionBank(form)
          ElMessage.success('新增成功')
        } else {
          await updateQuestionBank(form.id!, form)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadBanks()
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 删除题库
const handleDelete = async (row: any) => {
  try {
    await deleteQuestionBank(row.id)
    ElMessage.success('删除成功')
    loadBanks()
  } catch (error: any) {
    ElMessage.error(error.message || '删除失败')
  }
}

// 管理题目
const handleManageQuestions = (row: any) => {
  router.push(`/question-banks/${row.id}/questions`)
}

onMounted(() => {
  loadBanks()
  loadCategories()
})
</script>

<style scoped>
.question-banks-container {
  padding: 20px;
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  flex: 1;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 