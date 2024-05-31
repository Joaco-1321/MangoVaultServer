package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.domain.dto.FriendRequestData;
import io.joaco.mangovaultserver.facade.FriendFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendFacade friendFacade;

    @GetMapping
    public ResponseEntity<?> getFriends(Principal principal) {
        return ResponseEntity.ok(friendFacade.getFriendsByUsername(principal.getName()));
    }

    @GetMapping("/request")
    public ResponseEntity<?> getFriendRequests(Principal principal) {
        return ResponseEntity.ok(friendFacade.getFriendRequestsByUsername(principal.getName()));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> removeFriend(Principal principal, @PathVariable String username) {
        friendFacade.removeFriend(principal.getName(), username);

        return ResponseEntity.ok()
                             .build();
    }

    @PreAuthorize("#friendRequestData.requester == authentication.principal.username")
    @PostMapping("/request/send")
    public ResponseEntity<?> sendRequest(@Valid @RequestBody FriendRequestData friendRequestData) {
        friendFacade.sendFriendRequest(friendRequestData);

        return ResponseEntity.ok()
                             .build();
    }

    @PreAuthorize("#friendRequestData.recipient == authentication.principal.username")
    @PostMapping("/request/accept")
    public ResponseEntity<?> acceptRequest(@Valid @RequestBody FriendRequestData friendRequestData) {
        friendFacade.acceptFriendRequest(friendRequestData);

        return ResponseEntity.ok()
                             .build();
    }

    @PreAuthorize("#friendRequestData.recipient == authentication.principal.username")
    @PostMapping("/request/reject")
    public ResponseEntity<?> rejectRequest(@Valid @RequestBody FriendRequestData friendRequestData) {
        friendFacade.rejectFriendRequest(friendRequestData);

        return ResponseEntity.ok()
                             .build();
    }

    @PreAuthorize("#friendRequestData.requester == authentication.principal.username")
    @PostMapping("/request/cancel")
    public ResponseEntity<?> cancelRequest(@Valid @RequestBody FriendRequestData friendRequestData) {
        friendFacade.cancelFriendRequest(friendRequestData);

        return ResponseEntity.ok()
                             .build();
    }
}
