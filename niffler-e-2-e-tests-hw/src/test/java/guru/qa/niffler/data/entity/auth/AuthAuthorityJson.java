package guru.qa.niffler.data.entity.auth;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AuthAuthorityJson {

    private UUID id;
    private UUID userId;
    private Authority authority;

}