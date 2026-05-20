package transactionmanager.challange.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.core.model.UpsertTransactionResult;
import transactionmanager.challange.util.Factory;
import transactionmanager.challange.util.UnitTest;

class UpsertTransactionUseCaseTest extends UnitTest {

    @MockBean
    private TransactionGateway transactionGateway;

    @Autowired
    private UpsertTransactionUseCase useCase;

    @Test
    void execute_whenTransactionDoesNotExist_returnsCreatedResult() {
        // Given
        Long id = 1L;
        Transaction saved = Factory.aTransaction(id);
        when(transactionGateway.findById(id)).thenReturn(Optional.empty());
        when(transactionGateway.save(any())).thenReturn(saved);

        // When
        UpsertTransactionResult result = useCase.execute(id, 100.0, "DEBIT", null);

        // Then
        assertTrue(result.isCreated());
        assertEquals(saved, result.getTransaction());
        verify(transactionGateway).save(any());
    }

    @Test
    void execute_whenTransactionExists_returnsUpdatedResult() {
        // Given
        Long id = 2L;
        Transaction existing = Factory.aTransaction(id).toBuilder().amount(50.0).type("CREDIT").build();
        Transaction updated = existing.toBuilder().amount(200.0).type("DEBIT").build();
        when(transactionGateway.findById(id)).thenReturn(Optional.of(existing));
        when(transactionGateway.save(any())).thenReturn(updated);

        // When
        UpsertTransactionResult result = useCase.execute(id, 200.0, "DEBIT", null);

        // Then
        assertFalse(result.isCreated());
        assertEquals(updated, result.getTransaction());
        verify(transactionGateway).save(any());
    }

    @Test
    void execute_whenUpdating_propagatesAllFields() {
        // Given
        Long id = 3L;
        Long parentId = 10L;
        Transaction existing = Factory.aTransaction(id).toBuilder().amount(50.0).type("CREDIT").build();
        when(transactionGateway.findById(id)).thenReturn(Optional.of(existing));
        when(transactionGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        UpsertTransactionResult result = useCase.execute(id, 300.0, "DEBIT", parentId);

        // Then
        assertEquals(300.0, result.getTransaction().getAmount());
        assertEquals("DEBIT", result.getTransaction().getType());
        assertEquals(parentId, result.getTransaction().getParentId());
    }
}
