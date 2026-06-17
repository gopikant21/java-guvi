package allDemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import allDemo.dao.ProductDAO;
import allDemo.entity.Product;

import java.util.List;

@Service
public class ProductService {

    private ProductDAO productDAO;

    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void saveProduct(Product product) {
        productDAO.addProduct(product);
    }

    public List<Product> getProducts() {
        return productDAO.getAllProducts();
    }

    public Product findProduct(int productId) {
        return productDAO.getProductById(productId);
    }
}