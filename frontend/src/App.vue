<template>
  <main class="app-shell">
    <header class="hero">
      <h1>FPC Notes</h1>
      <p>Manage and explore your class notes.</p>
    </header>
    <section class="content">
      <router-view />
    </section>
  </main>
</template>

<script setup lang="ts">
</script>

<style scoped>
.app-shell {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px 16px 48px;
}

.hero {
  text-align: center;
  margin-bottom: 24px;
}

.content {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.06);
}
</style>
