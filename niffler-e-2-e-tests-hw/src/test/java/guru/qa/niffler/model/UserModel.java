package guru.qa.niffler.model;

import lombok.*;
import lombok.experimental.Accessors;

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
    private String avatar;

}

