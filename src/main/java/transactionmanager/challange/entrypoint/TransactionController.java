package transactionmanager.challange.entrypoint;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.model.UpsertTransactionResult;
import transactionmanager.challange.core.usecase.GetTransactionSumUseCase;
import transactionmanager.challange.core.usecase.GetTransactionsByTypeUseCase;
import transactionmanager.challange.core.usecase.UpsertTransactionUseCase;
import transactionmanager.challange.entrypoint.dto.StatusResponse;
import transactionmanager.challange.entrypoint.dto.SumResponse;
import transactionmanager.challange.entrypoint.dto.TransactionResponse;
import transactionmanager.challange.entrypoint.dto.UpsertTransactionRequest;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final UpsertTransactionUseCase upsertTransactionUseCase;
    private final GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;
    private final GetTransactionSumUseCase getTransactionSumUseCase;

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

    // Its inconvinient to return a list of ids for this endpoint, but the challenge requires it, so I implemented it as required,
    // in a real world scenario I would return a list of transactions, with all the details
    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getByType(@PathVariable("type") String type) {
        return ResponseEntity.ok(getTransactionsByTypeUseCase.execute(type));
    }

    // This endpoint does not really give a lot of information it could return the related transactions and their amounts, 
    // but the challenge requires to return only the sum, so I implemented it as required,
    // it could be argued that it could have too many transaction in this case we can a flag and pagination to return the related transactions, 
    // but for the sake of simplicity I will return only the sum as required.
    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<SumResponse> getSum(@PathVariable("transaction_id") Long transactionId) {
        return ResponseEntity.ok(new SumResponse(getTransactionSumUseCase.execute(transactionId)));
    }
}
