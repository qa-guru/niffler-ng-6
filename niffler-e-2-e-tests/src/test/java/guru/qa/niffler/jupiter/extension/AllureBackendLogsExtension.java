package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

        List<Path> logFiles = List.of(
                Path.of("./logs/niffler-auth/app.log"),
                Path.of("./logs/niffler-currency/app.log"),
                Path.of("./logs/niffler-userdata/app.log"),
                Path.of("./logs/niffler-gateway/app.log")
        );

        for (Path logFile : logFiles) {
                Allure.addAttachment(
                        logFile.getFileName().toString(),
                        "text/plain",
                        Files.newInputStream(logFile),
                        ".log"
                );
        }

        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }
}