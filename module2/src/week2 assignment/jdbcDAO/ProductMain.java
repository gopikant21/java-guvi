package day4.jdbcDAO;

import java.sql.Connection;
import java.util.List;

import day4.jdbcDAO.dao.ProductDAO;
import day4.jdbcDAO.ui.ProductDAOimpl;
import day4.jdbcDAO.entity.Product;
import day4.jdbcDAO.connection.DBManager;

public class ProductMain {
    public static void main(String[] args) {

        Connection conn = null;

        try {
            conn = DBManager.getConnection();

            ProductDAO dao = new ProductDAOimpl(conn);

            Product p1 = new Product(0, "Laptop", 75000, "Electronics");
            dao.addProduct(p1);

            Product p2 = new Product(0, "Phone", 25000, "Electronics");
            dao.addProduct(p2);

            Product product = dao.getProductById(1);
            System.out.println("By ID: " + product);

            List<Product> all = dao.getAllProducts();
            System.out.println("All Products:");
            for (Product p : all) {
                System.out.println(p);
            }

            if (product != null) {
                product.setPrice(80000);
                dao.updateProduct(product);
            }

            List<Product> byCategory = dao.getProductsByCategory("Electronics");
            System.out.println("Products by Category:");
            for (Product p : byCategory) {
                System.out.println(p);
            }

            System.out.println("Count: " + dao.countProducts());

            List<Product> sorted = dao.sortByName();
            System.out.println("Sorted:");
            for (Product p : sorted) {
                System.out.println(p);
            }

            System.out.println("Exists ID 1: " + dao.exists(1));

            dao.deleteProduct(2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(conn);
        }
    }
}

