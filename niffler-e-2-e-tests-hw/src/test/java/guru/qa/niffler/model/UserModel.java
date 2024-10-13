package guru.qa.niffler.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserModel {

    private UUID id;

    private String username;

    private String password;

    private String passwordConfirmation;

    private CurrencyValues currency;

    private String firstName;

    private String surname;

    private byte[] photo;

    private byte[] photoSmall;

    private String fullName;

    @Builder.Default
    private List<CategoryJson> categories = new ArrayList<>();

    @Builder.Default
    private List<SpendJson> spendings = new ArrayList<>();

}