package transactionmanager.challange.infra.inmemory.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Builder(toBuilder = true)
public class TransactionEntity {

    private Long id; // would normally use UUID or something similar, but challenge description specifies long so i will use it.

    private Double amount; // Its better to use BigDecimal for money, but challenge description specifies double so i will use it.

    private String type; // would normally use an enum for this, but challenge description does not specify the possible types, so i will use string.

    private Long parentId;
}
