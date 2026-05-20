package transactionmanager.challange.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.model.UpsertTransactionResult;
import transactionmanager.challange.core.usecase.UpsertTransactionUseCase;
import transactionmanager.challange.entrypoint.dto.StatusResponse;
import transactionmanager.challange.entrypoint.dto.TransactionResponse;
import transactionmanager.challange.entrypoint.dto.UpsertTransactionRequest;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final UpsertTransactionUseCase upsertTransactionUseCase;

    // The response for this method breaks REST conventions, the response should be always the same, 
    // the challenge requires to return different responses for created and updated transactions, so I implemented it as required, 
    // but in a real world scenario I would return the same response for both cases, with the transaction details, 
    // and use the status code to differentiate between created and updated.
    @PutMapping("/{transaction_id}")
    public ResponseEntity<?> transactions(
            @PathVariable("transaction_id") Long transactionId,
            @RequestBody UpsertTransactionRequest request) {

        UpsertTransactionResult result = upsertTransactionUseCase.execute(
                transactionId,
                request.getAmount(),
                request.getType(),
                request.getParentId());

        if (result.isCreated()) {
            return ResponseEntity.status(201).body(StatusResponse.ok());
        }

        return ResponseEntity.ok(TransactionResponse.from(result.getTransaction()));
    }
}
