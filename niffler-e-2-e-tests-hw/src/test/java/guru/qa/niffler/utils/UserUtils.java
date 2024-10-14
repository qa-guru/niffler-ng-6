package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.helper.EnumHelper;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserModel;

import java.util.Collections;

public class UserUtils {

    private static final Faker FAKE = new Faker();

    public static UserModel generateUser() {
        var password = generatePassword();
        var firstName = FAKE.name().firstName();
        var surname = FAKE.name().lastName();
        return UserModel.builder()
                .username(generateUsername())
                .password(password)
                .passwordConfirmation(password)
                .currency(EnumHelper.getRandomEnum(CurrencyValues.class))
                .firstName(firstName)
                .surname(surname)
                .photo(new byte[0])
                .photoSmall(new byte[0])
                .fullName(firstName + " " + surname)
                .categories(Collections.emptyList())
                .spendings(Collections.emptyList())
                .build();

    }

    public static AuthUserJson generateAuthUser() {
        return AuthUserJson.builder()
                .username(generateUsername())
                .password(generatePassword())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private static String generatePassword() {
        var password = FAKE.internet().password();
        return (password.length() > 12)
                ? password.substring(0, 12)
                : password;
    }

    private static String generateUsername() {
        var username = FAKE.name().username();
        return (username.length() > 50)
                ? username.substring(0, 50)
                : username;
    }

}