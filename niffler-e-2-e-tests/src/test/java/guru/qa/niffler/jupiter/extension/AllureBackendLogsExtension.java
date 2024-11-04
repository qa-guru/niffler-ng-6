package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {
    public static final String caseName = "Niffler backend logs";

    @SneakyThrows
    @Override
    public void afterSuite() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        allureLifecycle.startTestCase(caseId);
        addLogAttachment("Niffler-auth log", "./logs/niffler-auth/app.log");
        addLogAttachment("Niffler-currency log", "./logs/niffler-currency/app.log");
        addLogAttachment("Niffler-gateway log", "./logs/niffler-gateway/app.log");
        addLogAttachment("Niffler-spend log", "./logs/niffler-spend/app.log");
        addLogAttachment("Niffler-userdata log", "./logs/niffler-userdata/app.log");
        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

    @SneakyThrows
    private void addLogAttachment(String attachmentName, String logPath) {
        Allure.getLifecycle().addAttachment(
                attachmentName,
                "text/html",
                ".log",
                Files.newInputStream(Path.of(logPath))
        );
    }
}