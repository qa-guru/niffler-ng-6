package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserMapper {

    public AuthUserEntity toEntity(AuthUserJson authUserJson) {
        return AuthUserEntity.builder()
                .id(authUserJson.getId())
                .username(authUserJson.getUsername())
                .accountNonExpired(authUserJson.isAccountNonExpired())
                .accountNonLocked(authUserJson.isAccountNonLocked())
                .credentialsNonExpired(authUserJson.isCredentialsNonExpired())
                .enabled(authUserJson.isEnabled())
                .password(authUserJson.getPassword())
                .build();
    }

    public AuthUserJson toDto(AuthUserEntity authUserEntity) {
        return AuthUserJson.builder()
                .id(authUserEntity.getId())
                .username(authUserEntity.getUsername())
                .enabled(authUserEntity.isEnabled())
                .accountNonExpired(authUserEntity.isAccountNonExpired())
                .accountNonLocked(authUserEntity.isAccountNonLocked())
                .enabled(authUserEntity.isEnabled())
                .credentialsNonExpired(authUserEntity.isCredentialsNonExpired())
                .password(authUserEntity.getPassword())
                .build();
    }

    public UserModel updateFromAnno(UserModel user, CreateNewUser anno) {
        return UserModel.builder()
                .id(user.getId())
                .username(
                        anno.username().isEmpty()
                                ? user.getUsername()
                                : anno.username())
                .currency(
                        (anno.currency() != CurrencyValues.USD || anno.notGenerateCurrency())
                                ? anno.currency()
                                : user.getCurrency())
                .password(
                        anno.password().isEmpty()
                                ? user.getPassword()
                                : anno.password())
                .passwordConfirmation(
                        anno.password().isEmpty()
                                ? user.getPasswordConfirmation()
                                : anno.password())
                .firstName(
                        anno.firstName().isEmpty()
                                ? user.getFirstName()
                                : anno.firstName())
                .surname(
                        anno.surname().isEmpty()
                                ? user.getSurname()
                                : anno.surname())
                .photo(user.getPhoto())
                .photoSmall(user.getPhotoSmall())
                .fullName(
                        anno.fullName().isEmpty()
                                ? user.getFullName()
                                : anno.fullName())
                .build();
    }

}