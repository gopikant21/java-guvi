package allDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import allDemo.entity.Product;
import allDemo.service.ProductService;

@Component
public class ProductConsoleController {

    private ProductService productService;

    @Autowired
    public ProductConsoleController(ProductService productService) {
        this.productService = productService;
    }

    public void start() {

        productService.saveProduct(
                new Product(101, "Laptop", 65000));

        productService.saveProduct(
                new Product(102, "Mobile", 25000));

        productService.saveProduct(
                new Product(103, "Headphones", 3000));

        System.out.println("All Products");

        productService.getProducts()
                .forEach(System.out::println);

        System.out.println("\nSearch Product");

        Product product =
                productService.findProduct(102);

        if (product != null) {
            System.out.println(product);
        } else {
            System.out.println("Product Not Found");
        }
    }
}