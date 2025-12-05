<template>
  <div class="card">
    <p>{{ msg }}</p>
    <button type="button" @click="fetchGreeting">Call backend</button>
    <p v-if="loading" class="status">Loading...</p>
    <p v-else-if="error" class="status error">{{ error }}</p>
    <p v-else-if="greeting" class="status success">{{ greeting }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  msg: {
    type: String,
    default: 'Hello World'
  }
})

const greeting = ref('')
const error = ref('')
const loading = ref(false)

async function fetchGreeting() {
  greeting.value = ''
  error.value = ''
  loading.value = true

  try {
    const response = await fetch('/api/hello')
    if (!response.ok) {
      throw new Error(`Request failed with status ${response.status}`)
    }

    const data = await response.json()
    greeting.value = data.message
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.card {
  display: grid;
  gap: 1rem;
  align-items: start;
}

button {
  background-color: #42b983;
  border: none;
  color: #fff;
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

button:hover {
  background-color: #369b6f;
}

.status {
  margin: 0;
}

.error {
  color: #e53935;
}

.success {
  color: #1b5e20;
}
</style>
