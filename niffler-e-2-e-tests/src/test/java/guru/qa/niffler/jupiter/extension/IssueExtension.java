package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {
    final GhApiClient ghApiClient = new GhApiClient();

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), DisabledByIssue.class)
                .or(() -> AnnotationSupport.findAnnotation(
                        context.getRequiredTestClass(),
                        DisabledByIssue.class,
                        SearchOption.INCLUDE_ENCLOSING_CLASSES

                ))
                .map(
                        findByAnno -> "open".equals(ghApiClient.issue(findByAnno.value()))
                                      ? ConditionEvaluationResult.disabled("Disabled by issue : " + findByAnno.value())
                                      : ConditionEvaluationResult.enabled("Issue closed")
                ).orElseGet(
                        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
                );
    }
}
