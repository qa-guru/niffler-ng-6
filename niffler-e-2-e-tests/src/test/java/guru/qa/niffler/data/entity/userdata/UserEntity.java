package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
public class UserEntity {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;
    private String fullname;

    public static UserEntity fromJson(UserJson json) {
        UserEntity ue = new UserEntity();
        ue.setId(json.id());
        ue.setUsername(json.username());
        ue.setCurrency(json.currency());
        ue.setFirstname(json.firstname());
        ue.setSurname(json.surname());
        ue.setPhoto(json.photo().getBytes(StandardCharsets.UTF_8));
        ue.setPhotoSmall(json.photo().getBytes(StandardCharsets.UTF_8));
        return ue;
    }
}
