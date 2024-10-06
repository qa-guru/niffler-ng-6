package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.UserModel;

public class UserUtils {

    public static UserModel generateValidUser() {

        Faker fake = new Faker();
        var username = fake.name().username();
        var password = fake.internet().password();
        var fullName = fake.name().fullName();
        var avatar = fake.internet().avatar();

        username = (username.length() > 50)
                ? username.substring(0, 50)
                : username;

        password = (password.length() > 12)
                ? password.substring(0, 12)
                : password;

        return UserModel.builder()
                .username(username)
                .password(password)
                .passwordConfirmation(password)
                .fullName(fullName)
                .avatar(avatar)
                .build();

    }

}