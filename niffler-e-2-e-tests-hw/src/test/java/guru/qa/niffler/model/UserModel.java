package guru.qa.niffler.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserModel {

    private String username;

    private String password;

    private String passwordConfirmation;

    private String fullName;

    @Builder.Default
    private List<CategoryJson> categories = new ArrayList<>();

    @Builder.Default
    private List<SpendJson> spendings = new ArrayList<>();

    private String avatar;

}