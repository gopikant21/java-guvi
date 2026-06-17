package services;

import dao.CreditScoreDAO;
import entity.CreditScore;

import java.util.List;

public class CreditScoreService {

    private CreditScoreDAO creditScoreDAO;


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
