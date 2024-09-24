package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CurrencyValues;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class UserEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String firstname;
  private String surname;
  private String fullname;
  private byte[] photo;
  private byte[] photoSmall;

  public static UserEntity fromJson(UserEntity json) {
    UserEntity ue = new UserEntity();
    ue.setId(json.id);
    ue.setUsername(json.username);
    ue.setCurrency(json.currency);
    ue.setFirstname(json.firstname);
    ue.setSurname(json.surname);
    ue.setFullname(json.fullname);
    ue.setPhoto(json.photo);
    ue.setPhotoSmall(json.photoSmall);
    return ue;
  }
}