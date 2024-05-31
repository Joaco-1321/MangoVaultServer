package io.joaco.mangovaultserver.domain.dto;

import io.joaco.mangovaultserver.domain.model.FriendRequest.FriendRequestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendRequestData {

    @NotBlank
    private String requester;

    @NotBlank
    private String recipient;

    @Builder.Default
    private FriendRequestStatus status = FriendRequestStatus.PENDING;
}
