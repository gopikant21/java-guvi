package day4.jdbcDAO.dao;

import java.util.List;
import day4.jdbcDAO.entity.Product;

public interface ProductDAO {

    void addProduct(Product product);

    Product getProductById(int id);

    List<Product> getAllProducts();

    void updateProduct(Product product);

    void deleteProduct(int id);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByName(String name);

    int countProducts();

    List<Product> sortByName();

    boolean exists(int id);

    void deleteAll();
}
