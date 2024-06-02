package io.joaco.mangovaultserver.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String sender;
    private String recipient;
    private String message;
    private Long timestamp;
}

