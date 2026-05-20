package transactionmanager.challange.core.usecase;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import transactionmanager.challange.core.exception.NotFoundException;
import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.core.model.Transaction;

@Component
@RequiredArgsConstructor
public class GetTransactionSumUseCase {

    private final TransactionGateway transactionGateway;

    public Double execute(Long transactionId) {

        // As we dont have a real database, we need to load all transactions in memory to perform the sum.
        // In a real world scenario with a SQL database (adjacency list model, parent_id column) we would use
        // a recursive CTE with INNER JOIN to traverse only the relevant subtree entirely in the database:
        //
        //   WITH RECURSIVE subtree AS (
        //       SELECT id, amount, parent_id          -- anchor: root transaction
        //       FROM transactions
        //       WHERE id = :transactionId
        //
        //       UNION ALL
        //
        //       SELECT t.id, t.amount, t.parent_id    -- recursive: children of already-collected nodes
        //       FROM transactions t
        //       INNER JOIN subtree s ON t.parent_id = s.id
        //   )
        //   SELECT SUM(amount) AS sum FROM subtree;
        //
        // This avoids loading all transactions into application memory and lets the database
        // do the tree traversal and aggregation efficiently, returning only the final sum.

        List<Transaction> all = transactionGateway.findAll();

        Transaction root = all.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElseThrow(NotFoundException::new);

        // Build parent -> children index
        Map<Long, List<Transaction>> byParent = all.stream()
                .filter(t -> t.getParentId() != null)
                .collect(Collectors.groupingBy(Transaction::getParentId));

        // BFS to collect the subtree rooted at transactionId
        double sum = 0.0;
        Deque<Transaction> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Transaction current = queue.poll();
            sum += current.getAmount();
            List<Transaction> children = byParent.get(current.getId());
            if (children != null) {
                queue.addAll(children);
            }
        }

        return sum;
    }
}
