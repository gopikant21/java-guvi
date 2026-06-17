package dao;


import entity.CreditScore;
import java.util.List;

public interface CreditScoreDAO {

    void save(CreditScore creditScore);

    CreditScore findByCustomerId(int customerId);

    List<CreditScore> findAll();
}
