<template>
  <div class="bank-questions-container">
    <div class="page-header">
      <div class="header-left">
        <h2>{{ bank?.name }} - 题目管理</h2>
        <el-tag>{{ bank?.category?.name }}</el-tag>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增题目
        </el-button>
        <el-button type="primary" @click="handleBatchImport">
          <el-icon><Upload /></el-icon>批量导入
        </el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams">
        <el-form-item>
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索题目"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-select v-model="queryParams.type" placeholder="题目类型" clearable>
            <el-option label="单选题" value="SINGLE" />
            <el-option label="多选题" value="MULTIPLE" />
            <el-option label="判断题" value="JUDGE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 在搜索栏上方添加统计卡片 -->
    <div class="stat-cards">
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>题目总数</span>
            <el-tag>{{ stats.total || 0 }}</el-tag>
          </div>
        </template>
        <div class="card-content">
          <div class="stat-item">
            <span>单选题</span>
            <span>{{ stats.singleCount || 0 }}</span>
          </div>
          <div class="stat-item">
            <span>多选题</span>
            <span>{{ stats.multipleCount || 0 }}</span>
          </div>
          <div class="stat-item">
            <span>判断题</span>
            <span>{{ stats.judgeCount || 0 }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>平均正确率</span>
            <el-tag type="success">{{ stats.avgCorrectRate || 0 }}%</el-tag>
          </div>
        </template>
        <div class="card-content">
          <el-progress 
            type="dashboard"
            :percentage="Number((stats.avgCorrectRate || 0).toFixed(1))"
            :color="correctRateColor"
          />
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>答题次数</span>
            <el-tag type="warning">{{ stats.totalAnswers || 0 }}</el-tag>
          </div>
        </template>
        <div class="card-content">
          <div class="stat-item">
            <span>今日答题</span>
            <span>{{ stats.todayAnswers || 0 }}</span>
          </div>
          <div class="stat-item">
            <span>本周答题</span>
            <span>{{ stats.weekAnswers || 0 }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 在表格上方添加批量操作工具栏 -->
    <div class="table-toolbar" v-if="selection.length">
      <span class="selected-count">已选择 {{ selection.length }} 项</span>
      <div class="toolbar-actions">
        <el-button type="primary" @click="handleBatchMove">
          <el-icon><FolderAdd /></el-icon>
          移动到其他题库
        </el-button>
        <el-button type="danger" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 题目列表 -->
    <el-table
      v-loading="loading"
      :data="questionList"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="题目" min-width="400">
        <template #default="{ row }">
          <div class="question-content">
            <div class="question-type">
              <el-tag :type="getQuestionTypeTag(row.type)">
                {{ getQuestionTypeLabel(row.type) }}
              </el-tag>
            </div>
            <div class="question-text" v-html="row.content"></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="difficulty" label="难度" width="100">
        <template #default="{ row }">
          <el-rate
            v-model="row.difficulty"
            disabled
            :max="3"
            :colors="['#67C23A', '#E6A23C', '#F56C6C']"
          />
        </template>
      </el-table-column>
      <el-table-column prop="correctRate" label="正确率" width="100">
        <template #default="{ row }">
          {{ (row.correctRate * 100).toFixed(1) }}%
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">
            编��
          </el-button>
          <el-button type="primary" link @click="handlePreview(row)">
            预览
          </el-button>
          <el-popconfirm
            title="确定要删除该题目吗？"
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
    <question-form-dialog
      v-if="dialogVisible"
      v-model="dialogVisible"
      :type="dialogType"
      :question="currentQuestion"
      :bank-id="bankId"
      @success="handleFormSuccess"
    />

    <!-- 预览对话框 -->
    <question-preview-dialog
      v-if="previewVisible"
      v-model="previewVisible"
      :question="currentQuestion"
    />

    <!-- ��量导入对话框 -->
    <import-dialog
      v-if="importVisible"
      v-model="importVisible"
      :bank-id="bankId"
      @success="handleImportSuccess"
    />

    <!-- 添加移动题目对话框 -->
    <el-dialog
      v-model="moveDialogVisible"
      title="移动题目"
      width="400px"
    >
      <el-form :model="moveForm" label-width="80px">
        <el-form-item label="目标题库">
          <el-select v-model="moveForm.targetBankId" placeholder="请选择题库">
            <el-option
              v-for="item in bankOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="moveDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="moveLoading" @click="handleMoveConfirm">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getQuestionBank } from '@/api/questionBank'
import { getQuestions, deleteQuestion } from '@/api/question'
import QuestionFormDialog from './components/QuestionFormDialog.vue'
import QuestionPreviewDialog from './components/QuestionPreviewDialog.vue'
import ImportDialog from './components/ImportDialog.vue'

const route = useRoute()
const bankId = Number(route.params.bankId)

// 数据
const bank = ref()
const questionList = ref([])
const total = ref(0)
const loading = ref(false)

// 查询参数
const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  type: undefined,
  bankId
})

// 对话框控件
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const currentQuestion = ref()
const previewVisible = ref(false)
const importVisible = ref(false)

// 获取题库信息
const loadBank = async () => {
  try {
    bank.value = await getQuestionBank(bankId)
  } catch (error: any) {
    ElMessage.error(error.message || '获取题库信息失败')
  }
}

// 获取题目列表
const loadQuestions = async () => {
  loading.value = true
  try {
    const res = await getQuestions(queryParams)
    questionList.value = res.content
    total.value = res.total
  } catch (error: any) {
    ElMessage.error(error.message || '获取题目列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  queryParams.page = 1
  loadQuestions()
}

// 分页操作
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  loadQuestions()
}

const handleCurrentChange = (val: number) => {
  queryParams.page = val
  loadQuestions()
}

// 新增题目
const handleAdd = () => {
  dialogType.value = 'add'
  currentQuestion.value = null
  dialogVisible.value = true
}

// 编辑题目
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  currentQuestion.value = row
  dialogVisible.value = true
}

// 预览题目
const handlePreview = (row: any) => {
  currentQuestion.value = row
  previewVisible.value = true
}

// 删除题目
const handleDelete = async (row: any) => {
  try {
    await deleteQuestion(row.id)
    ElMessage.success('删除成功')
    loadQuestions()
  } catch (error: any) {
    ElMessage.error(error.message || '删除失败')
  }
}

// 批量导入
const handleBatchImport = () => {
  importVisible.value = true
}

// 表单提交成功
const handleFormSuccess = () => {
  dialogVisible.value = false
  loadQuestions()
}

// 导入成功
const handleImportSuccess = () => {
  importVisible.value = false
  loadQuestions()
}

// 工具函数
const getQuestionTypeLabel = (type: string) => {
  const types = {
    SINGLE: '单选题',
    MULTIPLE: '多选题',
    JUDGE: '判断题'
  }
  return types[type] || type
}

const getQuestionTypeTag = (type: string) => {
  const types = {
    SINGLE: '',
    MULTIPLE: 'success',
    JUDGE: 'warning'
  }
  return types[type] || ''
}

onMounted(() => {
  loadBank()
  loadQuestions()
})
</script>

<style scoped>
.bank-questions-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.question-content {
  display: flex;
  gap: 12px;
}

.question-text {
  flex: 1;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 