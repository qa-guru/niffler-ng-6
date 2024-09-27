package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public static AuthUserEntity fromJson(AuthUserJson json) {
        AuthUserEntity aue = new AuthUserEntity();
        aue.setId(json.id());
        aue.setUsername(json.username());
        aue.setPassword(json.password());
        aue.setEnabled(json.enabled());
        aue.setAccountNonExpired(json.accountNonExpired());
        aue.setAccountNonLocked(json.accountNonLocked());
        aue.setCredentialsNonExpired(json.credentialsNonExpired());
        return aue;
    }
}