package dao;

import entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class ProductDaoImplJdbc implements ProductDao {

    private JdbcTemplate jdbcTemplate;

    // Constructor Injection
    public ProductDaoImplJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper
    private RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setQuantity(rs.getInt("quantity"));
        p.setCategory(rs.getString("category"));
        p.setBrand(rs.getString("brand"));
        p.setAvailable(rs.getBoolean("available"));
        return p;
    };

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (id, name, description, price, quantity, category, brand, available) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getBrand(),
                product.isAvailable());
    }

    @Override
    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }


    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, quantity=?, category=?, brand=?, available=? WHERE id=?";
        jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getBrand(),
                product.isAvailable(),
                product.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Product> findByName(String name) {
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE ?";
        return jdbcTemplate.query(sql, rowMapper, "%" + name.toLowerCase() + "%");
    }

    @Override
    public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category=?";
        return jdbcTemplate.query(sql, rowMapper, category);
    }

    @Override
    public List<Product> findByBrand(String brand) {
        String sql = "SELECT * FROM products WHERE brand=?";
        return jdbcTemplate.query(sql, rowMapper, brand);
    }

    @Override
    public List<Product> findAvailableProducts() {
        String sql = "SELECT * FROM products WHERE available=true";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, rowMapper, minPrice, maxPrice);
    }

    @Override
    public long countProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}