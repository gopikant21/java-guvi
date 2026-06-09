package association;

//has a relationship -> one to one mapping
public class Passport {
    private String passportNo;
    private String country;
    private String issueDate;
    private String expiryDate;
    private Person person;

    public Passport(String passportNo, String country, String issueDate, String expiryDate) {
        this.passportNo = passportNo;
        this.country = country;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public String getPassportNo() {
        return passportNo;
    }
    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

}
