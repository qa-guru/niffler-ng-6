package guru.qa.niffler.controller;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/categories")
public class CategoriesController {

    private final CategoryService categoryService;

    @Autowired
    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public List<CategoryJson> getCategories(@RequestParam String username,
                                            @RequestParam(required = false, defaultValue = "false") boolean excludeArchived) {
        return categoryService.getAllCategories(username, excludeArchived);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryJson addCategory(@RequestBody CategoryJson category) {
        return categoryService.addCategory(category);
    }

    @PatchMapping("/update")
    public CategoryJson updateCategory(@RequestBody CategoryJson category) {
        return categoryService.update(category);
    }
}
