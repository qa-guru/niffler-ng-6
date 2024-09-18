package guru.qa.niffler.test.web;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.*;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserQueueExtension.class)
public class UserQueueTest {

    @Test
    void testWithEmptyUser(@UserType(empty = true) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);
    }

    @Test
    void testWithNotEmptyUser(@UserType(empty = false) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);
    }
}
