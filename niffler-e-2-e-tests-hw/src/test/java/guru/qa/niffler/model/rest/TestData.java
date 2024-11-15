package guru.qa.niffler.model.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TestData {

    @Builder.Default
    private List<CategoryJson> categories = new ArrayList<>();

    @Builder.Default
    private List<SpendJson> spendings = new ArrayList<>();

    @Builder.Default
    private List<UserJson> incomeInvitations = new ArrayList<>();

    @Builder.Default
    private List<UserJson> outcomeInvitations = new ArrayList<>();

    @Builder.Default
    private List<UserJson> friends = new ArrayList<>();

}
