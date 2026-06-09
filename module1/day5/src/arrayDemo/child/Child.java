package arrayDemo.child;

public class Child {
    String fname;
    String lname;
    String dob; //dd-mm-yy

    public Child(String fname, String lname, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getDob() {
        return dob;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    @Override
    public String toString() {
        return "Child{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }

}
