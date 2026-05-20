package transactionmanager.challange.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import transactionmanager.challange.core.model.Transaction;

@Getter
@Builder
public class TransactionResponse {

    private Double amount;

    private String type;

    @JsonProperty("parent_id")
    private Long parentId;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .parentId(transaction.getParentId())
                .build();
    }
}
