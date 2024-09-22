package guru.qa.nifler.jupiter;

import guru.qa.nifler.api.SpendApiClient;
import guru.qa.nifler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class SpendingResolverExtension implements ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingResolverExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext
        .getParameter()
        .getType()
        .isAssignableFrom(SpendJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext
        .getStore(CreateSpendingExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(), SpendJson.class);
  }
}