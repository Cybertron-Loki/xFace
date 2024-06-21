package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;

public interface ProductsService {
    Result selectByBrand(Long id);

    Result selectByShop(Long id);

    Result addProducts(Products products);

    Result selectByName(String name);

    Result selectBlogs(Long id, String[] range);

    Result deleteProducts(Long id, Long shopId, Long brandId);

    Result updateProducts(Products products);
}
