package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.db.impl.springJdbc.SpendDbClientSpringJdbc;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private static final SpendMapper spendMapper = new SpendMapper();
    private final CategoryDao categoryDbClient = new CategoryDaoSpringJdbc();
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
                                            spendings.add(
                                                    spendClient.create(
                                                            spendMapper.updateFromAnno(
                                                                    SpendUtils.generateForUser(user.getUsername()),
                                                                    spendAnno
                                                            )
                                                    ));
                                        });

                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        usersMap.put(parameterName, user.setTestData(new TestData().setSpendings(spendings)))
                                );

                                log.info("Created new spendings for user = [{}]: {}", user.getUsername(), user.getTestData().getSpendings());
                            }

                        }

                );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }

}