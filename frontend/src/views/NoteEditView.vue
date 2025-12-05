<template>
  <div class="note-edit-page">
    <div class="page-header">
      <h2 class="title">{{ isEdit ? '编辑笔记' : '新建笔记' }}</h2>
      <el-button @click="goBack">返回列表</el-button>
    </div>

    <el-card shadow="never" v-loading="loading">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" status-icon>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>

        <el-form-item label="课程名">
          <el-input v-model="form.courseName" placeholder="请输入课程名" />
        </el-form-item>

        <el-form-item label="标签">
          <el-input v-model="form.tags" placeholder="标签可用逗号分隔" />
        </el-form-item>

        <el-form-item label="笔记内容">
          <el-input
            v-model="form.rawContent"
            type="textarea"
            :rows="12"
            placeholder="粘贴或输入课堂笔记内容"
          />
        </el-form-item>

        <el-form-item>
          <el-space>
            <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
            <el-button @click="goBack">取消</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import request from '../api/request'

type NoteForm = {
  title: string
  courseName: string
  tags: string
  rawContent: string
}

const router = useRouter()
const route = useRoute()
const formRef = ref<FormInstance>()
const loading = ref(false)
const saving = ref(false)
const form = reactive<NoteForm>({
  title: '',
  courseName: '',
  tags: '',
  rawContent: ''
})

const rules: FormRules<NoteForm> = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

const noteId = computed(() => route.params.id as string | undefined)
const isEdit = computed(() => Boolean(noteId.value))

const fetchNote = async () => {
  if (!noteId.value) return
  loading.value = true
  try {
    const { data } = await request.get(`/notes/${noteId.value}`)
    const payload = data?.data
    if (payload) {
      form.title = payload.title ?? ''
      form.courseName = payload.courseName ?? ''
      form.tags = payload.tags ?? ''
      form.rawContent = payload.rawContent ?? ''
    }
  } catch (error) {
    ElMessage.error('加载笔记失败')
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/notes')
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      if (isEdit.value && noteId.value) {
        await request.put(`/notes/${noteId.value}`, form)
        ElMessage.success('更新成功')
      } else {
        await request.post('/notes', form)
        ElMessage.success('创建成功')
      }
      router.push('/notes')
    } catch (error) {
      ElMessage.error('保存失败')
    } finally {
      saving.value = false
    }
  })
}

onMounted(() => {
  fetchNote()
})
</script>

<style scoped>
.note-edit-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
}
</style>
