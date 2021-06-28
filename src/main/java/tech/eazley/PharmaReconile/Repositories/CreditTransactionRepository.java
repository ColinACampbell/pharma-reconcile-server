package tech.eazley.PharmaReconile.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.eazley.PharmaReconile.Models.CreditAccount;
import tech.eazley.PharmaReconile.Models.CreditTransaction;

@Repository
public interface CreditTransactionRepository extends CrudRepository<CreditTransaction,Integer> {
    CreditTransaction findByCreditAccount(CreditAccount creditAccount);
}
