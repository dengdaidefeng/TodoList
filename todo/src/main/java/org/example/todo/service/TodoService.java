package org.example.todo.service;

import org.example.todo.Entity.Todo;
import org.example.todo.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public List<Todo> findCompletedTodos() {
        return todoRepository.findByCompleted(true);
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public void clearCompleted() {
        todoRepository.deleteByCompleted(true);
    }

    public Todo updateTodoStatus(Long id, boolean completed) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setCompleted(completed);
        return todoRepository.save(todo);
    }
}
