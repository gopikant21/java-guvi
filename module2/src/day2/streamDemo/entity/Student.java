package day2.streamDemo.entity;

public class Student {
    String name;
    int phyMarks;
    int chemMarks;
    int mathsMarks;
    int bioMarks;
    int hisMarks;
    int geoMarks;

    public Student(String name, int phyMarks, int chemMarks, int mathsMarks, int bioMarks, int hisMarks, int geoMarks) {
        this.name = name;
        this.phyMarks = phyMarks;
        this.chemMarks = chemMarks;
        this.mathsMarks = mathsMarks;
        this.bioMarks = bioMarks;
        this.hisMarks = hisMarks;
        this.geoMarks = geoMarks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPhyMarks() {
        return phyMarks;
    }

    public void setPhyMarks(int phyMarks) {
        this.phyMarks = phyMarks;
    }

    public int getChemMarks() {
        return chemMarks;
    }

    public void setChemMarks(int chemMarks) {
        this.chemMarks = chemMarks;
    }

    public int getMathsMarks() {
        return mathsMarks;
    }

    public void setMathsMarks(int mathsMarks) {
        this.mathsMarks = mathsMarks;
    }

    public int getBioMarks() {
        return bioMarks;
    }

    public void setBioMarks(int bioMarks) {
        this.bioMarks = bioMarks;
    }

    public int getHisMarks() {
        return hisMarks;
    }

    public void setHisMarks(int hisMarks) {
        this.hisMarks = hisMarks;
    }

    public int getGeoMarks() {
        return geoMarks;
    }

    public void setGeoMarks(int geoMarks) {
        this.geoMarks = geoMarks;
    }

    @Override
    public String toString() {
        return "Student: [name=" + name + ", phyMarks=" + phyMarks + ", chemMarks=" + chemMarks + ", mathsMarks=" + mathsMarks + ", bioMarks=" + bioMarks + ", hisMarks=" + hisMarks + ", geoMarks=" + geoMarks + "]";
    }
}
