package org.example.unittestingdemo.service;

import org.example.unittestingdemo.model.Product;
import org.example.unittestingdemo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
