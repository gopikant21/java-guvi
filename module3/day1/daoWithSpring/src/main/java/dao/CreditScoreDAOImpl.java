package dao;


import entity.CreditScore;

import java.util.ArrayList;
import java.util.List;

public class CreditScoreDAOImpl implements CreditScoreDAO {

    private List<CreditScore> creditScoreList = new ArrayList<>();

    @Override
    public void save(CreditScore creditScore) {
        creditScoreList.add(creditScore);
    }

    @Override
    public CreditScore findByCustomerId(int customerId) {
        for (CreditScore cs : creditScoreList) {
            if (cs.getCustomerId() == (customerId)) {
                return cs;
            }
        }
        return null;
    }

    @Override
    public List<CreditScore> findAll() {
        return creditScoreList;
    }
}
