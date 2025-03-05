package org.example.todo.controller;

import org.example.todo.Entity.Todo;
import org.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAll() {
        return todoService.getAllTodos();
    }

    @GetMapping("/completed")
    public List<Todo> getCompletedTodos(){
        return todoService.findCompletedTodos();
    }

    @PostMapping
    public Todo addTodo(@RequestBody Todo todo) {
        System.out.println(todo.getCreatedAt());
        return todoService.addTodo(todo);
    }

    @DeleteMapping("{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

    @DeleteMapping("/clear-completed")
    public void clearCompletedTodos() {
        todoService.clearCompleted();
    }

    @PatchMapping("{id}")
    public Todo updateTodo(@PathVariable Long id,@RequestBody boolean completed) {
        System.out.println(completed);
        return todoService.updateTodoStatus(id,completed);
    }
}
