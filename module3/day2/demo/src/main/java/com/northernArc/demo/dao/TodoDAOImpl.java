package com.northernArc.demo.dao;

import com.northernArc.demo.model.Todo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TodoDAOImpl implements TodoDAO {

    private Map<Integer, Todo> todos;

    @Override
    public void save(Todo todo) {
        todos.put(todo.getId(), todo);
    }

    @PostConstruct
    public void init(){
        todos = new HashMap<>();
        todos.put(1, new Todo(1, "eggs", true));
        todos.put(2, new Todo(2, "murgi", false));
        todos.put(3, new Todo(3, "ghora", false));
    }

    @Override
    public Map<Integer, Todo> findAll() {
        return todos;
    }

    @Override
    public void deleteById(int id) {
        todos.remove(id);
    }

    @Override
    public Todo findById(int id) {
        return todos.get(id);
    }

    @PreDestroy
    public void destroy(){
        todos.clear();
    }
}
