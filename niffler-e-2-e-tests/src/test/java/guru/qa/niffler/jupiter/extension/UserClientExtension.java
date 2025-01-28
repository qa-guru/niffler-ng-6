package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.db.UserDbClient;
import guru.qa.niffler.service.rest.UserRestClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class UserClientExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
         for (Field field : testInstance.getClass().getDeclaredFields()){
             if(field.getType().isAssignableFrom(UserClient.class)) {
                 field.setAccessible(true);
                 if ("db".equals(System.getProperty("user.client"))){
                     field.set(testInstance, new UserDbClient());
                 } else {
                     field.set(testInstance, new UserRestClient());
                 }
             }
         }
    }
}
