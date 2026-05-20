package transactionmanager.challange.core.gateway;

import java.util.List;
import java.util.Optional;

import transactionmanager.challange.core.model.Transaction;

public interface TransactionGateway {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Long> findIdsByType(String type);

    List<Transaction> findAll();
}
