package transactionmanager.challange.infra.inmemory.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Builder(toBuilder = true)
public class SampleEntity {

    private String id;

    // Add additional fields as needed
}
