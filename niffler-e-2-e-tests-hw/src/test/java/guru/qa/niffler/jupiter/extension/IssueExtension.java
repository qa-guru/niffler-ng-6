package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GitHubApiClientRetrofit;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;


public class IssueExtension implements ExecutionCondition {

    private static final GitHubApiClientRetrofit gitHubApiClient = new GitHubApiClientRetrofit();

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
                byIssue -> "open".equals(gitHubApiClient.getIssueState(byIssue.issueId()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.issueId())
                        : ConditionEvaluationResult.enabled("Issue closed")
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
        );
    }
}
