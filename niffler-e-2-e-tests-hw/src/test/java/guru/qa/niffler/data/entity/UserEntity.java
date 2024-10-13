package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@Accessors(chain = true)
public class UserEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String fullName;
    private String firstName;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;

}
