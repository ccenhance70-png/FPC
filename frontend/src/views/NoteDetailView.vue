<template>
  <div class="note-detail-page" v-loading="loading">
    <div class="header">
      <div class="info">
        <h2 class="title">{{ note?.title || '笔记详情' }}</h2>
        <div class="meta">
          <span v-if="note?.courseName">课程：{{ note.courseName }}</span>
          <span v-if="note?.tags">标签：{{ note.tags }}</span>
          <span v-if="note?.createdAt">创建时间：{{ note.createdAt }}</span>
        </div>
      </div>
      <el-button type="primary" :loading="summarizing" @click="handleSummarize">
        生成/重新生成 AI 总结
      </el-button>
    </div>

    <div class="content">
      <el-card class="content-card" shadow="hover">
        <template #header>
          <div class="card-header">原始笔记内容</div>
        </template>
        <pre class="raw-text">{{ note?.rawContent || '暂无内容' }}</pre>
      </el-card>

      <el-card class="content-card" shadow="hover">
        <template #header>
          <div class="card-header">AI 要点与总结</div>
        </template>
        <div v-if="hasSummary" class="ai-section">
          <div class="ai-block">
            <h4>要点</h4>
            <ul>
              <li v-for="(item, index) in aiKeypoints" :key="index">{{ item }}</li>
            </ul>
          </div>
          <div class="ai-block">
            <h4>总结</h4>
            <p>{{ note?.aiSummary }}</p>
          </div>
        </div>
        <div v-else class="empty-summary">还没有生成 AI 总结</div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

interface NoteDetail {
  id: number
  title: string
  courseName?: string
  tags?: string
  rawContent?: string
  aiKeypoints?: string
  aiSummary?: string
  createdAt?: string
  updatedAt?: string
}

const route = useRoute()
const note = ref<NoteDetail | null>(null)
const loading = ref(false)
const summarizing = ref(false)

const aiKeypoints = computed(() => {
  const raw = note.value?.aiKeypoints || ''
  return raw
    .split(/\n|,/)
    .map((item) => item.trim())
    .filter(Boolean)
})

const hasSummary = computed(() => aiKeypoints.value.length > 0 || !!note.value?.aiSummary)

const fetchNote = async () => {
  const id = route.params.id
  if (!id) return
  loading.value = true
  try {
    const { data } = await request.get(`/notes/${id}`)
    if (data?.data) {
      note.value = data.data
    } else {
      ElMessage.error('未找到笔记')
    }
  } catch (error) {
    ElMessage.error('加载笔记详情失败')
  } finally {
    loading.value = false
  }
}

const handleSummarize = async () => {
  const id = route.params.id
  if (!id) return
  summarizing.value = true
  try {
    const { data } = await request.post(`/notes/${id}/summarize`)
    if (data?.data) {
      note.value = data.data
      ElMessage.success('AI 总结已生成')
    } else {
      await fetchNote()
      ElMessage.success('AI 总结已生成')
    }
  } catch (error) {
    ElMessage.error('生成 AI 总结失败')
  } finally {
    summarizing.value = false
  }
}

onMounted(() => {
  fetchNote()
})
</script>

<style scoped>
.note-detail-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 700;
}

.meta {
  display: flex;
  gap: 16px;
  color: #666;
  flex-wrap: wrap;
}

.content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.content-card {
  height: 100%;
}

.card-header {
  font-weight: 600;
}

.raw-text {
  white-space: pre-wrap;
  margin: 0;
  line-height: 1.6;
  min-height: 240px;
}

.ai-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ai-block h4 {
  margin: 0 0 8px;
}

.ai-block ul {
  padding-left: 18px;
  margin: 0;
}

.ai-block p {
  margin: 0;
  line-height: 1.6;
}

.empty-summary {
  color: #999;
  text-align: center;
  padding: 24px 0;
}

@media (max-width: 960px) {
  .content {
    grid-template-columns: 1fr;
  }
}
</style>
