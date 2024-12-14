<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    title="关联题目"
    width="800px"
  >
    <div class="associate-dialog">
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
              <el-tag size="small" :type="getQuestionTypeTag(row.type)">
                {{ getQuestionTypeLabel(row.type) }}
              </el-tag>
              <span class="question-text">{{ row.content }}</span>
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
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
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
    </div>

    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">取消</el-button>
      <el-button 
        type="primary" 
        :loading="submitLoading"
        :disabled="!selection.length"
        @click="handleSubmit"
      >
        确定关联 ({{ selection.length }})
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getQuestions } from '@/api/question'
import { associateQuestions } from '@/api/knowledge'
import type { KnowledgePoint } from '@/api/knowledge'

const props = defineProps({
  modelValue: Boolean,
  knowledgePoint: {
    type: Object as () => KnowledgePoint,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 数据
const loading = ref(false)
const submitLoading = ref(false)
const questionList = ref([])
const total = ref(0)
const selection = ref([])

// 查询参数
const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  type: undefined as string | undefined
})

// 加载题目列表
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

// 选择变化
const handleSelectionChange = (val: any[]) => {
  selection.value = val
}

// 分页处理
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  queryParams.page = 1
  loadQuestions()
}

const handleCurrentChange = (val: number) => {
  queryParams.page = val
  loadQuestions()
}

// 提交关联
const handleSubmit = async () => {
  submitLoading.value = true
  try {
    await associateQuestions(
      props.knowledgePoint.id,
      selection.value.map(item => item.id)
    )
    ElMessage.success('关联成功')
    emit('success')
  } catch (error: any) {
    ElMessage.error(error.message || '关联失败')
  } finally {
    submitLoading.value = false
  }
}

// 获取题目类型标签
const getQuestionTypeLabel = (type: string) => {
  const types = {
    SINGLE: '单选题',
    MULTIPLE: '多选题',
    JUDGE: '判断题'
  }
  return types[type] || type
}

// 获取题目类型标签样式
const getQuestionTypeTag = (type: string) => {
  const types = {
    SINGLE: '',
    MULTIPLE: 'success',
    JUDGE: 'warning'
  }
  return types[type] || ''
}

// 初始加载
loadQuestions()
</script>

<style scoped>
.associate-dialog {
  padding: 0 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.question-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.question-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 