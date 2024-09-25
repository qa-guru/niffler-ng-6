package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.spendings().length != 0) {
                        Spending spending = anno.spendings()[0];

                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        spending.category(),
                                        anno.username(),
                                        false
                                ),
                                CurrencyValues.RUB,
                                spending.amount(),
                                spending.description(),
                                anno.username()
                        );

                        SpendJson spendJson = spendDbClient.createSpend(spend);
                        System.out.println(spendJson);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendJson
                        );
                    }
                });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        var spend = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        if (spend == null) return;

        spendDbClient.deleteSpend(SpendEntity.fromJson(spend));
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
