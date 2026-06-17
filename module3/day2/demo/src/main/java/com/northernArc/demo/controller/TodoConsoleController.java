package com.northernArc.demo.controller;

import com.northernArc.demo.dao.TodoDAO;
import com.northernArc.demo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TodoConsoleController {
    @Autowired
    private Scanner scanner;

    @Autowired
    private TodoDAO todoDAO;

    public void showMenu(){
        while(true){
            System.out.println("Welcome to your Todo application: ");
            System.out.println("Enter 1: add");
            System.out.println("Enter 2: delete");
            System.out.println("Enter 3: find");
            System.out.println("Enter 4: exit");
            int choice = scanner.nextInt();
            if(choice == 4){
                break;
            }
            redirect(choice);
        }
    }

    private void redirect(int choice) {
        switch(choice){
            case 1:
                add();
                break;
            case 2:
                delete();
                break;
            case 3:
                find();
                break;
        }
    }

    private void delete() {
        System.out.println("Enter ID to Delete: ");
        int id = scanner.nextInt();
        todoDAO.deleteById(id);
    }

    private void add() {
        System.out.print("Enter id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter content: ");
        String content = scanner.nextLine();
        System.out.println("Enter completed: ");
        boolean completed = scanner.nextBoolean();

        Todo todo = new Todo(id, content, completed);
        todoDAO.save(todo);
    }

    private void find() {
        System.out.println("Enter ID to find: ");
        int id = scanner.nextInt();
        System.out.println(todoDAO.findById(id));
    }
}
