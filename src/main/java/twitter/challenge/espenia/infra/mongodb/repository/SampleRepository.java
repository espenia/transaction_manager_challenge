package twitter.challenge.espenia.infra.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import twitter.challenge.espenia.infra.mongodb.document.SampleDocument;

@Repository
public interface SampleRepository extends MongoRepository<SampleDocument, String> {
    // Add custom query methods if needed
}
