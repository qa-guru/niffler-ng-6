package guru.qa.niffler.data.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import org.apache.commons.lang3.StringUtils;

public class AllureAppender extends StdoutLogger {
    private final String templateName = "sql-attachment.ftl";
    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        if(StringUtils.isNoneEmpty(sql)){
            final SqlAttachmentData attachmentData = new SqlAttachmentData(
                    sql.split("\\s")[0].toUpperCase()+"quere to: "+ StringUtils.substringBetween(url,"5432/", "?"),
                    SqlFormatter.of(Dialect.PostgreSql).format(sql)
            );
            attachmentProcessor.addAttachment(
                    attachmentData,
                    new FreemarkerAttachmentRenderer(templateName)
            );
        }
    }
}
