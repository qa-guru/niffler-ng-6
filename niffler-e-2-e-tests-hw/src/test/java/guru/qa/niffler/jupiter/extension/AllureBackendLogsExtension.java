package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

    public static final String CASE_NAME = "Niffler backend logs";

    private static void addLogsToAllure(AllureLifecycle allureLifecycle) throws IOException {
        addLogToAllure(
                allureLifecycle,
                "Niffler-auth log",
                Path.of("./logs/niffler-auth/app.log"));

        addLogToAllure(
                allureLifecycle,
                "Niffler-currency log",
                Path.of("./logs/niffler-currency/app.log"));

        addLogToAllure(
                allureLifecycle,
                "Niffler-gateway log",
                Path.of("./logs/niffler-gateway/app.log"));

        addLogToAllure(allureLifecycle,
                "Niffler-spend log",
                Path.of("./logs/niffler-spend/app.log"));

        addLogToAllure(
                allureLifecycle,
                "Niffler-userdata log",
                Path.of("./logs/niffler-userdata/app.log"));
    }

    private static void addLogToAllure(AllureLifecycle allureLifecycle, String name, Path pathToLog) throws IOException {
        allureLifecycle.addAttachment(
                name,
                "text/html",
                ".log",
                Files.newInputStream(pathToLog)
        );
    }

    @SneakyThrows
    @Override
    public void afterSuite() {

        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();

        List<Label> labels = List.of(
                new Label().setName("story").setValue("Niffler backend logs"));

        allureLifecycle.scheduleTestCase(
                new TestResult()
                        .setUuid(caseId)
                        .setName(CASE_NAME)
                        .setLabels(labels));

        allureLifecycle.startTestCase(caseId);

        addLogsToAllure(allureLifecycle);

        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

}
