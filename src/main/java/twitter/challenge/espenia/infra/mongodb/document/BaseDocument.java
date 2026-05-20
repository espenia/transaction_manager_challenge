package twitter.challenge.espenia.infra.mongodb.document;

import lombok.*;
import org.springframework.data.domain.Persistable;

import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public abstract class BaseDocument implements Persistable<String> {

}
