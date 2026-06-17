package allDemo.dao;

import allDemo.entity.Product;
import java.util.List;

public interface ProductDAO {

    void addProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(int productId);
}