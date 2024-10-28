package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import lombok.NonNull;

import java.util.Random;

public class CategoryMapper {

    public CategoryEntity toEntity(@NonNull CategoryJson source) {
        return CategoryEntity.builder()
                .id(source.getId())
                .username(source.getUsername())
                .name(source.getName())
                .archived(source.isArchived())
                .build();
    }

    public CategoryJson toDto(@NonNull CategoryEntity category) {
        return CategoryJson.builder()
                .id(category.getId())
                .username(category.getUsername())
                .name(category.getName())
                .archived(category.isArchived())
                .build();
    }

    public CategoryJson updateDtoFromAnno(@NonNull CategoryJson category, Category anno) {

        return CategoryJson.builder()
                .id(category.getId())
                .username(category.getUsername())
                .name(
                        anno.name().isEmpty()
                                ? category.getName()
                                : anno.name())
                .archived(
                        anno.isArchived() || (anno.generateIsArchived() && new Random().nextBoolean()))
                .build();

    }
}