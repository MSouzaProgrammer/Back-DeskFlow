package com.pato.deskflow.dto.response;

import java.time.LocalDateTime;

public record HistoryResponse(
    Long id,
    String userName,
    String action,
    LocalDateTime createdAt) {

}
