package transactionmanager.challange.infra.inmemory.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Repository;

import transactionmanager.challange.infra.inmemory.entity.TransactionEntity;
import transactionmanager.challange.infra.inmemory.repository.TransactionRepository;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final ConcurrentHashMap<Long, TransactionEntity> store = new ConcurrentHashMap<>();

    @Override
    public TransactionEntity save(TransactionEntity entity) {
        if (entity.getId() == null) {
            entity = entity.toBuilder().id(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE)).build();
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<TransactionEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<TransactionEntity> findAll() {
        return new ArrayList<>(store.values());
    }
}
