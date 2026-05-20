package twitter.challenge.espenia.infra.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Document(collection = "sample")
public class SampleDocument extends BaseDocument {

    @Id
    private String id;

    @Override
    public boolean isNew() {
        return id == null || id.isEmpty();
    }
    // Add additional fields as needed
}
