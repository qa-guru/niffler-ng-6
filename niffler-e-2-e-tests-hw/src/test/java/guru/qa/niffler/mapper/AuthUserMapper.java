package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.NonNull;

public class AuthUserMapper {

    public AuthUserEntity toEntity(@NonNull AuthUserJson authUserJson) {
        var authUser = AuthUserEntity.builder()
                .id(authUserJson.getId())
                .username(authUserJson.getUsername())
                .accountNonExpired(authUserJson.isAccountNonExpired())
                .accountNonLocked(authUserJson.isAccountNonLocked())
                .credentialsNonExpired(authUserJson.isCredentialsNonExpired())
                .enabled(authUserJson.isEnabled())
                .password(authUserJson.getPassword())
                .authorities(
                        authUserJson.getAuthorities().stream()
                                .map(new AuthAuthorityMapper()::toEntity)
                                .toList()
                )
                .build();
        authUser.getAuthorities()
                .forEach(a -> a.setUser(authUser));
        return authUser;
    }

    public AuthUserJson toDto(AuthUserEntity authUserEntity) {
        return AuthUserJson.builder()
                .id(authUserEntity.getId())
                .username(authUserEntity.getUsername())
                .enabled(authUserEntity.getEnabled())
                .accountNonExpired(authUserEntity.getAccountNonExpired())
                .accountNonLocked(authUserEntity.getAccountNonLocked())
                .enabled(authUserEntity.getEnabled())
                .credentialsNonExpired(authUserEntity.getCredentialsNonExpired())
                .password(authUserEntity.getPassword())
                .authorities(
                        authUserEntity.getAuthorities().stream()
                                .map(new AuthAuthorityMapper()::toDto)
                                .toList())
                .build();
    }

    public UserJson updateFromAnno(UserJson user, CreateNewUser anno) {
        return UserJson.builder()
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