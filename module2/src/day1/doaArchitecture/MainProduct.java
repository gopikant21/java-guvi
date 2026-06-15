package day1.doaArchitecture;

import day1.doaArchitecture.dao.ProductDAO;
import day1.doaArchitecture.entity.Product;
import day1.doaArchitecture.ui.ProductDaoImp;

public class MainProduct {
    public static void main(String[] args) {

        //Create DAO Object
        ProductDAO dao = new ProductDaoImp();

        //Add Products
        dao.save(new Product(1, "iPhone", "Apple", 80000, "Mobile"));
        dao.save(new Product(2, "Galaxy", "Samsung", 70000, "Mobile"));
        dao.save(new Product(3, "AirPods", "Apple", 20000, "Accessories"));
        dao.save(new Product(4, "Laptop", "Dell", 60000, "Electronics"));

        //Display All Products
        System.out.println("All Products:");
        for (Product p : dao.findAll()) {
            System.out.println(p);
        }

        //Find By ID
        System.out.println("\nFind By ID (2):");
        System.out.println(dao.findById(2));

        //Update Product
        dao.update(new Product(2, "Galaxy S23", "Samsung", 75000, "Mobile"));

        //Find By Brand
        System.out.println("\nProducts by Brand (Apple):");
        for (Product p : dao.findByBrand("Apple")) {
            System.out.println(p);
        }

        //Find By Category
        System.out.println("\nProducts by Category (Mobile):");
        for (Product p : dao.findByCategory("Mobile")) {
            System.out.println(p);
        }

        //Find By Price Range
        System.out.println("\nProducts between 20000 and 80000:");
        for (Product p : dao.findByPriceRange(20000, 80000)) {
            System.out.println(p);
        }

        //Sort by Price ASC
        System.out.println("\nSorted by Price ASC:");
        for (Product p : dao.sortByPriceAsc()) {
            System.out.println(p);
        }

        //Sort by Name DESC
        System.out.println("\nSorted by Name DESC:");
        for (Product p : dao.sortByNameDesc()) {
            System.out.println(p);
        }

        //Count
        System.out.println("\nTotal Products: " + dao.count());

        //Exists
        //System.out.println("\nDoes ID 3 exist? " + dao.existsById(3));

        //Delete Product
        dao.deleteById(3);

        System.out.println("\nAfter Deletion:");
        for (Product p : dao.findAll()) {
            System.out.println(p);
        }

        //Delete All
        dao.deleteAll();

        System.out.println("\nAfter Deleting All Products:");
        System.out.println("Count: " + dao.count());
    }
}