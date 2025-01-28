package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.InvalidCategoryNameException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Test
    void getAllCategoriesExcludeArchived(@Mock CategoryRepository categoryRepository) {

        final String username = "Ignat";
        final boolean excludeArchived = true;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final List<CategoryEntity> categoryList = new ArrayList<>();
        final CategoryEntity categoryArchived = new CategoryEntity();
        categoryArchived.setId(UUID.randomUUID());
        categoryArchived.setName("Category is archived");
        categoryArchived.setUsername(username);
        categoryArchived.setArchived(true);
        categoryList.add(categoryArchived);
        final CategoryEntity categoryNotArchived = new CategoryEntity();
        categoryNotArchived.setId(UUID.randomUUID());
        categoryNotArchived.setName("Category is not archived");
        categoryNotArchived.setUsername(username);
        categoryNotArchived.setArchived(false);
        categoryList.add(categoryNotArchived);
        Mockito.when(categoryRepository.findAllByUsernameOrderByName(eq(username)))
                .thenReturn(categoryList);
        List<CategoryJson> categoryJsonListResult = categoryService.getAllCategories(username, excludeArchived);

        Assertions.assertEquals(categoryJsonListResult.getFirst(), CategoryJson.fromEntity(categoryNotArchived));
    }

    @Test
    void getAllCategoriesIncludeArchived(@Mock CategoryRepository categoryRepository) {

        final String username = "Ignat";
        final boolean excludeArchived = false;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final List<CategoryEntity> categoryList = new ArrayList<>();
        final CategoryEntity categoryArchived = new CategoryEntity();
        categoryArchived.setId(UUID.randomUUID());
        categoryArchived.setName("Category is archived");
        categoryArchived.setUsername(username);
        categoryArchived.setArchived(true);
        categoryList.add(categoryArchived);
        final CategoryEntity categoryNotArchived = new CategoryEntity();
        categoryNotArchived.setId(UUID.randomUUID());
        categoryNotArchived.setName("Category is not archived");
        categoryNotArchived.setUsername(username);
        categoryNotArchived.setArchived(false);
        categoryList.add(categoryNotArchived);
        Mockito.when(categoryRepository.findAllByUsernameOrderByName(eq(username)))
                .thenReturn(categoryList);
        List<CategoryJson> categoryJsonListResult = categoryService.getAllCategories(username, excludeArchived);

        Assertions.assertEquals(categoryJsonListResult.getFirst(), CategoryJson.fromEntity(categoryArchived));
    }


    @Test
    void updateShouldCantFindCategory(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Move",
                "Ignat",
                false
        );

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryNotFoundException ex = Assertions.assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.update(categoryJson)
        );

        Assertions.assertEquals(
                ex.getMessage(),
                "Can`t find category by id: '" + categoryJson.id() + "'"
        );
    }

    @Test
    void updateShouldCantAddCategoryWithName(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Archived",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 7;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final CategoryEntity categoryArchived = new CategoryEntity();
        categoryArchived.setId(UUID.randomUUID());
        categoryArchived.setName("Archived");
        categoryArchived.setUsername("Ignat");
        categoryArchived.setArchived(false);

        Mockito.when(categoryRepository.findByUsernameAndId(eq(categoryJson.username()), eq(categoryJson.id())))
                .thenReturn(Optional.of(categoryArchived));

        InvalidCategoryNameException ex = Assertions.assertThrows(
                InvalidCategoryNameException.class,
                () -> categoryService.update(categoryJson)
        );

        Assertions.assertEquals(
                ex.getMessage(),
                "Can`t add category with name: '" + categoryJson.name() + "'"
        );
    }


    @Test
    void updateShouldMaxCategoriesSizeIsExceeded(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Move",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 8;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final CategoryEntity categoryNotArchived = new CategoryEntity();
        categoryNotArchived.setId(UUID.randomUUID());
        categoryNotArchived.setName("Move");
        categoryNotArchived.setUsername("Ignat");
        categoryNotArchived.setArchived(true);

        Mockito.when(categoryRepository.findByUsernameAndId(eq(categoryJson.username()), eq(categoryJson.id())))
                .thenReturn(Optional.of(categoryNotArchived));


        Mockito.when(categoryRepository.countByUsernameAndArchived(eq(categoryJson.username()), eq(false)))
                .thenReturn(maxCategoriesSize);

        TooManyCategoriesException ex = Assertions.assertThrows(
                TooManyCategoriesException.class,
                () -> categoryService.update(categoryJson)
        );

        Assertions.assertEquals(
                ex.getMessage(),
                "Can`t unarchive category for user: '" + categoryJson.username() + "'"
        );
    }

    @Test
    void updateCorrectSave(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Move",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 6;
        CategoryService categoryService = new CategoryService(categoryRepository);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.randomUUID());
        categoryEntity.setName("Move");
        categoryEntity.setUsername("Ignat");
        categoryEntity.setArchived(false);

        Mockito.when(categoryRepository.findByUsernameAndId(eq(categoryJson.username()), eq(categoryJson.id())))
                .thenReturn(Optional.of(categoryEntity));

        Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
                .thenReturn(categoryEntity);

        CategoryJson result = categoryService.update(categoryJson);

        Assertions.assertEquals(result.username(), categoryJson.username());
    }


    @Test
    void saveShouldCantAddCategoryWithName(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Archived",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 7;
        CategoryService categoryService = new CategoryService(categoryRepository);

        InvalidCategoryNameException ex = Assertions.assertThrows(
                InvalidCategoryNameException.class,
                () -> categoryService.save(categoryJson)
        );

        Assertions.assertEquals(
                ex.getMessage(),
                "Can`t add category with name: '" + categoryJson.name() + "'"
        );
    }


    @Test
    void saveShouldMaxCategoriesSizeIsExceeded(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Move",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 8;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.randomUUID());
        categoryEntity.setName("Move");
        categoryEntity.setUsername("Ignat");
        categoryEntity.setArchived(true);

        Mockito.when(categoryRepository.countByUsernameAndArchived(eq(categoryJson.username()), eq(false)))
                .thenReturn(maxCategoriesSize);

        TooManyCategoriesException ex = Assertions.assertThrows(
                TooManyCategoriesException.class,
                () -> categoryService.save(categoryJson)
        );

        Assertions.assertEquals(
                ex.getMessage(),
                "Can`t add over than 8 categories for user: '" + categoryJson.username() + "'"
        );
    }

    @Test
    void saveCorrectSave(@Mock CategoryRepository categoryRepository) {

        final CategoryJson categoryJson = new CategoryJson(
                UUID.randomUUID(),
                "Move",
                "Ignat",
                false
        );
        final long maxCategoriesSize = 6;
        CategoryService categoryService = new CategoryService(categoryRepository);
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(UUID.randomUUID());
        categoryEntity.setName("Move");
        categoryEntity.setUsername("Ignat");
        categoryEntity.setArchived(false);

        Mockito.when(categoryRepository.countByUsernameAndArchived(eq(categoryJson.username()), eq(false)))
                .thenReturn(maxCategoriesSize);

        Mockito.when(categoryRepository.save((any(CategoryEntity.class))))
                .thenReturn(categoryEntity);

        CategoryEntity result = categoryService.save(categoryJson);

        Assertions.assertEquals(categoryEntity.getName(), result.getName());

    }

}