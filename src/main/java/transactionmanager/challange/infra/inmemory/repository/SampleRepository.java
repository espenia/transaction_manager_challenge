package transactionmanager.challange.infra.inmemory.repository;

import java.util.List;
import java.util.Optional;

import transactionmanager.challange.infra.inmemory.entity.SampleEntity;

public interface SampleRepository {

    SampleEntity save(SampleEntity entity);

    Optional<SampleEntity> findById(String id);

    List<SampleEntity> findAll();

    void deleteById(String id);

    boolean existsById(String id);
}
