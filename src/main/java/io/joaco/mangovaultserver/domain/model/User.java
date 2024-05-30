package io.joaco.mangovaultserver.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,
            unique = true,
            nullable = false)
    private String username;

    @Column(length = 100,
            nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private String role = "USER";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Singular
    @OneToMany(mappedBy = "requester")
    private Set<FriendRequest> sentFriendRequests = new HashSet<>();

    @Singular
    @OneToMany(mappedBy = "recipient")
    private Set<FriendRequest> receivedFriendRequests = new HashSet<>();

    @Singular
    @ManyToMany
    @JoinTable(name = "friends",
               joinColumns = @JoinColumn(name = "user_id1"),
               inverseJoinColumns = @JoinColumn(name = "user_id2"))
    private Set<User> friends = new HashSet<>();
}
