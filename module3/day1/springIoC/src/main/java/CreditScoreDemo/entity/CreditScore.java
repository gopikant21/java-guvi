package CreditScoreDemo.entity;

public class CreditScore {

    private int customerId;
    private int score;
    private String riskCategory;

    public CreditScore() {};

    public CreditScore(int customerId, int score, String riskCategory) {
        this.customerId = customerId;
        this.score = score;
        this.riskCategory = riskCategory;
    }

    public int getCustomerId() { return customerId; }
    public int getScore() { return score; }
    public String getRiskCategory() { return riskCategory; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setScore(int score) { this.score = score; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }

    @Override
    public String toString() {
        return "CreditScore{" +
                "customerId=" + customerId +
                ", score=" + score +
                ", riskCategory='" + riskCategory + '\'' +
                '}';
    }
}
