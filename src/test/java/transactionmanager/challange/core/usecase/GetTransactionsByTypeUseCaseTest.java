package transactionmanager.challange.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import transactionmanager.challange.core.gateway.TransactionGateway;
import transactionmanager.challange.util.UnitTest;

class GetTransactionsByTypeUseCaseTest extends UnitTest {

    @MockBean
    private TransactionGateway transactionGateway;

    @Autowired
    private GetTransactionsByTypeUseCase useCase;

    @Test
    void execute_returnsIdsFromGateway() {
        // Given
        when(transactionGateway.findIdsByType("DEBIT")).thenReturn(List.of(1L, 2L, 3L));

        // When
        List<Long> result = useCase.execute("DEBIT");

        // Then
        assertEquals(List.of(1L, 2L, 3L), result);
    }

    @Test
    void execute_whenNoTransactionsFound_returnsEmptyList() {
        // Given
        when(transactionGateway.findIdsByType("UNKNOWN")).thenReturn(List.of());

        // When
        List<Long> result = useCase.execute("UNKNOWN");

        // Then
        assertTrue(result.isEmpty());
    }
}
