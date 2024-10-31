package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.db.impl.springJdbc.SpendDbClientSpringJdbc;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class SpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final SpendClient spendClient = new SpendDbClientSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(
                        parameter -> {

                            var parameterName = parameter.getName();
                            var userAnno = parameter.getAnnotation(CreateNewUser.class);

                            if (userAnno.spendings().length > 0) {

                                @SuppressWarnings("unchecked")
                                Map<String, UserJson> usersMap = (Map<String, UserJson>) context
                                        .getStore(CreateNewUserExtension.NAMESPACE)
                                        .get(context.getUniqueId());
                                UserJson user = usersMap.get(parameterName);

                                List<SpendJson> spendings = new ArrayList<>();
                                Arrays.stream(userAnno.spendings())
                                        .forEach(spendAnno -> {

                                            SpendJson spend = new SpendMapper().updateFromAnno(
                                                    SpendUtils.generateForUser(user.getUsername()),
                                                    spendAnno
                                            );

                                            spend.setCategory(
                                                    spendClient.findCategoryByUsernameAndName(
                                                            user.getUsername(),
                                                            spend.getCategory().getName()
                                                    ).orElse(
                                                            spendClient.createCategory(spend.getCategory()))
                                            );

                                            spend = spendClient.create(spend);
                                            spendings.add(spend);

                                        });

                                usersMap.put(
                                        parameterName,
                                        user.setTestData(
                                                user.getTestData().setSpendings(spendings)));

                                context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);

                                log.info("Created new spendings for user = [{}]: {}",
                                        user.getUsername(),
                                        user.getTestData().getSpendings());
                            }

                        }

                );

    }

}