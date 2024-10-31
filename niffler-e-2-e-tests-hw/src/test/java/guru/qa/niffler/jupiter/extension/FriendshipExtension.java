package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.db.impl.springJdbc.UsersDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class FriendshipExtension implements BeforeEachCallback {

    private final UsersClient usersClient = new UsersDbClientSpringJdbc();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(
                        parameter -> {

                            var parameterName = parameter.getName();
                            var userAnno = parameter.getAnnotation(CreateNewUser.class);

                            @SuppressWarnings("unchecked")
                            Map<String, UserJson> usersMap = (Map<String, UserJson>) context
                                    .getStore(NAMESPACE)
                                    .get(context.getUniqueId());
                            UserJson user = usersMap.get(parameterName);

                            TestData testData = TestData.builder()
                                    .categories(user.getTestData().getCategories())
                                    .spendings(user.getTestData().getSpendings())
                                    .build();

                            if (userAnno.incomeInvitations() > 0) {
                                testData.setIncomeInvitations(usersClient
                                        .getIncomeInvitationFromNewUsers(user, userAnno.incomeInvitations()));
                            }

                            if (userAnno.outcomeInvitations() > 0) {
                                testData.setOutcomeInvitations(usersClient
                                        .sendOutcomeInvitationToNewUsers(user, userAnno.incomeInvitations()));
                            }

                            if (userAnno.friends() > 0) {
                                testData.setFriends(usersClient
                                        .addNewFriends(user, userAnno.incomeInvitations()));
                            }

                            usersMap.put(parameterName, user.setTestData(testData));
                            context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);

                            log.info("Created invitations for user = [{}]:\nIncome invitations: {}\nOutcome invitations: {};\nFriends: {}", user.getUsername(), user.getTestData().getIncomeInvitations(), user.getTestData().getOutcomeInvitations(), user.getTestData().getFriends());

                        });
    }

}
