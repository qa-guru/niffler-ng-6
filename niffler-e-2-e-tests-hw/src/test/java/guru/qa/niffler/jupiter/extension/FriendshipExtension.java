package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.api.impl.UsersApiClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@ParametersAreNonnullByDefault
public class FriendshipExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final UsersClient usersClient = new UsersApiClientImpl();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class) &&
                        parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(
                        parameter -> {

                            var parameterName = parameter.getName();
                            var userAnno = parameter.getAnnotation(CreateNewUser.class);

                            var user = CreateNewUserExtension.getUserByTestParamName(parameterName);

                            TestData testData = TestData.builder()
                                    .categories(user.getTestData().getCategories())
                                    .spendings(user.getTestData().getSpendings())
                                    .build();

                            if (userAnno.incomeInvitations() > 0) {
                                testData.setIncomeInvitations(
                                        usersClient.getIncomeInvitationFromNewUsers(
                                                user,
                                                userAnno.incomeInvitations()));
                            }

                            if (userAnno.outcomeInvitations() > 0) {
                                testData.setOutcomeInvitations(usersClient
                                        .sendOutcomeInvitationToNewUsers(user, userAnno.incomeInvitations()));
                            }

                            if (userAnno.friends() > 0) {
                                testData.setFriends(usersClient
                                        .addNewFriends(user, userAnno.incomeInvitations()));
                            }

                            CreateNewUserExtension.setUserByTestParamName(parameterName, user.setTestData(testData));

                            log.info("Created invitations for user = [{}]:\nIncome invitations: {}\nOutcome invitations: {};\nFriends: {}", user.getUsername(), user.getTestData().getIncomeInvitations(), user.getTestData().getOutcomeInvitations(), user.getTestData().getFriends());

                        });
    }

}
