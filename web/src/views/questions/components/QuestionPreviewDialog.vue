<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    title="题目预览"
    width="600px"
  >
    <div class="question-preview">
      <!-- 题目类型和难度 -->
      <div class="question-header">
        <el-tag :type="getQuestionTypeTag(question.type)">
          {{ getQuestionTypeLabel(question.type) }}
        </el-tag>
        <el-rate
          v-model="question.difficulty"
          disabled
          :max="3"
          :colors="['#67C23A', '#E6A23C', '#F56C6C']"
        />
      </div>

      <!-- 题目内容 -->
      <div class="question-content">
        <div class="content-label">题目内容：</div>
        <div class="content-text" v-html="question.content"></div>
      </div>

      <!-- 选项列表 -->
      <div class="options-list" v-if="question.type !== 'JUDGE'">
        <div class="content-label">选项：</div>
        <div
          v-for="option in question.options"
          :key="option.label"
          class="option-item"
          :class="{ 'is-correct': isCorrectAnswer(option.label) }"
        >
          <span class="option-label">{{ option.label }}.</span>
          <span class="option-content">{{ option.content }}</span>
          <el-icon v-if="isCorrectAnswer(option.label)" class="correct-icon">
            <Select />
          </el-icon>
        </div>
      </div>

      <!-- 答案 -->
      <div class="question-answer">
        <div class="content-label">正确答案：</div>
        <div class="answer-text">
          <template v-if="question.type === 'JUDGE'">
            {{ question.answer[0] === 'T' ? '正确' : '错误' }}
          </template>
          <template v-else>
            {{ question.answer.join('、') }}
          </template>
        </div>
      </div>

      <!-- 解析 -->
      <div class="question-analysis" v-if="question.analysis">
        <div class="content-label">答案解析：</div>
        <div class="analysis-text">{{ question.analysis }}</div>
      </div>

      <!-- 统计信息 -->
      <div class="question-stats">
        <div class="stat-item">
          <span class="stat-label">正确率：</span>
          <span class="stat-value">{{ (question.correctRate * 100).toFixed(1) }}%</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">答题次数：</span>
          <span class="stat-value">{{ question.answerCount || 0 }}</span>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Question } from '@/api/question'

const props = defineProps({
  modelValue: Boolean,
  question: {
    type: Object as () => Question,
    required: true
  }
})

defineEmits(['update:modelValue'])

// 判断是否为正确答案
const isCorrectAnswer = (label: string) => {
  return props.question.answer.includes(label)
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
</script>

<style scoped>
.question-preview {
  padding: 0 20px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.content-label {
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.question-content {
  margin-bottom: 20px;
}

.content-text {
  line-height: 1.6;
  color: #333;
}

.options-list {
  margin-bottom: 20px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  padding: 8px 12px;
  margin-bottom: 8px;
  border-radius: 4px;
  background: #f5f7fa;
}

.option-item.is-correct {
  background: #f0f9eb;
  color: #67c23a;
}

.option-label {
  margin-right: 8px;
  font-weight: 500;
}

.option-content {
  flex: 1;
}

.correct-icon {
  margin-left: 8px;
  color: #67c23a;
}

.question-answer,
.question-analysis {
  margin-bottom: 20px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}

.answer-text,
.analysis-text {
  color: #333;
  line-height: 1.6;
}

.question-stats {
  display: flex;
  gap: 24px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-label {
  color: #666;
  margin-right: 4px;
}

.stat-value {
  color: #333;
  font-weight: 500;
}
</style> 