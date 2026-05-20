package transactionmanager.challange.infra.inmemory.repository;

import java.util.List;
import java.util.Optional;

import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;

public interface TransactionRepository {

    TransactionEntity save(TransactionEntity entity);

    Optional<TransactionEntity> findById(Long id);

    List<TransactionEntity> findAll();

    List<TransactionEntity> findByType(String type);
}
