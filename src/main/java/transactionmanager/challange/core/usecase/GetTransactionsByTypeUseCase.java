package transactionmanager.challange.core.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.gateway.TransactionGateway;

@Component
@RequiredArgsConstructor
public class GetTransactionsByTypeUseCase {

    private final TransactionGateway transactionGateway;

    public List<Long> execute(String type) {
        return transactionGateway.findIdsByType(type);
    }
}
