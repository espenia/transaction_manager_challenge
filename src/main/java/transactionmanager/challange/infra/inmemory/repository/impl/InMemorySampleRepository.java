package transactionmanager.challange.infra.inmemory.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import transactionmanager.challange.infra.inmemory.entity.SampleEntity;
import transactionmanager.challange.infra.inmemory.repository.SampleRepository;

@Repository
public class InMemorySampleRepository implements SampleRepository {

    private final ConcurrentHashMap<String, SampleEntity> store = new ConcurrentHashMap<>();

    @Override
    public SampleEntity save(SampleEntity entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity = entity.toBuilder().id(UUID.randomUUID().toString()).build();
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<SampleEntity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<SampleEntity> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }
}
