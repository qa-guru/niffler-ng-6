package guru.qa.niffler.mapper;

import guru.qa.niffler.model.LoginModel;
import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class LoginModelToMap {

    public @Nonnull Map<String, String> toLoginMap(LoginModel source) {
        return Map.of(
                "_csrf", source.getCsrf(),
                "username", source.getUsername(),
                "password", source.getPassword()
        );
    }

    public @Nonnull LoginModel fromUserModel(UserJson source) {
        return LoginModel.builder()
                .username(source.getUsername())
                .password(source.getPassword())
                .build();
    }

}