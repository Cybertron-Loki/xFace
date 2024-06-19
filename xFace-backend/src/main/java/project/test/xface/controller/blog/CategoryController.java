package project.test.xface.controller.blog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.test.xface.entity.dto.Result;
import project.test.xface.service.CategoryService;

@RestController
@RequestMapping("/Category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/checkCategory")
    private Result checkCategory(){
        return categoryService.checkCategory();
    }

}
