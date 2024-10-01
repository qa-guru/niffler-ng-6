package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.model.AuthAuthorityJson;
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
        AuthAuthorityEntity aae = new AuthAuthorityEntity();
        aae.setId(json.id());
        aae.setUserId(json.userId());
        aae.setAuthority(json.authority());
        return aae;
    }
}
