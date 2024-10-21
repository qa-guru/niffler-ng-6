package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.auth.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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