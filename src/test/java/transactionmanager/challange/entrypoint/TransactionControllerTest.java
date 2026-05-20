package transactionmanager.challange.entrypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
