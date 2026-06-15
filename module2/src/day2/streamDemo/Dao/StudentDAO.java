package day2.streamDemo.Dao;

import day2.streamDemo.entity.Student;

import java.util.List;

public interface StudentDAO {
    public void insertStudent(Student student);
    public Iterable<Student> getMaxMarksPerSubject();
    public Iterable<Student> getMinMarksPerSubject();
    public Student getAverageMarksPerSubject();
    public Iterable<Student> getAllStudents();
    public Iterable<Student> getTopperPerSubject();
    public Iterable<Student> getAboveAverageInPhy();
    public Student getTotalTopper();

}
