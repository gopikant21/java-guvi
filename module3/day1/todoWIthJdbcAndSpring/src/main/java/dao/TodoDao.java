package dao;

import entity.Todo;
import java.util.List;

public interface TodoDao {

     void save(Todo todo);
     Todo findById(String id);
     List<Todo> findAll();
     void update(Todo todo);
     void delete(String id);
     List<Todo> findByCompleted(boolean done);
     List<Todo> findByDescription(String keyword);
     long countTodos();
}