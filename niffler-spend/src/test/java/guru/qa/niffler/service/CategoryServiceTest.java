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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Test
    void categoryNotFoundExceptionShouldBeThrown(@Mock CategoryRepository categoryRepository) {
        final String username = "not_found";
        final UUID id = UUID.randomUUID();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
                .thenReturn(Optional.empty());

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                "",
                username,
                true
        );

        CategoryNotFoundException ex = Assertions.assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
                "Can`t find category by id: '" + id + "'",
                ex.getMessage()
        );
    }

    @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
    @ParameterizedTest
    void categoryNameArchivedShouldBeDenied(String catName, @Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
                .thenReturn(Optional.of(
                        cat
                ));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                catName,
                username,
                true
        );

        InvalidCategoryNameException ex = Assertions.assertThrows(
                InvalidCategoryNameException.class,
                () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
                "Can`t add category with name: '" + catName + "'",
                ex.getMessage()
        );
    }

    @Test
    void onlyTwoFieldsShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();
        cat.setId(id);
        cat.setUsername(username);
        cat.setName("Магазины");
        cat.setArchived(false);

        when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
                .thenReturn(Optional.of(
                        cat
                ));
        when(categoryRepository.save(any(CategoryEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                "Бары",
                username,
                true
        );

        categoryService.update(categoryJson);
        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(argumentCaptor.capture());
        assertEquals("Бары", argumentCaptor.getValue().getName());
        assertEquals("duck", argumentCaptor.getValue().getUsername());
        assertTrue(argumentCaptor.getValue().isArchived());
        assertEquals(id, argumentCaptor.getValue().getId());
    }

    @Test
    void getAllCategoriesShouldFilterResultsByExcludeArchived(@Mock CategoryRepository categoryRepository) {

        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity activeCategory = new CategoryEntity();
        activeCategory.setId(id);
        activeCategory.setUsername(username);
        activeCategory.setName("Active");
        activeCategory.setArchived(false);

        final CategoryEntity archivedCategory = new CategoryEntity();
        archivedCategory.setId(UUID.randomUUID());
        archivedCategory.setUsername(username);
        archivedCategory.setName("Archived");
        archivedCategory.setArchived(true);

        when(categoryRepository.findAllByUsernameOrderByName(username))
                .thenReturn(
                        List.of(
                                activeCategory,
                                archivedCategory
                        )
                );

        CategoryService categoryService = new CategoryService(categoryRepository);

        List<CategoryJson> categories = categoryService.getAllCategories(username, true);
        assertEquals(1, categories.size());
        assertEquals("Active", categories.getFirst().name());
    }

    @Test
    void updateShouldThrowExceptionIfMaxCategoriesSizeIsExceeded(@Mock CategoryRepository categoryRepository) {
        final String username = "test_user";
        final long MAX_CATEGORIES_SIZE = 7;
        CategoryJson category = new CategoryJson(
                UUID.randomUUID(),
                "category",
                username,
                false
        );

        final CategoryEntity ce = new CategoryEntity();
        ce.setArchived(true);

        when(categoryRepository.findByUsernameAndId(eq(username), eq(category.id())))
                .thenReturn(Optional.of(ce));

        when(categoryRepository.countByUsernameAndArchived(eq(username), eq(false)))
                .thenReturn(MAX_CATEGORIES_SIZE + 1);

        CategoryService categoryService = new CategoryService(categoryRepository);
        TooManyCategoriesException ex = assertThrows(TooManyCategoriesException.class,
                () -> categoryService.update(category));

        assertEquals("Can`t unarchive category for user: '" + category.username() + "'",
                ex.getMessage());

        verify(categoryRepository, never()).save(any(CategoryEntity.class));
    }

    @Test
    void shouldSaveCategorySuccessfully(@Mock CategoryRepository categoryRepository) {
        String username = "test_user";
        String categoryName = "Shopping";
        CategoryJson category = new CategoryJson(
                UUID.randomUUID(),
                categoryName,
                username,
                false
        );

        when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(5L);
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryEntity savedCategory = categoryService.save(category);

        assertNotNull(savedCategory);
        assertEquals(categoryName, savedCategory.getName());
        assertEquals(username, savedCategory.getUsername());
        assertFalse(savedCategory.isArchived());

        verify(categoryRepository).countByUsernameAndArchived(username, false);
        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
    @ParameterizedTest
    void shouldThrowExceptionForArchivedCategoryName(String catName, @Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                catName,
                username,
                true
        );

        InvalidCategoryNameException ex = Assertions.assertThrows(
                InvalidCategoryNameException.class,
                () -> categoryService.save(categoryJson)
        );
        Assertions.assertEquals(
                "Can`t add category with name: '" + catName + "'",
                ex.getMessage()
        );
    }

    @Test
    void saveShouldThrowExceptionIfMaxCategoriesSizeIsExceeded(@Mock CategoryRepository categoryRepository) {
        final String username = "test_user";
        final long MAX_CATEGORIES_SIZE = 7;
        CategoryJson category = new CategoryJson(
                UUID.randomUUID(),
                "category",
                username,
                false
        );

        when(categoryRepository.countByUsernameAndArchived(eq(username), eq(false)))
                .thenReturn(MAX_CATEGORIES_SIZE + 1);

        CategoryService categoryService = new CategoryService(categoryRepository);
        TooManyCategoriesException ex = assertThrows(TooManyCategoriesException.class,
                () -> categoryService.save(category));

        assertEquals("Can`t add over than 8 categories for user: '" + username + "'",
                ex.getMessage());

        verify(categoryRepository, never()).save(any(CategoryEntity.class));
    }
}