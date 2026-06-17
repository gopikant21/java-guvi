package allDemo.dao;

import allDemo.entity.KYCApplicant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class KYCDAOImpl implements KYCDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // INSERT
    @Override
    public void addApplicant(KYCApplicant a) {

        String sql = """
            INSERT INTO kyc_applicant (
                applicant_id, full_name, father_name, mother_name, gender, date_of_birth,
                aadhaar_number, pan_number, passport_number,
                email, phone, alternate_phone,
                address_line, city, state, pincode, country,
                annual_income, employment_type, organization_name,
                kyc_status, risk_category,
                aadhaar_verified, pan_verified, email_verified, phone_verified
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                a.getApplicantId(),
                a.getFullName(),
                a.getFatherName(),
                a.getMotherName(),
                a.getGender(),
                a.getDateOfBirth(),

                a.getAadhaarNumber(),
                a.getPanNumber(),
                a.getPassportNumber(),

                a.getEmail(),
                a.getPhone(),
                a.getAlternatePhone(),

                a.getAddressLine(),
                a.getCity(),
                a.getState(),
                a.getPincode(),
                a.getCountry(),

                a.getAnnualIncome(),
                a.getEmploymentType(),
                a.getOrganizationName(),

                a.getKycStatus(),
                a.getRiskCategory(),

                a.isAadhaarVerified(),
                a.isPanVerified(),
                a.isEmailVerified(),
                a.isPhoneVerified()
        );
    }

    // ROW MAPPER (IMPORTANT for complex entity)
    private KYCApplicant mapRow(ResultSet rs, int rowNum) throws SQLException {

        KYCApplicant a = new KYCApplicant();

        a.setApplicantId(rs.getInt("applicant_id"));
        a.setFullName(rs.getString("full_name"));
        a.setFatherName(rs.getString("father_name"));
        a.setMotherName(rs.getString("mother_name"));
        a.setGender(rs.getString("gender"));

        a.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());

        a.setAadhaarNumber(rs.getString("aadhaar_number"));
        a.setPanNumber(rs.getString("pan_number"));
        a.setPassportNumber(rs.getString("passport_number"));

        a.setEmail(rs.getString("email"));
        a.setPhone(rs.getString("phone"));
        a.setAlternatePhone(rs.getString("alternate_phone"));

        a.setAddressLine(rs.getString("address_line"));
        a.setCity(rs.getString("city"));
        a.setState(rs.getString("state"));
        a.setPincode(rs.getString("pincode"));
        a.setCountry(rs.getString("country"));

        a.setAnnualIncome(rs.getDouble("annual_income"));
        a.setEmploymentType(rs.getString("employment_type"));
        a.setOrganizationName(rs.getString("organization_name"));

        a.setKycStatus(rs.getString("kyc_status"));
        a.setRiskCategory(rs.getString("risk_category"));

        a.setAadhaarVerified(rs.getBoolean("aadhaar_verified"));
        a.setPanVerified(rs.getBoolean("pan_verified"));
        a.setEmailVerified(rs.getBoolean("email_verified"));
        a.setPhoneVerified(rs.getBoolean("phone_verified"));

        return a;
    }

    // SELECT ALL
    @Override
    public List<KYCApplicant> getAllApplicants() {
        String sql = "SELECT * FROM kyc_applicant";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    // SELECT BY ID
    @Override
    public KYCApplicant getById(int id) {
        String sql = "SELECT * FROM kyc_applicant WHERE applicant_id = ?";

        List<KYCApplicant> list = jdbcTemplate.query(sql, this::mapRow, id);
        return list.isEmpty() ? null : list.get(0);
    }

    // SELECT BY RISK
    @Override
    public List<KYCApplicant> getByRisk(String risk) {
        String sql = "SELECT * FROM kyc_applicant WHERE LOWER(risk_category) = LOWER(?)";
        return jdbcTemplate.query(sql, this::mapRow, risk);
    }

    // UPDATE STATUS
    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE kyc_applicant SET kyc_status = ? WHERE applicant_id = ?";
        return jdbcTemplate.update(sql, status, id) > 0;
    }

    // DELETE
    @Override
    public boolean deleteApplicant(int id) {
        String sql = "DELETE FROM kyc_applicant WHERE applicant_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // COUNT
    @Override
    public int totalApplicants() {
        String sql = "SELECT COUNT(*) FROM kyc_applicant";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}