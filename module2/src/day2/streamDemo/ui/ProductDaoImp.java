package day2.streamDemo.ui;

import day2.streamDemo.Dao.ProductDAO;
import day2.streamDemo.entity.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductDaoImp implements ProductDAO {

    private List<Product> productList = new ArrayList<>();

    //Save
    @Override
    public void save(Product product) {
        productList.add(product);
    }

    //Find by ID
    @Override
    public Product findById(int id) {
        for (Product p : productList) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    //Update
    @Override
    public void update(Product product) {
        for (Product p : productList) {
            if (p.getId() == product.getId()) {
                p.setName(product.getName());
                p.setBrand(product.getBrand());
                p.setPrice(product.getPrice());
                p.setCategory(product.getCategory());
            }
        }
    }

    //Delete by ID
    @Override
    public void deleteById(int id) {
        productList.removeIf(p -> p.getId() == id);
    }

    //Delete All
    @Override
    public void deleteAll() {
        productList.clear();
    }
    //Find All
    @Override
    public Iterable<Product> findAll() {
        return productList;
    }

    //Find by Name
    @Override
    public Iterable<Product> findByName(String name) {
        /*List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(name)) {
                result.add(p);
            }
        }
        return result;*/
        return productList.stream().filter((Product p)->p.getName().equalsIgnoreCase(name)).toList();
    }

    //Find by Brand
    @Override
    public Iterable<Product> findByBrand(String brand) {
        /*List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            if (p.getBrand().equalsIgnoreCase(brand)) {
                result.add(p);
            }
        }
        return result;*/
        return productList.stream().filter((Product p)->p.getBrand().equalsIgnoreCase(brand)).toList();
    }

    //Find by Category
    @Override
    public Iterable<Product> findByCategory(String category) {
        /*List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;*/
        return productList.stream().filter((Product p)->p.getCategory().equalsIgnoreCase(category)).toList();
    }

    //Find by Price Range
    @Override
    public Iterable<Product> findByPriceRange(double min, double max) {
        List<Product> result = new ArrayList<>();
        for (Product p : productList) {
            if (p.getPrice() >= min && p.getPrice() <= max) {
                result.add(p);
            }
        }
        return result;
    }

    //Sort by Name ASC
    @Override
    public Iterable<Product> sortByNameAsc() {
        List<Product> list = new ArrayList<>(productList);
        list.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        return list;
    }

    //Sort by Name DESC
    @Override
    public Iterable<Product> sortByNameDesc() {
        List<Product> list = new ArrayList<>(productList);
        list.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
        return list;
    }

    //Sort by Price ASC
    @Override
    public Iterable<Product> sortByPriceAsc() {
        List<Product> list = new ArrayList<>(productList);
        list.sort(Comparator.comparingDouble(Product::getPrice));
        return list;
    }

    //Sort by Price DESC
    @Override
    public Iterable<Product> sortByPriceDesc() {
        List<Product> list = new ArrayList<>(productList);
        list.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        return list;
    }

    //Exists by ID
    @Override
    public boolean existsById(int id) {
        return productList.stream().anyMatch(p -> p.getId() == id);
    }

    //Count
    @Override
    public long count() {
        return productList.size();
    }
}