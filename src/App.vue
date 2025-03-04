<template>
  <div class="app">
    <h1>我的待办事项</h1>
    
    <!-- 添加任务区域 -->
    <div class="input-section">
      <input
        v-model="newTodo"
        class="todo-input"
        placeholder="输入新任务"
        @keyup.enter="addTodo"
      >
      <button class="add-btn" @click="addTodo">
        {{ loading ? '添加中...' : '添加' }}
      </button>
    </div>

    <!-- 过滤和统计区域 -->
    <div class="controls">
      <div class="stats">
        总计: {{ todos.length }} 项 / 未完成: {{ incompleteCount }} 项
      </div>
      <div class="filters">
        <button
          v-for="filter in filters"
          :key="filter"
          :class="{ active: currentFilter === filter }"
          @click="currentFilter = filter"
        >
          {{ filter }}
        </button>
      </div>
      <button
        class="clear-btn"
        @click="clearCompleted"
        :disabled="completedCount === 0"
      >
        清除已完成 ({{ completedCount }})
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 任务列表 -->
    <ul v-else class="todo-list">
      <li
        v-for="todo in filteredTodos"
        :key="todo.id"
        class="todo-item"
        :class="{ completed: todo.completed }"
      >
        <div class="todo-content">
          <input
            type="checkbox"
            :checked="todo.completed"
            @change="updateStatus(todo.id, !todo.completed)"
            class="todo-checkbox"
          >
          <span class="todo-text">{{ todo.mes }}</span>
          <span class="todo-time">{{ formatTime(todo.createdAt) }}</span>
        </div>
        <button class="delete-btn" @click="deleteTodo(todo.id)">×</button>
      </li>
      
      <li v-if="filteredTodos.length === 0" class="empty-tip">
        {{ emptyMessage }}
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import dayjs from 'dayjs';
import { ElMessage } from 'element-plus';
import type { Todo } from '@/types/todo';
import api from '@/utils/api';

// 类型定义
type FilterType = 'all' | 'active' | 'completed';

// 响应式数据
const todos = ref<Todo[]>([]);
const newTodo = ref('');
const loading = ref(false);
const currentFilter = ref<FilterType>('all');

// 常量定义
const filters = ['all', 'active', 'completed'] as const;

// 计算属性
const incompleteCount = computed(() => 
  todos.value.filter(t => !t.completed).length
);

const completedCount = computed(() => 
  todos.value.filter(t => t.completed).length
);

const filteredTodos = computed(() => {
  switch (currentFilter.value) {
    case 'active':
      return todos.value.filter(t => !t.completed);
    case 'completed':
      return todos.value.filter(t => t.completed);
    default:
      return todos.value;
  }
});

const emptyMessage = computed(() => {
  switch (currentFilter.value) {
    case 'active': return '暂无未完成任务';
    case 'completed': return '暂无已完成任务';
    default: return '暂无任务，快去添加吧！';
  }
});

// 方法
const formatTime = (time?: string) => {
  return time ? dayjs(time).format('MM-DD HH:mm') : '--';
};

const fetchTodos = async () => {
  try {
    loading.value = true;
    const response = await api.get<Todo[]>('/todos');
    todos.value = response.data;
  } catch (error) {
    ElMessage.error('加载任务失败');
  } finally {
    loading.value = false;
  }
};

const addTodo = async () => {
  if (!newTodo.value.trim()) return;

  try {
    loading.value = true;
    await api.post('/todos', {
      mes: newTodo.value.trim(),
      completed: false
    });
    newTodo.value = '';
    await fetchTodos();
  } catch (error) {
    ElMessage.error('添加任务失败');
  } finally {
    loading.value = false;
  }
};

const deleteTodo = async (id: number) => {
  try {
    await api.delete(`/todos/${id}`);
    await fetchTodos();
  } catch (error) {
    ElMessage.error('删除任务失败');
  }
};

const updateStatus = async (id: number, completed: boolean) => {
  console.log(id,completed);
  try {
    await api.patch(`/todos/${id}`, completed, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
    await fetchTodos();
  } catch (error) {
    ElMessage.error('更新状态失败');
  }
};

const clearCompleted = async () => {
  try {
    await api.delete('/todos/clear-completed');
    await fetchTodos();
  } catch (error) {
    ElMessage.error('清除失败');
  }
};

// 初始化加载
onMounted(() => {
  fetchTodos();
});
</script>

<style scoped>
.app {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

h1 {
  text-align: center;
  color: #fff;
  background-color: #007bff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.input-section {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.todo-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.add-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-btn:hover {
  background-color: #0056b3;
}

.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
}

.stats {
  color: #666;
  font-size: 14px;
}

.filters button {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s;
}

.filters button.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.clear-btn {
  padding: 5px 10px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.clear-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background-color: #fff;
  margin-bottom: 8px;
  border-radius: 4px;
  transition: all 0.3s;
}

.todo-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.todo-content {
  display: flex;
  align-items: center;
  gap: 10px;
}

.todo-checkbox {
  width: 18px;
  height: 18px;
}

.todo-text {
  flex: 1;
}

.todo-text.completed {
  color: #999;
  text-decoration: line-through;
}

.todo-time {
  font-size: 12px;
  color: #666;
}

.delete-btn {
  background: none;
  border: none;
  color: #dc3545;
  font-size: 20px;
  cursor: pointer;
  padding: 0 8px;
}

.delete-btn:hover {
  color: #c82333;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.empty-tip {
  text-align: center;
  padding: 20px;
  color: #666;
  background-color: #fff;
  border-radius: 4px;
}
</style>