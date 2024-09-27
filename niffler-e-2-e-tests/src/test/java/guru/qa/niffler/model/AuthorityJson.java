package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(

        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        String authority) {

    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        return new AuthorityJson(
                entity.getId(),
                entity.getAuthority().name()
        );
    }

    public AuthorityEntity toEntity() {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setId(this.id());
        ae.setAuthority(AuthorityEntity.Authority.valueOf(this.authority()));

        return ae;
    }
}
