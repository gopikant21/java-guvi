package association;

public class Person {
    private String fname;
    private String lname;
    private Address address;

    public Person(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    public Person(String fname, String lname, Address address) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String toString() {
        return fname + " " + lname + " " + address;
    }
}
