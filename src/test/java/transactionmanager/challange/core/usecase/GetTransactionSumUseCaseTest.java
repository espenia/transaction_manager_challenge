package transactionmanager.challange.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import transactionmanager.challange.core.exception.NotFoundException;
import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.core.model.Transaction;
import transactionmanager.challange.util.UnitTest;

class GetTransactionSumUseCaseTest extends UnitTest {

    @MockBean
    private TransactionGateway transactionGateway;

    @Autowired
    private GetTransactionSumUseCase useCase;

    @Test
    void execute_whenTransactionHasNoChildren_returnsSingleAmount() {
        // Given
        Transaction t = Transaction.builder().id(1L).amount(100.0).type("DEBIT").parentId(null).build();
        when(transactionGateway.findAll()).thenReturn(List.of(t));

        // When
        Double sum = useCase.execute(1L);

        // Then
        assertEquals(100.0, sum);
    }

    @Test
    void execute_whenTransactionHasDirectChildren_returnsSumOfSubtree() {
        // Given
        Transaction parent = Transaction.builder().id(1L).amount(100.0).type("DEBIT").parentId(null).build();
        Transaction child1 = Transaction.builder().id(2L).amount(50.0).type("DEBIT").parentId(1L).build();
        Transaction child2 = Transaction.builder().id(3L).amount(25.0).type("CREDIT").parentId(1L).build();
        when(transactionGateway.findAll()).thenReturn(List.of(parent, child1, child2));

        // When
        Double sum = useCase.execute(1L);

        // Then
        assertEquals(175.0, sum);
    }

    @Test
    void execute_whenTransactionHasNestedChildren_returnsSumOfWholeSubtree() {
        // Given
        Transaction root = Transaction.builder().id(1L).amount(10.0).type("DEBIT").parentId(null).build();
        Transaction child = Transaction.builder().id(2L).amount(20.0).type("DEBIT").parentId(1L).build();
        Transaction grandchild = Transaction.builder().id(3L).amount(30.0).type("DEBIT").parentId(2L).build();
        when(transactionGateway.findAll()).thenReturn(List.of(root, child, grandchild));

        // When
        Double sum = useCase.execute(1L);

        // Then
        assertEquals(60.0, sum);
    }

    @Test
    void execute_whenTransactionNotFound_throwsNotFoundException() {
        // Given
        when(transactionGateway.findAll()).thenReturn(List.of());

        // When / Then
        assertThrows(NotFoundException.class, () -> useCase.execute(99L));
    }

    @Test
    void execute_sumOnlyIncludesSubtreeNotSiblings() {
        // Given
        Transaction root = Transaction.builder().id(1L).amount(10.0).type("DEBIT").parentId(null).build();
        Transaction child = Transaction.builder().id(2L).amount(20.0).type("DEBIT").parentId(1L).build();
        Transaction sibling = Transaction.builder().id(3L).amount(999.0).type("DEBIT").parentId(null).build();
        when(transactionGateway.findAll()).thenReturn(List.of(root, child, sibling));

        // When — sum of subtree rooted at 1, should not include sibling (id=3)
        Double sum = useCase.execute(1L);

        // Then
        assertEquals(30.0, sum);
    }
}
