<template>
  <el-drawer
    v-model="modelValue"
    title="数据分析"
    size="800px"
    :destroy-on-close="true"
    @open="loadData"
  >
    <div class="analysis-panel">
      <!-- 时间范围选择 -->
      <div class="filter-bar">
        <el-radio-group v-model="timeRange" @change="handleTimeRangeChange">
          <el-radio-button label="week">最近一周</el-radio-button>
          <el-radio-button label="month">最近一月</el-radio-button>
          <el-radio-button label="custom">自定义</el-radio-button>
        </el-radio-group>
        
        <el-date-picker
          v-if="timeRange === 'custom'"
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :disabled-date="disabledDate"
          @change="handleDateRangeChange"
        />
      </div>

      <!-- 答题趋势图 -->
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>答题趋势</span>
            <el-radio-group v-model="trendType" size="small">
              <el-radio-button label="count">答题数</el-radio-button>
              <el-radio-button label="rate">正确率</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div class="chart-container">
          <v-chart :option="trendChartOption" autoresize />
        </div>
      </el-card>

      <!-- 题型分布和难度分布 -->
      <div class="chart-row">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>题型分布</span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="typeChartOption" autoresize />
          </div>
        </el-card>

        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>难度分布</span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="difficultyChartOption" autoresize />
          </div>
        </el-card>
      </div>

      <!-- 错题排行 -->
      <el-card class="ranking-card">
        <template #header>
          <div class="card-header">
            <span>错题排行</span>
            <el-button type="primary" link @click="exportErrorQuestions">
              导出错题
            </el-button>
          </div>
        </template>
        <el-table :data="errorRanking" stripe>
          <el-table-column label="排名" width="80">
            <template #default="{ $index }">
              {{ $index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="题目" min-width="300">
            <template #default="{ row }">
              <div class="question-brief">
                <el-tag size="small" :type="getQuestionTypeTag(row.type)">
                  {{ getQuestionTypeLabel(row.type) }}
                </el-tag>
                <span class="question-content">{{ row.content }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="errorCount" label="错误次数" width="100" />
          <el-table-column prop="errorRate" label="错误率" width="100">
            <template #default="{ row }">
              {{ (row.errorRate * 100).toFixed(1) }}%
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button type="primary" link @click="handlePreview(row)">
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 题目预览对话框 -->
    <question-preview-dialog
      v-if="previewVisible"
      v-model="previewVisible"
      :question="currentQuestion"
    />
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import { ElMessage } from 'element-plus'
import QuestionPreviewDialog from './QuestionPreviewDialog.vue'
import { getQuestionAnalysis, exportErrorQuestionList } from '@/api/question'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const props = defineProps({
  modelValue: Boolean,
  bankId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue'])

// 数据
const timeRange = ref('week')
const dateRange = ref<[Date, Date]>()
const trendType = ref('count')
const analysisData = ref<any>()
const errorRanking = ref([])
const previewVisible = ref(false)
const currentQuestion = ref()

// 加载数据
const loadData = async () => {
  try {
    const res = await getQuestionAnalysis({
      bankId: props.bankId,
      startDate: dateRange.value?.[0]?.toISOString(),
      endDate: dateRange.value?.[1]?.toISOString()
    })
    analysisData.value = res
    errorRanking.value = res.errorRanking
  } catch (error: any) {
    ElMessage.error(error.message || '获取分析数据失败')
  }
}

// 时间范围处理
const handleTimeRangeChange = (value: string) => {
  if (value !== 'custom') {
    const end = new Date()
    const start = new Date()
    if (value === 'week') {
      start.setDate(start.getDate() - 7)
    } else {
      start.setMonth(start.getMonth() - 1)
    }
    dateRange.value = [start, end]
  }
}

const disabledDate = (date: Date) => {
  return date > new Date()
}

// 图表配置
const trendChartOption = computed(() => {
  if (!analysisData.value) return {}
  
  const { trends } = analysisData.value
  return {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: trends.dates
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: trendType.value === 'rate' ? '{value}%' : '{value}'
      }
    },
    series: [
      {
        type: 'line',
        data: trends[trendType.value === 'rate' ? 'correctRates' : 'counts'],
        smooth: true,
        areaStyle: {}
      }
    ]
  }
})

const typeChartOption = computed(() => {
  if (!analysisData.value) return {}
  
  const { typeDistribution } = analysisData.value
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: [
          { name: '单选题', value: typeDistribution.single },
          { name: '多选题', value: typeDistribution.multiple },
          { name: '判断题', value: typeDistribution.judge }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})

const difficultyChartOption = computed(() => {
  if (!analysisData.value) return {}
  
  const { difficultyDistribution } = analysisData.value
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    series: [
      {
        type: 'pie',
        radius: '70%',
        data: [
          { name: '简单', value: difficultyDistribution[1] },
          { name: '中等', value: difficultyDistribution[2] },
          { name: '困难', value: difficultyDistribution[3] }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})

// 导出错题
const exportErrorQuestions = async () => {
  try {
    await exportErrorQuestionList(props.bankId)
    ElMessage.success('导出成功')
  } catch (error: any) {
    ElMessage.error(error.message || '导出失败')
  }
}

// 预览题目
const handlePreview = (question: any) => {
  currentQuestion.value = question
  previewVisible.value = true
}

// 监听时间范围变化
watch([dateRange], () => {
  if (dateRange.value) {
    loadData()
  }
})
</script>

<style scoped>
.analysis-panel {
  padding: 20px;
}

.filter-bar {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.question-brief {
  display: flex;
  align-items: center;
  gap: 8px;
}

.question-content {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style> 