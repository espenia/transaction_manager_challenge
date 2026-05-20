package transactionmanager.challange.entrypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import transactionmanager.challange.integration.ControllerTest;

class TransactionControllerTest extends ControllerTest {

    @Autowired
    TransactionControllerTest(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Test
    @SuppressWarnings("unchecked")
    void upsert_whenTransactionDoesNotExist_returns201AndStatusOk() {
        // Given
        Long newId = 800001L;
        HttpEntity<String> request = putEntity(Map.of("amount", 100.0, "type", "DEBIT"));

        // When
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/transaction/" + newId, HttpMethod.PUT, request, Map.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ok", response.getBody().get("status"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void upsert_whenTransactionAlreadyExists_returns200AndTransactionWithoutId() {
        // Given
        Long existingId = 800002L;
        HttpEntity<String> createRequest = putEntity(Map.of("amount", 50.0, "type", "CREDIT"));
        testRestTemplate.exchange("/transaction/" + existingId, HttpMethod.PUT, createRequest, Map.class);

        HttpEntity<String> updateRequest = putEntity(Map.of("amount", 200.0, "type", "DEBIT"));

        // When
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/transaction/" + existingId, HttpMethod.PUT, updateRequest, Map.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200.0, response.getBody().get("amount"));
        assertEquals("DEBIT", response.getBody().get("type"));
        assertFalse(response.getBody().containsKey("id"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void upsert_whenTransactionHasParentId_parentIdIsPersisted() {
        // Given
        Long newId = 800003L;
        HttpEntity<String> request = putEntity(Map.of("amount", 75.0, "type", "CREDIT", "parent_id", 800001L));

        // First call — create
        testRestTemplate.exchange("/transaction/" + newId, HttpMethod.PUT, request, Map.class);

        // Second call — update and read back
        HttpEntity<String> updateRequest = putEntity(Map.of("amount", 75.0, "type", "CREDIT", "parent_id", 800001L));
        ResponseEntity<Map> response = testRestTemplate.exchange(
                "/transaction/" + newId, HttpMethod.PUT, updateRequest, Map.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("parent_id"));
    }

    private HttpEntity<String> putEntity(Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void getByType_returnsIdsOfMatchingTransactions() {
        // Given — create two DEBIT and one CREDIT
        Long id1 = 900001L;
        Long id2 = 900002L;
        Long id3 = 900003L;
        testRestTemplate.exchange("/transaction/" + id1, HttpMethod.PUT, putEntity(Map.of("amount", 10.0, "type", "DEBIT")), Map.class);
        testRestTemplate.exchange("/transaction/" + id2, HttpMethod.PUT, putEntity(Map.of("amount", 20.0, "type", "DEBIT")), Map.class);
        testRestTemplate.exchange("/transaction/" + id3, HttpMethod.PUT, putEntity(Map.of("amount", 30.0, "type", "CREDIT")), Map.class);

        // When
        ResponseEntity<List> response = testRestTemplate.getForEntity("/transaction/types/DEBIT", List.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<Number> ids = response.getBody();
        assertTrue(ids.stream().anyMatch(id -> id.longValue() == id1));
        assertTrue(ids.stream().anyMatch(id -> id.longValue() == id2));
        assertFalse(ids.stream().anyMatch(id -> id.longValue() == id3));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getByType_whenNoTransactionsForType_returnsEmptyList() {
        // When
        ResponseEntity<List> response = testRestTemplate.getForEntity("/transaction/types/NONEXISTENT", List.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getSum_whenTransactionExistsWithChildren_returnsSumOfSubtree() {
        // Given — root + two children
        Long rootId = 810001L;
        Long childId1 = 810002L;
        Long childId2 = 810003L;
        testRestTemplate.exchange("/transaction/" + rootId, HttpMethod.PUT, putEntity(Map.of("amount", 10.0, "type", "DEBIT")), Map.class);
        testRestTemplate.exchange("/transaction/" + childId1, HttpMethod.PUT, putEntity(Map.of("amount", 20.0, "type", "DEBIT", "parent_id", rootId)), Map.class);
        testRestTemplate.exchange("/transaction/" + childId2, HttpMethod.PUT, putEntity(Map.of("amount", 30.0, "type", "DEBIT", "parent_id", rootId)), Map.class);

        // When
        ResponseEntity<Map> response = testRestTemplate.getForEntity("/transaction/sum/" + rootId, Map.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(60.0, ((Number) response.getBody().get("sum")).doubleValue());
    }

    @Test
    void getSum_whenTransactionDoesNotExist_returns404() {
        // When
        ResponseEntity<Map> response = testRestTemplate.getForEntity("/transaction/sum/999999999", Map.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
