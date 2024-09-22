package guru.qa.nifler.jupiter;

import guru.qa.nifler.api.SpendApiClient;
import guru.qa.nifler.model.SpendJson;
import guru.qa.nifler.model.submodel.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

  public static final Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spend.class)
        .ifPresent(
            anno -> {
              SpendJson spendJson = new SpendJson(
                  null,
                  new Date(),
                  new CategoryJson(null, anno.category(), anno.username(), false),
                  anno.currency(),
                  anno.amount(),
                  anno.description(),
                  anno.username()
              );
              final SpendJson createdSpend = spendApiClient.createSpend(spendJson);
              context.getStore(NAMESPACE)
                  .put(context.getUniqueId(), createdSpend);
            }
        );
  }
}