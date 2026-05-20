package transactionmanager.challange.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpsertTransactionRequest {

    private Double amount;

    private String type;

    @JsonProperty("parent_id")
    private Long parentId;
}
