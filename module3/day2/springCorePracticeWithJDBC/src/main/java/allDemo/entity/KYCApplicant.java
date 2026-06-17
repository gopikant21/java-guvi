package allDemo.entity;

import java.time.LocalDate;

public class KYCApplicant {

    // Identity Info
    private int applicantId;
    private String fullName;
    private String fatherName;
    private String motherName;
    private String gender;
    private LocalDate dateOfBirth;

    // Documents
    private String aadhaarNumber;
    private String panNumber;
    private String passportNumber;

    // Contact Info
    private String email;
    private String phone;
    private String alternatePhone;

    // Address Info
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;

    // Financial Info
    private double annualIncome;
    private String employmentType; // Salaried / Self-Employed
    private String organizationName;

    // KYC Status
    private String kycStatus; // PENDING / VERIFIED / REJECTED
    private String riskCategory; // LOW / MEDIUM / HIGH

    // Verification flags
    private boolean aadhaarVerified;
    private boolean panVerified;
    private boolean emailVerified;
    private boolean phoneVerified;

    public KYCApplicant() {
    }

    public KYCApplicant(int applicantId, String fullName, String fatherName,
                        String motherName, String gender, LocalDate dateOfBirth,
                        String aadhaarNumber, String panNumber, String passportNumber,
                        String email, String phone, String alternatePhone,
                        String addressLine, String city, String state, String pincode,
                        String country, double annualIncome, String employmentType,
                        String organizationName, String kycStatus, String riskCategory) {

        this.applicantId = applicantId;
        this.fullName = fullName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.aadhaarNumber = aadhaarNumber;
        this.panNumber = panNumber;
        this.passportNumber = passportNumber;
        this.email = email;
        this.phone = phone;
        this.alternatePhone = alternatePhone;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
        this.annualIncome = annualIncome;
        this.employmentType = employmentType;
        this.organizationName = organizationName;
        this.kycStatus = kycStatus;
        this.riskCategory = riskCategory;
    }

    // Business Methods (important for NBFC logic)

    public void verifyAadhaar() {
        this.aadhaarVerified = true;
    }

    public void verifyPAN() {
        this.panVerified = true;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void verifyPhone() {
        this.phoneVerified = true;
    }

    public boolean isFullyVerified() {
        return aadhaarVerified && panVerified && emailVerified && phoneVerified;
    }

    public void updateRiskCategory() {
        if (annualIncome > 1000000) {
            riskCategory = "LOW";
        } else if (annualIncome > 500000) {
            riskCategory = "MEDIUM";
        } else {
            riskCategory = "HIGH";
        }
    }

    public void approveKYC() {
        if (isFullyVerified()) {
            this.kycStatus = "APPROVED";
        } else {
            this.kycStatus = "PENDING";
        }
    }

    @Override
    public String toString() {
        return "KYCApplicant{" +
                "applicantId=" + applicantId +
                ", fullName='" + fullName + '\'' +
                ", city='" + city + '\'' +
                ", annualIncome=" + annualIncome +
                ", kycStatus='" + kycStatus + '\'' +
                ", riskCategory='" + riskCategory + '\'' +
                ", verified=" + isFullyVerified() +
                '}';
    }

    // Getters & Setters omitted for brevity (can be generated)

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(String riskCategory) {
        this.riskCategory = riskCategory;
    }

    public boolean isAadhaarVerified() {
        return aadhaarVerified;
    }

    public void setAadhaarVerified(boolean aadhaarVerified) {
        this.aadhaarVerified = aadhaarVerified;
    }

    public boolean isPanVerified() {
        return panVerified;
    }

    public void setPanVerified(boolean panVerified) {
        this.panVerified = panVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }
}
