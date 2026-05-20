package transactionmanager.challange.infra.inmemory.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;
import transactionmanager.challange.util.Factory;
import transactionmanager.challange.util.UnitTest;

class InMemoryTransactionRepositoryTest extends UnitTest {

    @Autowired
    private InMemoryTransactionRepository repository;

    @Test
    void save_whenIdIsNull_generatesId() {
        TransactionEntity entity = Factory.aTransactionEntityWithoutId();

        TransactionEntity saved = repository.save(entity);

        assertNotNull(saved.getId());
    }

    @Test
    void save_whenIdIsProvided_keepsId() {
        TransactionEntity entity = Factory.aTransactionEntity(50001L);

        TransactionEntity saved = repository.save(entity);

        assertEquals(50001L, saved.getId());
    }

    @Test
    void save_persistsEntity_andCanBeRetrieved() {
        repository.save(Factory.aTransactionEntity(50002L).toBuilder().amount(50.0).type("CREDIT").build());

        Optional<TransactionEntity> found = repository.findById(50002L);

        assertTrue(found.isPresent());
        assertEquals(50.0, found.get().getAmount());
    }

    @Test
    void findById_whenNotFound_returnsEmpty() {
        Optional<TransactionEntity> found = repository.findById(99999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_containsSavedEntities() {
        repository.save(Factory.aTransactionEntity(50005L));
        repository.save(Factory.aTransactionEntity(50006L));

        List<TransactionEntity> all = repository.findAll();

        assertTrue(all.stream().anyMatch(e -> e.getId().equals(50005L)));
        assertTrue(all.stream().anyMatch(e -> e.getId().equals(50006L)));
    }

    @Test
    void findByType_returnsOnlyMatchingEntities() {
        repository.save(Factory.aTransactionEntity(50007L).toBuilder().type("CREDIT").build());
        repository.save(Factory.aTransactionEntity(50008L).toBuilder().type("DEBIT").build());
        repository.save(Factory.aTransactionEntity(50009L).toBuilder().type("CREDIT").build());

        List<TransactionEntity> result = repository.findByType("CREDIT");

        assertTrue(result.stream().anyMatch(e -> e.getId().equals(50007L)));
        assertTrue(result.stream().anyMatch(e -> e.getId().equals(50009L)));
        assertTrue(result.stream().noneMatch(e -> e.getId().equals(50008L)));
    }

    @Test
    void findByType_whenNoneMatch_returnsEmptyList() {
        List<TransactionEntity> result = repository.findByType("NONEXISTENT");

        assertTrue(result.isEmpty());
    }
}
