package day1.doaArchitecture.dao;

import day1.doaArchitecture.entity.Product;

public interface ProductDAO {

    //Basic CRUD
    public void save(Product product);
    public Product findById(int id);
    public void update(Product product);
    public void deleteById(int id);
    public void deleteAll();

    //Read Operations
    public Iterable<Product> findAll();

    //Search / Filter
    public Iterable<Product> findByName(String name);
    public Iterable<Product> findByBrand(String brand);
    public Iterable<Product> findByCategory(String category);
    public Iterable<Product> findByPriceRange(double min, double max);

    //Sorting
    public Iterable<Product> sortByNameAsc();
    public Iterable<Product> sortByNameDesc();
    public Iterable<Product> sortByPriceAsc();
    public Iterable<Product> sortByPriceDesc();

    //Utility / Advanced
    //public boolean existsById(int id);
    public long count();
}

