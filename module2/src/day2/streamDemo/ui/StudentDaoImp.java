package day2.streamDemo.ui;

import day2.streamDemo.Dao.StudentDAO;
import day2.streamDemo.entity.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentDaoImp implements StudentDAO {

    private List<Student> studentList = new ArrayList<>();

    @Override
    public void insertStudent(Student student) {
        studentList.add(student);
    }

    // Max marks per subject → create one Student holding max values
    @Override
    public List<Student> getMaxMarksPerSubject() {
        if (studentList.isEmpty()) return List.of();

        int phy = studentList.stream().mapToInt(Student::getPhyMarks).max().orElse(0);
        int chem = studentList.stream().mapToInt(Student::getChemMarks).max().orElse(0);
        int maths = studentList.stream().mapToInt(Student::getMathsMarks).max().orElse(0);
        int bio = studentList.stream().mapToInt(Student::getBioMarks).max().orElse(0);
        int his = studentList.stream().mapToInt(Student::getHisMarks).max().orElse(0);
        int geo = studentList.stream().mapToInt(Student::getGeoMarks).max().orElse(0);

        Student maxStudent = new Student("MAX", phy, chem, maths, bio, his, geo);
        return List.of(maxStudent);
    }

    // Min marks per subject
    @Override
    public Iterable<Student> getMinMarksPerSubject() {
        if (studentList.isEmpty()) return List.of();

        int phy = studentList.stream().mapToInt(Student::getPhyMarks).min().orElse(0);
        int chem = studentList.stream().mapToInt(Student::getChemMarks).min().orElse(0);
        int maths = studentList.stream().mapToInt(Student::getMathsMarks).min().orElse(0);
        int bio = studentList.stream().mapToInt(Student::getBioMarks).min().orElse(0);
        int his = studentList.stream().mapToInt(Student::getHisMarks).min().orElse(0);
        int geo = studentList.stream().mapToInt(Student::getGeoMarks).min().orElse(0);

        return List.of(new Student("MIN", phy, chem, maths, bio, his, geo));
    }

    // Average marks per subject
    @Override
    public Student getAverageMarksPerSubject() {
        if (studentList.isEmpty()) return null;

        int phy = (int) studentList.stream().mapToInt(Student::getPhyMarks).average().orElse(0);
        int chem = (int) studentList.stream().mapToInt(Student::getChemMarks).average().orElse(0);
        int maths = (int) studentList.stream().mapToInt(Student::getMathsMarks).average().orElse(0);
        int bio = (int) studentList.stream().mapToInt(Student::getBioMarks).average().orElse(0);
        int his = (int) studentList.stream().mapToInt(Student::getHisMarks).average().orElse(0);
        int geo = (int) studentList.stream().mapToInt(Student::getGeoMarks).average().orElse(0);

        return new Student("AVG", phy, chem, maths, bio, his, geo);
    }

    @Override
    public Iterable<Student> getAllStudents() {
        return studentList;
    }

    // Topper per subject (returns list of 6 students, one per subject)
    @Override
    public Iterable<Student> getTopperPerSubject() {
        if (studentList.isEmpty()) return List.of();

        Student phyTopper = Collections.max(studentList, Comparator.comparingInt(Student::getPhyMarks));
        Student chemTopper = Collections.max(studentList, Comparator.comparingInt(Student::getChemMarks));
        Student mathsTopper = Collections.max(studentList, Comparator.comparingInt(Student::getMathsMarks));
        Student bioTopper = Collections.max(studentList, Comparator.comparingInt(Student::getBioMarks));
        Student hisTopper = Collections.max(studentList, Comparator.comparingInt(Student::getHisMarks));
        Student geoTopper = Collections.max(studentList, Comparator.comparingInt(Student::getGeoMarks));

        return List.of(phyTopper, chemTopper, mathsTopper, bioTopper, hisTopper, geoTopper);
    }

    // Students scoring above average in Physics
    @Override
    public Iterable<Student> getAboveAverageInPhy() {
        if (studentList.isEmpty()) return List.of();

        double avgPhy = studentList.stream()
                .mapToInt(Student::getPhyMarks)
                .average()
                .orElse(0);

        return studentList.stream()
                .filter(s -> s.getPhyMarks() > avgPhy)
                .collect(Collectors.toList());
    }

    // Overall topper (based on total marks)
    @Override
    public Student getTotalTopper() {
        return studentList.stream()
                .max(Comparator.comparingInt(s ->
                        s.getPhyMarks() +
                                s.getChemMarks() +
                                s.getMathsMarks() +
                                s.getBioMarks() +
                                s.getHisMarks() +
                                s.getGeoMarks()))
                .orElse(null);
    }
}