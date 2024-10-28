package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.enums.CurrencyValuesEnum;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
public class UdUserEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValuesEnum currency;
  private String firstname;
  private String surname;
  private String fullname;
  private byte[] photo;
  private byte[] photoSmall;

  public static UdUserEntity fromJson(UserJson json) {
    UdUserEntity ue = new UdUserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setCurrency(json.currency());
    ue.setFirstname(json.firstname());
    ue.setSurname(json.surname());
    ue.setPhoto(json.photo() != null ? json.photo().getBytes(StandardCharsets.UTF_8) : null);
    ue.setPhotoSmall(json.photoSmall() != null ? json.photoSmall().getBytes(StandardCharsets.UTF_8) : null);
    ue.setFullname(json.fullname());
    return ue;
  }
}