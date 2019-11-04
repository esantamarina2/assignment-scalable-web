package com.esantamarina.assignmentscalableweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomApiException {

    private final Instant timestamp = Instant.now();
    private HttpStatus status;
    private String message;
    private String debugMessage;

}
