package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.enums.AuthorityEnum;
import guru.qa.niffler.model.AuthAuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthAuthorityEntity implements Serializable {
  private UUID id;
  private AuthorityEnum authority;
  private UUID userId;

  public static AuthAuthorityEntity fromJson(AuthAuthorityJson json) {
    AuthAuthorityEntity aaue = new AuthAuthorityEntity();
    aaue.setId(json.id());
    aaue.setUserId(json.userId());
    aaue.setAuthority(json.authority());
    return aaue;
  }
}
