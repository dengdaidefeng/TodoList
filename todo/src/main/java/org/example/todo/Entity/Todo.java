package org.example.todo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mes;
    private boolean completed;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Todo() {
        createdAt = LocalDateTime.now();
    }

    public Todo(String mes, boolean completed) {
        this.mes = mes;
        this.completed = completed;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        System.out.println(LocalDateTime.now());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreateAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
