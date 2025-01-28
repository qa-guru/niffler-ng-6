package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.utils.SelenideConfigBrowser;
import guru.qa.niffler.utils.SelenideSelectDriver;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class ToDriverArgumentConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        SelenideConfigBrowser typeDriver = (SelenideConfigBrowser) source;
        return new SelenideSelectDriver().getDriver(typeDriver);
    }
}