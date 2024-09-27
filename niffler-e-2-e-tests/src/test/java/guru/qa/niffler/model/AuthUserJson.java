package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.UUID;

public record AuthUserJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("enabled")
        Boolean enabled,
        @JsonProperty("accountNonExpired")
        Boolean accountNonExpired,
        @JsonProperty("accountNonLocked")
        Boolean accountNonLocked,
        @JsonProperty("credentialsNonExpired")
        Boolean credentialsNonExpired,
        @JsonProperty("authorities")
        List<AuthorityJson> authorities) {

    public static AuthUserJson fromEntity(AuthUserEntity entity) {
        return new AuthUserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEnabled(),
                entity.getAccountNonExpired(),
                entity.getAccountNonLocked(),
                entity.getCredentialsNonExpired(),
                entity.getAuthorities().stream()
                        .map(authority -> new AuthorityJson(
                                authority.getId(),
                                authority.getAuthority().name()
                        ))
                        .toList()
        );
    }
}

