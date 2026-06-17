package CreditScoreDemo.dao;

import CreditScoreDemo.entity.CreditScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

