package transactionmanager.challange.infra.gateway.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;
import transactionmanager.challange.infra.inmemory.repository.TransactionRepository;

@Component
@RequiredArgsConstructor
public class TransactionGatewayImpl implements TransactionGateway {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity saved = transactionRepository.save(toEntity(transaction));
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id).map(this::toDomain);
    }

    private TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .parentId(transaction.getParentId())
                .build();
    }

    private Transaction toDomain(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .type(entity.getType())
                .parentId(entity.getParentId())
                .build();
    }
}
