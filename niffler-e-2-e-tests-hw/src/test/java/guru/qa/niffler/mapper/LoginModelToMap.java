package guru.qa.niffler.mapper;

import guru.qa.niffler.model.LoginModel;
import guru.qa.niffler.model.UserModel;

import java.util.Map;

public class LoginModelToMap {

    public Map<String, String> toLoginMap(LoginModel source) {
        return Map.of(
                "_csrf", source.csrf(),
                "username", source.username(),
                "password", source.password()
        );
    }

    public LoginModel fromUserModel(UserModel source) {
        return new LoginModel(
                null,
                source.getUsername(),
                source.getPassword()
        );
    }


}
