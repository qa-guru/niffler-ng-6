package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.service.impl.GithubApiClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;


public class IssueExtension implements ExecutionCondition {

  private static final GithubApiClient ghApiClient = new GithubApiClient();

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
        byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))
            ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())
            : ConditionEvaluationResult.enabled("Issue closed")
    ).orElseGet(
        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
    );
  }
}
