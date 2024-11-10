package guru.qa.niffler.mapper;

import guru.qa.niffler.model.RegisterModel;
import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class RegisterModelMapper {

    public @Nonnull Map<String, String> toRegisterMap(RegisterModel source) {
        return Map.of(
                "_csrf", source.getCsrf(),
                "username", source.getUsername(),
                "password", source.getPassword(),
                "passwordSubmit", source.getPasswordConfirmation()
        );
    }

    public @Nonnull RegisterModel fromUserModel(UserJson source) {
        return RegisterModel.builder()
                .username(source.getUsername())
                .password(source.getPassword())
                .passwordConfirmation(source.getPasswordConfirmation())
                .build();
    }

}