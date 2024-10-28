package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.io.Serializable;
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

  public static AuthUserEntity fromJson(AuthUserJson json) {
    AuthUserEntity aue = new AuthUserEntity();
    aue.setUsername(json.username());
    aue.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(json.password()));
    aue.setEnabled(json.enabled());
    aue.setAccountNonExpired(json.accountNonExpired());
    aue.setAccountNonLocked(json.accountNonLocked());
    aue.setAccountNonExpired(json.accountNonExpired());
    return aue;
  }
}
