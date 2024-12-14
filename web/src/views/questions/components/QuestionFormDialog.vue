<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="type === 'add' ? '新增题目' : '编辑题目'"
    width="800px"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="题目类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio-button value="SINGLE">单选题</el-radio-button>
          <el-radio-button value="MULTIPLE">多选题</el-radio-button>
          <el-radio-button value="JUDGE">判断题</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="题目内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="4"
          placeholder="请输入题目内容"
        />
      </el-form-item>

      <!-- 选项 -->
      <template v-if="form.type !== 'JUDGE'">
        <el-form-item
          v-for="(option, index) in form.options"
          :key="index"
          :label="'选项' + option.label"
          :prop="'options.' + index + '.content'"
          :rules="{ required: true, message: '请输入选项内容', trigger: 'blur' }"
        >
          <div class="option-item">
            <el-input v-model="option.content" placeholder="请输入选项内容" />
            <el-button type="danger" link @click="removeOption(index)">
              删除
            </el-button>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" link @click="addOption">
            添加选项
          </el-button>
        </el-form-item>
      </template>

      <!-- 答案 -->
      <el-form-item label="正确答案" prop="answer">
        <template v-if="form.type === 'JUDGE'">
          <el-radio-group v-model="form.answer[0]">
            <el-radio label="T">正确</el-radio>
            <el-radio label="F">错误</el-radio>
          </el-radio-group>
        </template>
        <template v-else-if="form.type === 'SINGLE'">
          <el-radio-group v-model="form.answer[0]">
            <el-radio
              v-for="option in form.options"
              :key="option.label"
              :label="option.label"
            >
              {{ option.label }}
            </el-radio>
          </el-radio-group>
        </template>
        <template v-else>
          <el-checkbox-group v-model="form.answer">
            <el-checkbox
              v-for="option in form.options"
              :key="option.label"
              :label="option.label"
            >
              {{ option.label }}
            </el-checkbox>
          </el-checkbox-group>
        </template>
      </el-form-item>

      <el-form-item label="答案解析" prop="analysis">
        <el-input
          v-model="form.analysis"
          type="textarea"
          :rows="3"
          placeholder="请输入答案解析"
        />
      </el-form-item>

      <el-form-item label="难度" prop="difficulty">
        <el-rate
          v-model="form.difficulty"
          :max="3"
          :colors="['#67C23A', '#E6A23C', '#F56C6C']"
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
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { createQuestion, updateQuestion } from '@/api/question'

const props = defineProps({
  modelValue: Boolean,
  type: {
    type: String as () => 'add' | 'edit',
    required: true
  },
  question: {
    type: Object,
    default: null
  },
  bankId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref<FormInstance>()
const loading = ref(false)

// 表单数据
const form = reactive({
  type: 'SINGLE',
  content: '',
  options: [
    { label: 'A', content: '' },
    { label: 'B', content: '' },
    { label: 'C', content: '' },
    { label: 'D', content: '' }
  ],
  answer: ['A'],
  analysis: '',
  difficulty: 1
})

// 表单校验规则
const rules = {
  type: [
    { required: true, message: '请选择题目类型', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入题目内容', trigger: 'blur' }
  ],
  answer: [
    { required: true, message: '请选择正确答案', trigger: 'change' }
  ],
  difficulty: [
    { required: true, message: '请选择难度', trigger: 'change' }
  ]
}

// 监听题目类型变化
watch(() => form.type, (newType) => {
  if (newType === 'JUDGE') {
    form.options = []
    form.answer = ['T']
  } else if (form.options.length === 0) {
    form.options = [
      { label: 'A', content: '' },
      { label: 'B', content: '' },
      { label: 'C', content: '' },
      { label: 'D', content: '' }
    ]
    form.answer = newType === 'SINGLE' ? ['A'] : []
  }
})

// 监听编辑数据
watch(() => props.question, (question) => {
  if (question) {
    Object.assign(form, {
      type: question.type,
      content: question.content,
      options: [...question.options],
      answer: [...question.answer],
      analysis: question.analysis,
      difficulty: question.difficulty
    })
  } else {
    Object.assign(form, {
      type: 'SINGLE',
      content: '',
      options: [
        { label: 'A', content: '' },
        { label: 'B', content: '' },
        { label: 'C', content: '' },
        { label: 'D', content: '' }
      ],
      answer: ['A'],
      analysis: '',
      difficulty: 1
    })
  }
})

// 添加选项
const addOption = () => {
  const label = String.fromCharCode(65 + form.options.length)
  form.options.push({ label, content: '' })
}

// 删除选项
const removeOption = (index: number) => {
  form.options.splice(index, 1)
  // 更新选项标签
  form.options.forEach((option, i) => {
    option.label = String.fromCharCode(65 + i)
  })
  // 清理答案中已删除的选项
  form.answer = form.answer.filter(a => 
    form.options.some(o => o.label === a)
  )
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = {
          ...form,
          bankId: props.bankId
        }
        
        if (props.type === 'add') {
          await createQuestion(data)
          ElMessage.success('新增成功')
        } else {
          await updateQuestion(props.question!.id, data)
          ElMessage.success('更新成功')
        }
        
        emit('success')
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.option-item {
  display: flex;
  gap: 12px;
  align-items: center;
}

.option-item .el-input {
  flex: 1;
}
</style> 