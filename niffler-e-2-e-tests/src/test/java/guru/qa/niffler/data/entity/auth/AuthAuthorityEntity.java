package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthAuthorityJson;
import guru.qa.niffler.model.Authority;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthAuthorityEntity implements Serializable {
    private UUID id;
    private UUID userId;
    private Authority authority;

    public static AuthAuthorityEntity fromJson(AuthAuthorityJson json) {
        AuthAuthorityEntity entity = new AuthAuthorityEntity();
        entity.setId(json.id());
        entity.setUserId(json.userId());
        entity.setAuthority(json.authority());
        return entity;
    }
}
