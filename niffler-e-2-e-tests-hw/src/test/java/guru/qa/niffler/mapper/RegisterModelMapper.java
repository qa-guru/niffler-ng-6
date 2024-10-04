package guru.qa.niffler.mapper;

import guru.qa.niffler.model.RegisterModel;
import guru.qa.niffler.model.UserModel;

import java.util.Map;

public class RegisterModelMapper {

    public Map<String, String> toRegisterMap(RegisterModel source) {
        return Map.of(
                "_csrf", source.csrf(),
                "username", source.username(),
                "password", source.password(),
                "passwordSubmit", source.passwordConfirmation()
        );
    }

    public RegisterModel fromUserModel(UserModel source) {
        return new RegisterModel(
                null,
                source.getUsername(),
                source.getPassword(),
                source.getPasswordConfirmation()
        );
    }


}
