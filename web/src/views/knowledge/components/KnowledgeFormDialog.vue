<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="type === 'add' ? '新增知识点' : '编辑知识点'"
    width="500px"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="form.name"
          placeholder="请输入知识点名称"
        />
      </el-form-item>

      <el-form-item label="父节点" prop="parentId">
        <el-tree-select
          v-model="form.parentId"
          :data="treeData"
          :props="{ label: 'name', value: 'id' }"
          placeholder="请选择父节点"
          clearable
          :disabled="type === 'edit' && data?.level === 1"
        />
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入知识点描述"
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getKnowledgePoints, createKnowledgePoint, updateKnowledgePoint } from '@/api/knowledge'
import type { KnowledgePoint } from '@/api/knowledge'

const props = defineProps({
  modelValue: Boolean,
  type: {
    type: String as () => 'add' | 'edit',
    required: true
  },
  data: {
    type: Object as () => KnowledgePoint,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 表单相关
const formRef = ref<FormInstance>()
const loading = ref(false)
const treeData = ref<KnowledgePoint[]>([])

const form = reactive({
  name: '',
  parentId: undefined as number | undefined,
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入知识点名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ]
}

// 加载知识点树
const loadTree = async () => {
  try {
    const data = await getKnowledgePoints()
    // 编辑时过滤掉当前节点及其子节点
    if (props.type === 'edit' && props.data) {
      treeData.value = data.filter(node => !isChildNode(node, props.data!.id))
    } else {
      treeData.value = data
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取知识点列表失败')
  }
}

// 判断是否为子节点
const isChildNode = (node: KnowledgePoint, targetId: number): boolean => {
  if (node.id === targetId) return true
  if (node.children) {
    return node.children.some(child => isChildNode(child, targetId))
  }
  return false
}

// 初始化表单数据
const initForm = () => {
  if (props.type === 'edit' && props.data) {
    form.name = props.data.name
    form.parentId = props.data.parentId
    form.description = props.data.description || ''
  } else {
    form.name = ''
    form.parentId = undefined
    form.description = ''
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (props.type === 'add') {
          await createKnowledgePoint(form)
          ElMessage.success('新增成功')
        } else {
          await updateKnowledgePoint(props.data!.id, form)
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

onMounted(() => {
  loadTree()
  initForm()
})
</script> 