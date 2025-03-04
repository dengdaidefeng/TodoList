package org.example.todo.Repository;

import org.example.todo.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByCompleted(boolean completed);

    void deleteByCompleted(boolean b);
}
