package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jaxb.userdata.FriendState;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserJson implements Serializable {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    private transient String passwordConfirmation;

    @JsonProperty("currency")
    private CurrencyValues currency;

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("photoSmall")
    private String photoSmall;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("friendState")
    private FriendState friendState;

    private transient TestData testData;

    public UserJson addTestData(TestData testData) {
        return UserJson.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .passwordConfirmation(this.passwordConfirmation)
                .currency(this.currency)
                .firstName(this.firstName)
                .surname(this.surname)
                .photo(this.photo)
                .photoSmall(this.photoSmall)
                .fullName(this.fullName)
                .testData(testData)
                .build();
    }

}