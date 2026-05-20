package transactionmanager.challange.core.usecase;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.core.model.UpsertTransactionResult;

@Component
@RequiredArgsConstructor
public class UpsertTransactionUseCase {

    private final TransactionGateway transactionGateway;

    public UpsertTransactionResult execute(Long id, Double amount, String type, Long parentId) {
        Optional<Transaction> existing = transactionGateway.findById(id);

        Transaction toSave = existing
                .map(e -> e.toBuilder()
                        .amount(amount)
                        .type(type)
                        .parentId(parentId)
                        .build())
                .orElse(Transaction.builder()
                        .id(id)
                        .amount(amount)
                        .type(type)
                        .parentId(parentId)
                        .build());

        Transaction saved = transactionGateway.save(toSave);
        return new UpsertTransactionResult(saved, existing.isEmpty());
    }
}
