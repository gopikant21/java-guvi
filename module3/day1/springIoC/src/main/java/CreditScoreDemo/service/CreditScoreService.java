package CreditScoreDemo.service;

import CreditScoreDemo.dao.CreditScoreDAO;
import CreditScoreDemo.entity.CreditScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditScoreService {
    private CreditScoreDAO creditScoreDAO;

    @Autowired
    public CreditScoreService(CreditScoreDAO creditScoreDAO) {
        this.creditScoreDAO = creditScoreDAO;
    }

    public void createScore(int customerId, int score) {
        String risk = calculateRisk(score);
        CreditScore cs = new CreditScore(customerId, score, risk);
        creditScoreDAO.save(cs);
    }

    public List<CreditScore> getAllScores() {
        return creditScoreDAO.findAll();
    }

    private String calculateRisk(int score) {
        if (score >= 750) return "LOW";
        else if (score >= 600) return "MEDIUM";
        else return "HIGH";
    }
}
