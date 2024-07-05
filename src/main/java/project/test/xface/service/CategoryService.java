package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;

public interface CategoryService {
    Result checkCategory();

    Result addCategory(String blogTypeName);

    Result deleteCategory(Integer[] ids);

    Result updateCategory(BlogType blogType);
}
