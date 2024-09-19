package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

import java.io.IOException;


public class IssueExtension implements ExecutionCondition {

  private static final GhApiClient ghApiClient = new GhApiClient();

  @SneakyThrows
  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    return AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        DisabledByIssue.class
    ).or(
        () -> AnnotationSupport.findAnnotation(
            context.getRequiredTestClass(),
            DisabledByIssue.class,
            SearchOption.INCLUDE_ENCLOSING_CLASSES
        )
    ).map(
        byIssue -> {
            try {
                return "open".equals(ghApiClient.issueState(byIssue.value()))
                    ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())
                    : ConditionEvaluationResult.enabled("Issue closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    ).orElseGet(
        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
    );
  }
}
