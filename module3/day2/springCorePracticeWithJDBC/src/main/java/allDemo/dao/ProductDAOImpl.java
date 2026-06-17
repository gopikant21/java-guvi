package allDemo.dao;

import allDemo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // INSERT PRODUCT
    @Override
    public void addProduct(Product product) {

        String sql = "INSERT INTO product (product_id, product_name, price) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                product.getProductId(),
                product.getProductName(),
                product.getPrice()
        );
    }

    // GET ALL PRODUCTS
    @Override
    public List<Product> getAllProducts() {

        String sql = "SELECT * FROM product";

        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Product.class));
    }

    // GET PRODUCT BY ID
    @Override
    public Product getProductById(int productId) {

        String sql = "SELECT * FROM product WHERE product_id = ?";

        List<Product> list = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Product.class),
                productId
        );

        return list.isEmpty() ? null : list.get(0);
    }
}