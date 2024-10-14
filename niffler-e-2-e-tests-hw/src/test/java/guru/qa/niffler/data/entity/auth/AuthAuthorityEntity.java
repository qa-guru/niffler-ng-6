package guru.qa.niffler.data.entity.auth;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class AuthAuthorityEntity {

    private UUID id;
    private UUID userId;
    private Authority authority;

}