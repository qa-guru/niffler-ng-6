package guru.qa.niffler.model;

import guru.qa.niffler.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StaticUser {

    private String username;
    private String password;
    private UserType userType;
    private List<String> incomeRequestFromUsersList;
    private List<String> outcomeRequestToUsersList;
    private List<String> friendsList;

}
