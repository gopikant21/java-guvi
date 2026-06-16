package main;

import config.SpringConfiguration;
import dao.TodoDao;
import entity.Todo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class App {

    public static void main(String[] args) {

        // Load Spring Context
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        // Get DAO bean from Spring
        TodoDao todoDao = context.getBean(TodoDao.class);

        // Create Todos
        Todo t1 = new Todo("1", "Learn Spring", false);
        Todo t2 = new Todo("2", "Practice Java", true);
        Todo t3 = new Todo("3", "Build Project", false);

        // Save Todos
        todoDao.save(t1);
        todoDao.save(t2);
        todoDao.save(t3);

        System.out.println("=== All Todos ===");
        printTodos(todoDao.findAll());

        // Find by ID
        System.out.println("\n=== Find by ID ===");
        System.out.println(todoDao.findById(t1.getId()));

        // Update
        System.out.println("\n=== Update Todo ===");
        t1.setDone(true);
        todoDao.update(t1);
        System.out.println(todoDao.findById(t1.getId()));

        // Find completed
        System.out.println("\n=== Completed Todos ===");
        printTodos(todoDao.findByCompleted(true));

        // Search by description
        System.out.println("\n=== Search 'Java' ===");
        printTodos(todoDao.findByDescription("Java"));

        // Count
        System.out.println("\nTotal Todos: " + todoDao.countTodos());

        // Delete
        System.out.println("\n=== Delete Todo ===");
        todoDao.delete(t2.getId());
        printTodos(todoDao.findAll());
    }

    // Utility method
    private static void printTodos(List<Todo> todos) {
        for (Todo t : todos) {
            System.out.println(t);
        }
    }
}
