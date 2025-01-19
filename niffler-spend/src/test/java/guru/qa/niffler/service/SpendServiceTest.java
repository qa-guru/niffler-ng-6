package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class SpendServiceTest {

    @Test
    void getSpendForUserShouldThrowExceptionInCaseThatIdIsIncorrectFormat(
            @Mock SpendRepository spendRepository,
            @Mock CategoryService categoryService){
        final String incorrectId = "incorrectId";
        SpendService spendService = new SpendService(spendRepository, categoryService);
        SpendNotFoundException ex = Assertions.assertThrows(
                SpendNotFoundException.class,
                () -> spendService.getSpendForUser(
                        incorrectId,
                        "Ignat"
                )
        );
        Assertions.assertEquals(
                "Can`t find spend by given id: " + incorrectId,
                ex.getMessage());
    }


    @Test
    void getSpendForUserShouldThrowExceptionInCaseThatSpendNotFoundInDb(
            @Mock SpendRepository spendRepository,
            @Mock CategoryService categoryService){
        final UUID correctId = UUID.randomUUID();
        final String correctUsername = "Ignat";
        SpendService spendService = new SpendService(spendRepository, categoryService);
        Mockito.when(spendRepository.findByIdAndUsername(eq(correctId),eq(correctUsername)))
                .thenReturn(Optional.empty());
        SpendNotFoundException ex = Assertions.assertThrows(
                SpendNotFoundException.class,
                () -> spendService.getSpendForUser(
                        correctId.toString(),
                        correctUsername
                )
        );
        Assertions.assertEquals(
                "Can`t find spend by given id: " + correctId,
                ex.getMessage());
    }

    @Test
    void getSpendForUserShouldReturnCorrectJsonObject(
            @Mock SpendRepository spendRepository,
            @Mock CategoryService categoryService){
        final UUID correctId = UUID.randomUUID();
        final String correctUsername = "Ignat";
        final SpendEntity spend = new SpendEntity();
        final CategoryEntity category = new CategoryEntity();
        spend.setId(correctId);
        spend.setUsername(correctUsername);
        spend.setCurrency(CurrencyValues.RUB);
        spend.setAmount(100.15);
        spend.setDescription("Description spend");
        spend.setSpendDate(new Date(0));
        category.setUsername(correctUsername);
        category.setName("Description category");
        category.setArchived(false);
        category.setId(UUID.randomUUID());
        spend.setCategory(category);
        SpendService spendService = new SpendService(spendRepository, categoryService);
        Mockito.when(spendRepository.findByIdAndUsername(eq(correctId),eq(correctUsername)))
                .thenReturn(Optional.of(spend));
        final SpendJson result = spendService.getSpendForUser(correctId.toString(), correctUsername);
        Mockito.verify(spendRepository, Mockito.times(1))
                .findByIdAndUsername(eq(correctId),eq(correctUsername));
        Assertions.assertEquals(
                "Description spend",
                result.description());
    }

}