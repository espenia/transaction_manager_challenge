package transactionmanager.challange.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "id")
@Getter
@Builder(toBuilder = true)
public class Transaction {

    private Long id;

    private Double amount;

    private String type;

    private Long parentId;
}
