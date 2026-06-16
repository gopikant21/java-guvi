package dao;

import entity.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodoDaoImplCollections implements TodoDao {

    private List<Todo> todoList = new ArrayList<>();

    // Save
    @Override
    public void save(Todo todo) {
        // Generate ID if not present
        if (todo.getId() == null || todo.getId().isEmpty()) {
            todo.setId(UUID.randomUUID().toString());
        }
        todoList.add(todo);
    }

    // Find by ID
    @Override
    public Todo findById(String id) {
        // Convert Long → String for comparison
        String strId = String.valueOf(id);

        Optional<Todo> result = todoList.stream()
                .filter(t -> t.getId().equals(strId))
                .findFirst();

        return result.orElse(null);
    }

    // Find all
    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todoList);
    }

    // Update
    @Override
    public void update(Todo todo) {
        for (int i = 0; i < todoList.size(); i++) {
            if (todoList.get(i).getId().equals(todo.getId())) {
                todoList.set(i, todo);
                return;
            }
        }
    }

    // Delete
    @Override
    public void delete(String id) {
        String strId = String.valueOf(id);
        todoList.removeIf(t -> t.getId().equals(strId));
    }

    // Find by completed (done)
    @Override
    public List<Todo> findByCompleted(boolean completed) {
        return todoList.stream()
                .filter(t -> t.isDone() == completed)
                .collect(Collectors.toList());
    }

    // Find by title → mapped to description
    @Override
    public List<Todo> findByDescription(String keyword) {
        return todoList.stream()
                .filter(t -> t.getDescription() != null &&
                        t.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Count
    @Override
    public long countTodos() {
        return todoList.size();
    }
}
