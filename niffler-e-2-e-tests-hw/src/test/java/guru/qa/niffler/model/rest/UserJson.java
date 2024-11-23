package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.enums.FriendState;
import lombok.*;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
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
    FriendState friendState;

    @JsonIgnore
    @Builder.Default
    private transient TestData testData = new TestData();

    public UserJson addTestData(@Nonnull TestData testData) {

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