package guru.qa.niffler.model;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StaticUser {

    private String username;
    private String password;
    private Boolean empty;

}
