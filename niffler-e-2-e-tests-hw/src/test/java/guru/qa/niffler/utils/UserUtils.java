package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.AuthAuthorityJson;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.helper.EnumHelper;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserModel;

import java.util.Collections;
import java.util.List;

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
                .photo(null)
                .photoSmall(null)
                .fullName(firstName + " " + surname)
                .categories(Collections.emptyList())
                .spendings(Collections.emptyList())
                .build();

    }

    public static AuthUserJson generateAuthUser() {
        var user = AuthUserJson.builder()
                .username(generateUsername())
                .password(generatePassword())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

//        user.setAuthorities(
//                        List.of(
//                                AuthAuthorityJson.builder().authority(Authority.read).user(user).build(),
//                                AuthAuthorityJson.builder().authority(Authority.write).user(user).build()
//                        ));
        return user;
    }


//    public static AuthUserJson generateAuthUser() {
//        return AuthUserJson.builder()
//                .username(generateUsername())
//                .password(generatePassword())
//                .enabled(true)
//                .accountNonExpired(true)
//                .accountNonLocked(true)
//                .credentialsNonExpired(true)
//                .authorities(
//                        List.of(
//                                AuthAuthorityJson.builder().authority(Authority.read).build(),
//                                AuthAuthorityJson.builder().authority(Authority.write).build()
//                        ))
//                .build();
//    }

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