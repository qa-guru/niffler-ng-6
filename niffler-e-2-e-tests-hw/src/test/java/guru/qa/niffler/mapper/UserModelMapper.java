package guru.qa.niffler.mapper;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.UserModel;

public class UserModelMapper {

    public UserModel updateFromAnno(UserModel user, CreateNewUser anno) {
        return UserModel.builder()
                .username(
                        anno.username().isEmpty()
                                ? user.getUsername()
                                : anno.username())
                .password(
                        anno.password().isEmpty()
                                ? user.getPassword()
                                : anno.password())
                .passwordConfirmation(
                        anno.password().isEmpty()
                                ? user.getPasswordConfirmation()
                                : anno.password())
                .fullName(
                        anno.fullName().isEmpty()
                                ? user.getFullName()
                                : anno.fullName())
                .avatar(
                        anno.avatar().isEmpty()
                                ? user.getAvatar()
                                : anno.avatar())
                .build();
    }

}
