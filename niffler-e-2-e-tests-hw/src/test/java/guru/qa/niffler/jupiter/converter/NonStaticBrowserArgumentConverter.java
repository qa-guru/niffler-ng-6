package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.enums.NonStaticBrowserType;
import guru.qa.niffler.utils.NonStaticSelenideUtils;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class NonStaticBrowserArgumentConverter implements ArgumentConverter {

    @Override
    public SelenideDriver convert(Object source, ParameterContext context) throws ArgumentConversionException {

        if (!(source instanceof NonStaticBrowserType)) {
            throw new ArgumentConversionException("Expected provided values type = [%s], actual = [%s] "
                    .formatted(NonStaticBrowserType.class.getSimpleName(), source.getClass().getSimpleName()));
        }

        var config = switch ((NonStaticBrowserType) source) {
            case chrome -> NonStaticSelenideUtils.chromeConfig();
            case firefox -> NonStaticSelenideUtils.firefoxConfig();
            case opera -> NonStaticSelenideUtils.operaConfig();
        };

        return new SelenideDriver(config);

    }

}
