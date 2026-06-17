package allDemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import allDemo.dao.KYCDAO;
import allDemo.entity.KYCApplicant;
import java.util.List;


@Service
public class KYCService {

    private KYCDAO dao;

    @Autowired
    public KYCService(KYCDAO dao) {
        this.dao = dao;
    }

    public void register(KYCApplicant a) {
        a.updateRiskCategory();
        dao.addApplicant(a);
    }

    public List<KYCApplicant> getAll() {
        return dao.getAllApplicants();
    }

    public KYCApplicant find(int id) {
        return dao.getById(id);
    }

    public List<KYCApplicant> riskWise(String risk) {
        return dao.getByRisk(risk);
    }

    public boolean approve(int id) {
        KYCApplicant a = dao.getById(id);
        if (a != null) {
            a.approveKYC();
            return true;
        }
        return false;
    }

    public boolean remove(int id) {
        return dao.deleteApplicant(id);
    }

    public int count() {
        return dao.totalApplicants();
    }
}
