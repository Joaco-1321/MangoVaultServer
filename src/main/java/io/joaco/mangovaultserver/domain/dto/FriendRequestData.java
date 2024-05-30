package io.joaco.mangovaultserver.domain.dto;

import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FriendRequestData {

    @NotBlank
    private String requester;

    @NotBlank
    private String recipient;

    private FriendRequestStatus status = FriendRequestStatus.PENDING;
}
