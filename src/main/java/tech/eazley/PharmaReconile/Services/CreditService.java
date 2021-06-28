package tech.eazley.PharmaReconile.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.eazley.PharmaReconile.Models.CreditAccount;
import tech.eazley.PharmaReconile.Models.CreditTransaction;
import tech.eazley.PharmaReconile.Models.Pharmacy;
import tech.eazley.PharmaReconile.Repositories.CreditRepository;
import tech.eazley.PharmaReconile.Repositories.CreditTransactionRepository;

@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditTransactionRepository creditTransactionRepository;

    private void saveCreditAccount(CreditAccount creditAccount)
    {
        creditRepository.save(creditAccount);
    }

    private CreditAccount findByPharmacy(Pharmacy pharmacy)
    {
        return creditRepository.findByPharmacy(pharmacy);
    }

    private CreditTransaction findByCreditAccount(CreditAccount creditAccount)
    {
        return creditTransactionRepository.findByCreditAccount(creditAccount);
    }

    private void saveTransaction(CreditTransaction creditTransaction, CreditAccount creditAccount)
    {
        creditTransaction.setCreditAccount(creditAccount);
        creditTransactionRepository.save(creditTransaction);
    }
}
