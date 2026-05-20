package transactionmanager.challange.entrypoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {

    private String status;

    public static StatusResponse ok() {
        return new StatusResponse("ok");
    }
}
