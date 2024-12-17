<template>
  <div class="basic-list">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" @keyup.enter="handleSearch">
        <slot name="search-form" :form="searchForm"></slot>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card">
      <template #header>
        <div class="card-header">
          <div class="title">{{ title }}</div>
          <div class="operations">
            <slot name="operations"></slot>
          </div>
        </div>
      </template>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <slot></slot>
        <el-table-column
          v-if="showOperations"
          label="操作"
          width="200"
          fixed="right"
        >
          <template #default="scope">
            <slot name="table-operations" :row="scope.row"></slot>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = withDefaults(defineProps<{
  title?: string
  loading?: boolean
  tableData?: any[]
  total?: number
  showOperations?: boolean
}>(), {
  title: '',
  loading: false,
  tableData: () => [],
  total: 0,
  showOperations: true
})

const emit = defineEmits<{
  (e: 'search', form: any): void
  (e: 'reset'): void
  (e: 'page-change', page: number): void
  (e: 'size-change', size: number): void
}>()

const searchForm = ref({})
const currentPage = ref(1)
const pageSize = ref(10)

const handleSearch = () => {
  emit('search', searchForm.value)
}

const handleReset = () => {
  searchForm.value = {}
  emit('reset')
}

const handleSizeChange = (val: number) => {
  emit('size-change', val)
}

const handleCurrentChange = (val: number) => {
  emit('page-change', val)
}
</script>

<style lang="scss" scoped>
.basic-list {
  .search-card {
    margin-bottom: 20px;
  }

  .operation-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .title {
        font-size: 16px;
        font-weight: bold;
      }

      .operations {
        display: flex;
        gap: 10px;
      }
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 