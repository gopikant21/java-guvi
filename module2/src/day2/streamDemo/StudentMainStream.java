package day2.streamDemo;

import day2.streamDemo.Dao.StudentDAO;
import day2.streamDemo.entity.Student;
import day2.streamDemo.ui.StudentDaoImp;

public class StudentMainStream {

    public static void main(String[] args) {

        StudentDAO dao = new StudentDaoImp();

        // Adding students
        dao.insertStudent(new Student("Gopi", 85, 78, 92, 88, 76, 80));
        dao.insertStudent(new Student("Ram", 90, 88, 75, 80, 85, 70));
        dao.insertStudent(new Student("Sita", 70, 95, 89, 92, 88, 91));
        dao.insertStudent(new Student("Kiran", 88, 82, 91, 85, 79, 84));
        dao.insertStudent(new Student("Anu", 60, 72, 68, 74, 66, 70));
        dao.insertStudent(new Student("Vijay", 95, 91, 93, 89, 90, 94));
        dao.insertStudent(new Student("Priya", 82, 85, 88, 86, 84, 83));
        dao.insertStudent(new Student("Rahul", 78, 69, 72, 75, 70, 68));
        dao.insertStudent(new Student("Sneha", 88, 92, 94, 90, 87, 89));
        dao.insertStudent(new Student("Arjun", 91, 87, 90, 85, 88, 86));
        dao.insertStudent(new Student("Meena", 73, 76, 79, 81, 77, 75));
        dao.insertStudent(new Student("Ravi", 65, 68, 70, 72, 69, 67));


        // All Students
        System.out.println("---- All Students ----");
        dao.getAllStudents().forEach(System.out::println);

        // Max Marks
        System.out.println("\n---- Max Marks Per Subject ----");
        dao.getMaxMarksPerSubject().forEach(System.out::println);

        // Min Marks
        System.out.println("\n---- Min Marks Per Subject ----");
        dao.getMinMarksPerSubject().forEach(System.out::println);

        // Average Marks
        System.out.println("\n---- Average Marks Per Subject ----");
        System.out.println(dao.getAverageMarksPerSubject());

        // Topper per subject
        System.out.println("\n---- Topper Per Subject ----");
        dao.getTopperPerSubject().forEach(System.out::println);

        // Above avg Physics
        System.out.println("\n---- Above Average in Physics ----");
        dao.getAboveAverageInPhy().forEach(System.out::println);

        // Total Topper
        System.out.println("\n---- Overall Topper ----");
        System.out.println(dao.getTotalTopper());
    }
}
