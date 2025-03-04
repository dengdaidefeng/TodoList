<script setup lang="ts">
import { ref, onMounted } from 'vue';
import type { Todo } from '@/types/todo';
import api from '@/utils/api';

const todos = ref<Todo[]>([]);
const newTodo = ref('');
const loading = ref(false);

const fetchTodos = async () => {
  loading.value = true;
  try {
    const response = await api.get<Todo[]>('/todos');
    todos.value = response;
  } catch (error) {
    console.error('加载失败:', error);
  } finally {
    loading.value = false;
  }
};

const addTodo = async () => {
  if (!newTodo.value.trim()) return;
  try {
    await api.post('/todos', {
      mes: newTodo.value,
      completed: false
    });
    newTodo.value = '';
    await fetchTodos();
  } catch (error) {
    console.error('添加失败:', error);
  }
};

// 其他方法（deleteTodo、updateStatus）类似...
</script>