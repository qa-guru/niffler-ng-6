package guru.qa.niffler.mapper;

import guru.qa.niffler.model.LoginModel;
import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import java.util.Map;

public class LoginModelToMap {

    public Map<String, String> toLoginMap(@Nonnull LoginModel source) {
        return Map.of(
                "_csrf", source.getCsrf(),
                "username", source.getUsername(),
                "password", source.getPassword()
        );
    }

    public LoginModel fromUserModel(@Nonnull UserJson source) {
        return LoginModel.builder()
                .username(source.getUsername())
                .password(source.getPassword())
                .build();
    }

}