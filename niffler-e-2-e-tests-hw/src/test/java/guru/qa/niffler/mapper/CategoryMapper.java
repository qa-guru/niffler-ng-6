package guru.qa.niffler.mapper;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

import java.util.Random;

public class CategoryMapper {

    public CategoryJson updateFromAnno(CategoryJson category, Category anno) {

        return new CategoryJson(
                category.id(),
                !anno.name().isEmpty()
                        ? anno.name()
                        : category.name(),
                anno.username().isEmpty()
                        ? category.username()
                        : anno.username(),
                anno.generateIsArchived()
                        ? new Random().nextBoolean()
                        : anno.isArchived()
        );

    }

}
