package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.UUID;

public record AuthAuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("userId")
        UUID userId,
        @JsonProperty("authority")
        Authority authority) {
    public static AuthAuthorityJson fromEntity(AuthAuthorityEntity entity) {
        return new AuthAuthorityJson(
                entity.getId(),
                entity.getUser().getId(),
                entity.getAuthority()
        );
    }
}
