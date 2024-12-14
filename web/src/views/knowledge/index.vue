<template>
  <div class="knowledge-container">
    <div class="page-header">
      <h2>知识点管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增知识点
      </el-button>
    </div>

    <div class="content-layout">
      <!-- 知识点树 -->
      <div class="tree-panel">
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索知识点"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <el-tree
          ref="treeRef"
          :data="treeData"
          :props="{ label: 'name' }"
          :filter-node-method="filterNode"
          node-key="id"
          highlight-current
          @current-change="handleNodeSelect"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <span>{{ node.label }}</span>
              <span class="question-count">{{ data.questionCount }}题</span>
            </div>
          </template>
        </el-tree>
      </div>

      <!-- 知识点详情 -->
      <div class="detail-panel" v-if="currentNode">
        <el-card>
          <template #header>
            <div class="card-header">
              <h3>{{ currentNode.name }}</h3>
              <div class="header-actions">
                <el-button type="primary" link @click="handleEdit(currentNode)">
                  编辑
                </el-button>
                <el-popconfirm
                  title="确定要删除该知识点吗？"
                  @confirm="handleDelete(currentNode)"
                >
                  <template #reference>
                    <el-button type="danger" link>删除</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>
          </template>

          <div class="node-info">
            <div class="info-item">
              <span class="label">题目数量：</span>
              <span class="value">{{ currentNode.questionCount }}</span>
            </div>
            <div class="info-item">
              <span class="label">层级：</span>
              <span class="value">{{ currentNode.level }}</span>
            </div>
            <div class="info-item" v-if="currentNode.description">
              <span class="label">描述：</span>
              <span class="value">{{ currentNode.description }}</span>
            </div>
          </div>

          <!-- 关联题目列表 -->
          <div class="questions-list">
            <div class="list-header">
              <h4>关联题目</h4>
              <el-button type="primary" link @click="handleAssociate">
                关联题目
              </el-button>
            </div>

            <el-table
              v-loading="loading"
              :data="questionList"
              border
              style="width: 100%"
            >
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
              <el-table-column label="操作" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button 
                    type="danger" 
                    link 
                    @click="handleDissociate(row)"
                  >
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

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
        </el-card>
      </div>
    </div>

    <!-- 知识点表单对话框 -->
    <knowledge-form-dialog
      v-if="dialogVisible"
      v-model="dialogVisible"
      :type="dialogType"
      :data="currentNode"
      @success="handleFormSuccess"
    />

    <!-- 关联题目对话框 -->
    <associate-dialog
      v-if="associateVisible"
      v-model="associateVisible"
      :knowledge-point="currentNode"
      @success="handleAssociateSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { TreeInstance } from 'element-plus'
import {
  getKnowledgePoints,
  deleteKnowledgePoint,
  getKnowledgePointQuestions,
  dissociateQuestions,
  type KnowledgePoint
} from '@/api/knowledge'
import KnowledgeFormDialog from './components/KnowledgeFormDialog.vue'
import AssociateDialog from './components/AssociateDialog.vue'

// 树相关
const treeRef = ref<TreeInstance>()
const treeData = ref<KnowledgePoint[]>([])
const searchKeyword = ref('')
const currentNode = ref<KnowledgePoint>()

// 题目列表相关
const loading = ref(false)
const questionList = ref([])
const total = ref(0)
const queryParams = ref({
  page: 1,
  pageSize: 10
})

// 对话框控制
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const associateVisible = ref(false)

// 加载知识点树
const loadTree = async () => {
  try {
    const data = await getKnowledgePoints()
    treeData.value = data
  } catch (error: any) {
    ElMessage.error(error.message || '获取知识点列表失败')
  }
}

// 加载关联题目
const loadQuestions = async () => {
  if (!currentNode.value) return
  
  loading.value = true
  try {
    const res = await getKnowledgePointQuestions(currentNode.value.id, queryParams.value)
    questionList.value = res.content
    total.value = res.total
  } catch (error: any) {
    ElMessage.error(error.message || '获取题目列表失败')
  } finally {
    loading.value = false
  }
}

// 树节点过滤
const filterNode = (value: string, data: KnowledgePoint) => {
  if (!value) return true
  return data.name.toLowerCase().includes(value.toLowerCase())
}

// 搜索处理
const handleSearch = (value: string) => {
  treeRef.value?.filter(value)
}

// 节点选择处理
const handleNodeSelect = (data: KnowledgePoint) => {
  currentNode.value = data
  queryParams.value.page = 1
  loadQuestions()
}

// 新增知识点
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
}

// 编辑知识点
const handleEdit = (node: KnowledgePoint) => {
  dialogType.value = 'edit'
  currentNode.value = node
  dialogVisible.value = true
}

// 删除知识点
const handleDelete = async (node: KnowledgePoint) => {
  try {
    await deleteKnowledgePoint(node.id)
    ElMessage.success('删除成功')
    loadTree()
    if (currentNode.value?.id === node.id) {
      currentNode.value = undefined
    }
  } catch (error: any) {
    ElMessage.error(error.message || '删除失败')
  }
}

// 关联题目
const handleAssociate = () => {
  associateVisible.value = true
}

// 移除关联题目
const handleDissociate = (question: any) => {
  ElMessageBox.confirm(
    '确定要移除该题目的关联吗？',
    '提示',
    { type: 'warning' }
  ).then(async () => {
    try {
      await dissociateQuestions(currentNode.value!.id, [question.id])
      ElMessage.success('移除成功')
      loadQuestions()
      loadTree() // 更新题目数量
    } catch (error: any) {
      ElMessage.error(error.message || '移除失败')
    }
  })
}

// 表单提交成功
const handleFormSuccess = () => {
  dialogVisible.value = false
  loadTree()
}

// 关联成功
const handleAssociateSuccess = () => {
  associateVisible.value = false
  loadQuestions()
  loadTree() // 更新题目数量
}

// 分页处理
const handleSizeChange = (val: number) => {
  queryParams.value.pageSize = val
  queryParams.value.page = 1
  loadQuestions()
}

const handleCurrentChange = (val: number) => {
  queryParams.value.page = val
  loadQuestions()
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

onMounted(() => {
  loadTree()
})
</script>

<style scoped>
.knowledge-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.content-layout {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 20px;
  min-height: calc(100vh - 140px);
}

.tree-panel {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.question-count {
  color: #909399;
  font-size: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.node-info {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 12px;
}

.info-item .label {
  color: #606266;
  margin-right: 8px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.list-header h4 {
  margin: 0;
  font-size: 16px;
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