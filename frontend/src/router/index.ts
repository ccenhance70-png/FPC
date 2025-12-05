import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import NotesListView from '../views/NotesListView.vue'
import NoteEditView from '../views/NoteEditView.vue'
import NoteDetailView from '../views/NoteDetailView.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/notes',
    name: 'notes-list',
    component: NotesListView
  },
  {
    path: '/notes/:id',
    name: 'note-detail',
    component: NoteDetailView
  },
  {
    path: '/notes/edit',
    name: 'note-create',
    component: NoteEditView
  },
  {
    path: '/notes/edit/:id',
    name: 'note-edit',
    component: NoteEditView
  },
  {
    path: '/',
    redirect: '/notes'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
