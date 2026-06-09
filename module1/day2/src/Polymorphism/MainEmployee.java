package Polymorphism;

// downcasting/ upcasting
public class MainEmployee {
    public static void main(String[] args) {
        Person emp = new Employee();
        emp.setName("dhsgjh");
        emp.setAge(23);

        Employee downCastedEmp = (Employee) emp;
        downCastedEmp.setEmpId("23278");
        System.out.println(emp.name + " " + emp.age);
        downCastedEmp.work();
        emp.showDetails();
    }
}
