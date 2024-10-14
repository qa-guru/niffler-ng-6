package guru.qa.niffler.data.entity.auth;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AuthUserEntity {

    private UUID id;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String password;

}