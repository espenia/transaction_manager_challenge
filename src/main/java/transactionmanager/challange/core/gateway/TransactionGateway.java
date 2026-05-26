package transactionmanager.challange.core.gateway;

import java.util.Optional;

import transactionmanager.challange.core.model.Transaction;

public interface TransactionGateway {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById2(Long id);
}
