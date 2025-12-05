<template>
  <div class="notes-list-page">
    <div class="page-header">
      <h2 class="title">我的笔记</h2>
      <el-button type="primary" @click="goCreate">新建笔记</el-button>
    </div>

    <el-card shadow="never">
      <el-table :data="notes" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="courseName" label="课程名" min-width="160" />
        <el-table-column prop="tags" label="标签" min-width="180" />
        <el-table-column prop="createdAt" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-space size="small">
              <el-button size="small" type="primary" link @click="goView(scope.row.id)">查看</el-button>
              <el-button size="small" type="primary" link @click="goEdit(scope.row.id)">编辑</el-button>
              <el-button size="small" type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

interface NoteItem {
  id: number
  title: string
  courseName?: string
  tags?: string
  createdAt?: string
}

const router = useRouter()
const notes = ref<NoteItem[]>([])
const loading = ref(false)

const loadNotes = async () => {
  loading.value = true
  try {
    const { data } = await request.get('/notes', { params: { page: 1, pageSize: 20 } })
    const payload = data?.data
    if (Array.isArray(payload)) {
      notes.value = payload
    } else if (payload?.records) {
      notes.value = payload.records
    } else {
      notes.value = []
    }
  } catch (error) {
    ElMessage.error('加载笔记列表失败')
  } finally {
    loading.value = false
  }
}

const goCreate = () => router.push('/notes/edit')
const goView = (id: number) => router.push(`/notes/${id}`)
const goEdit = (id: number) => router.push(`/notes/edit/${id}`)

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确认删除该笔记吗？', '提示', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
    await request.delete(`/notes/${id}`)
    ElMessage.success('删除成功')
    await loadNotes()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.notes-list-page {
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
