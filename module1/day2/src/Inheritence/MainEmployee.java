package Inheritence;

public class MainEmployee {
    public static void main(String[] args) {
        Employee emp = new Employee("dghds", 56, "E123");
        emp.setName("dhsgjh");
        emp.setAge(23);
        /*emp.setEmpId("23278");*/
        System.out.println(emp.name + " " + emp.age);
        emp.work();
        emp.showDetails();
    }
}
