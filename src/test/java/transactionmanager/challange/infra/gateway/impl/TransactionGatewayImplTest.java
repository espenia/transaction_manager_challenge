package transactionmanager.challange.infra.gateway.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;
import transactionmanager.challange.infra.inmemory.repository.TransactionRepository;
import transactionmanager.challange.util.Factory;
import transactionmanager.challange.util.UnitTest;

class TransactionGatewayImplTest extends UnitTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionGatewayImpl gateway;

    @Test
    void save_mapsTransactionToEntityAndReturnsDomain() {
        // Given
        Transaction transaction = Factory.aTransaction();
        TransactionEntity savedEntity = Factory.aTransactionEntity();
        when(transactionRepository.save(any())).thenReturn(savedEntity);

        // When
        Transaction result = gateway.save(transaction);

        // Then
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getAmount());
        assertEquals("DEBIT", result.getType());
    }

    @Test
    void findById_whenEntityExists_returnsMappedDomain() {
        // Given
        TransactionEntity entity = Factory.aTransactionEntity(2L).toBuilder().amount(50.0).type("CREDIT").parentId(10L).build();
        when(transactionRepository.findById(2L)).thenReturn(Optional.of(entity));

        // When
        Optional<Transaction> result = gateway.findById(2L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals(50.0, result.get().getAmount());
        assertEquals("CREDIT", result.get().getType());
        assertEquals(10L, result.get().getParentId());
    }

    @Test
    void findById_whenEntityNotFound_returnsEmpty() {
        // Given
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<Transaction> result = gateway.findById(99L);

        // Then
        assertTrue(result.isEmpty());
    }
}
