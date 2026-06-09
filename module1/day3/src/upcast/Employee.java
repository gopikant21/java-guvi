package upcast;

public class Employee extends Person{
    String dep;
    public Employee(String name,String dep){
        super(name);
        this.dep=dep;
    }

    public String getDep(){
        return dep;
    }

    public void setDep(String dep){
        this.dep=dep;
    }
}
