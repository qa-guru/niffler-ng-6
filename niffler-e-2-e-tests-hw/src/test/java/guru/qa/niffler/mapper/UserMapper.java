package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserModel;

import java.nio.charset.StandardCharsets;

public class UserMapper {

    public UserEntity toEntity(UserModel userModel) {
        return UserEntity.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .currency(userModel.getCurrency())
                .firstName(userModel.getFirstName())
                .surname(userModel.getSurname())
                .photo(userModel.getPhoto() != null
                        ? userModel.getPhoto().getBytes(StandardCharsets.UTF_8)
                        : null
                )
                .photoSmall(userModel.getPhotoSmall() != null
                        ? userModel.getPhotoSmall().getBytes(StandardCharsets.UTF_8)
                        : null)
                .fullName(userModel.getFullName())
                .build();
    }

    public AuthUserJson toAuthDto(UserModel userModel) {
        return AuthUserJson.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password(userModel.getPassword())
                .build();
    }

    public UserModel toDto(UserEntity userEntity) {
        return UserModel.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .currency(userEntity.getCurrency())
                .firstName(userEntity.getFirstName())
                .surname(userEntity.getSurname())
                .photo(
                        userEntity.getPhoto() != null && userEntity.getPhoto().length > 0
                                ? new String(userEntity.getPhoto(), StandardCharsets.UTF_8)
                                : null)
                .photoSmall(
                        userEntity.getPhotoSmall() != null && userEntity.getPhotoSmall().length > 0
                                ? new String(userEntity.getPhotoSmall(), StandardCharsets.UTF_8)
                                : null)
                .fullName(userEntity.getFullName())
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