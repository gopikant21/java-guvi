package allDemo.dao;

import allDemo.entity.KYCApplicant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KYCDAOImpl implements KYCDAO {

    private List<KYCApplicant> db = new ArrayList<>();

    @Override
    public void addApplicant(KYCApplicant applicant) {
        db.add(applicant);
    }

    @Override
    public List<KYCApplicant> getAllApplicants() {
        return db;
    }

    @Override
    public KYCApplicant getById(int id) {
        return db.stream()
                .filter(a -> a.getApplicantId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<KYCApplicant> getByRisk(String risk) {
        List<KYCApplicant> list = new ArrayList<>();
        for (KYCApplicant a : db) {
            if (a.getRiskCategory().equalsIgnoreCase(risk)) {
                list.add(a);
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        KYCApplicant a = getById(id);
        if (a != null) {
            a.setKycStatus(status);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteApplicant(int id) {
        return db.removeIf(a -> a.getApplicantId() == id);
    }

    @Override
    public int totalApplicants() {
        return db.size();
    }
}