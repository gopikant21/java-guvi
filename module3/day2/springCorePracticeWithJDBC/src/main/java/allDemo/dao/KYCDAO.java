package allDemo.dao;

import allDemo.entity.KYCApplicant;
import java.util.List;

public interface KYCDAO {

    void addApplicant(KYCApplicant applicant);

    List<KYCApplicant> getAllApplicants();

    KYCApplicant getById(int id);

    List<KYCApplicant> getByRisk(String risk);

    boolean updateStatus(int id, String status);

    boolean deleteApplicant(int id);

    int totalApplicants();
}