package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public static AuthUserEntity fromJson(AuthUserJson json) {
        AuthUserEntity aue = new AuthUserEntity();
        aue.setId(json.id());
        aue.setUsername(json.username());
        aue.setPassword(json.password());
        aue.setEnabled(json.enabled());
        aue.setAccountNonExpired(json.accountNonExpired());
        aue.setAccountNonLocked(json.accountNonLocked());
        aue.setCredentialsNonExpired(json.credentialsNonExpired());
        List<AuthorityEntity> authorityEntities = json.authorities().stream()
                .map(authorityJson -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setId(authorityJson.id());
                    authorityEntity.setAuthority(AuthorityEntity.Authority.valueOf(authorityJson.authority()));
                    return authorityEntity;
                })
                .toList();

        aue.setAuthorities(authorityEntities);

        return aue;
    }
}
