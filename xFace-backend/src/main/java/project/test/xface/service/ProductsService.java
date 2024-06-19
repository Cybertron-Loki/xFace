package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Products;

public interface ProductsService {
    Result selectByBrand(Long id);

    Result selectByShop(Long id);

    Result addProducts(Products products);
}
