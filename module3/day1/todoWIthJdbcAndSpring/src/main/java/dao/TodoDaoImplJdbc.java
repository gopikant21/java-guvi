package dao;

import entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.UUID;

public class TodoDaoImplJdbc implements TodoDao {

    private JdbcTemplate jdbcTemplate;

    // Constructor Injection
    public TodoDaoImplJdbc() {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper (can also move to separate class)
    private RowMapper<Todo> rowMapper = (rs, rowNum) -> new Todo(
            rs.getString("id"),
            rs.getString("description"),
            rs.getBoolean("done")
    );

    @Override
    public void save(Todo todo) {
        if (todo.getId() == null || todo.getId().isEmpty()) {
            todo.setId(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO todos (id, description, done) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                todo.getId(),
                todo.getDescription(),
                todo.isDone());
    }

    @Override
    public Todo findById(String id) {
        String sql = "SELECT * FROM todos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Todo> findAll() {
        String sql = "SELECT * FROM todos";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void update(Todo todo) {
        String sql = "UPDATE todos SET description = ?, done = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                todo.getDescription(),
                todo.isDone(),
                todo.getId());
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM todos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Todo> findByCompleted(boolean completed) {
        String sql = "SELECT * FROM todos WHERE done = ?";
        return jdbcTemplate.query(sql, rowMapper, completed);
    }

    @Override
    public List<Todo> findByDescription(String keyword) {
        String sql = "SELECT * FROM todos WHERE LOWER(description) LIKE ?";
        return jdbcTemplate.query(sql, rowMapper, "%" + keyword.toLowerCase() + "%");
    }

    @Override
    public long countTodos() {
        String sql = "SELECT COUNT(*) FROM todos";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}