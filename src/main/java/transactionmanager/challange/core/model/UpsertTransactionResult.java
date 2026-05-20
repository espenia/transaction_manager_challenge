package transactionmanager.challange.core.model;

import lombok.Value;

@Value
public class UpsertTransactionResult {

    Transaction transaction;
    boolean created;
}
