package io.joaco.mangovaultserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsData {

    private String username;

    @Builder.Default
    private Set<String> friends = new HashSet<>();
}
