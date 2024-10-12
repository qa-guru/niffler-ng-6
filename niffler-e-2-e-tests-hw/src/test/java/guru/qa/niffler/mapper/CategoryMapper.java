package guru.qa.niffler.mapper;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

import java.util.Random;

public class CategoryMapper {

    public CategoryJson updateFromAnno(CategoryJson category, Category anno) {

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