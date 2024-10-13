package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.helper.EnumHelper;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserModel;

import java.util.Collections;

public class UserUtils {

    public static UserModel generateValidUser() {

        Faker fake = new Faker();
        var firstName = fake.name().firstName();
        var surname = fake.name().lastName();
        var password = fake.internet().password();

        var username = fake.name().username();
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

}