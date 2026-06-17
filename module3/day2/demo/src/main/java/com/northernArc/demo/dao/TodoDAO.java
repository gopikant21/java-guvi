package com.northernArc.demo.dao;

import com.northernArc.demo.model.Todo;

import java.util.Map;

public interface TodoDAO {
    public void save(Todo todo);
    public Map<Integer, Todo> findAll();
    public void deleteById(int id);
    public Todo findById(int id);
}
