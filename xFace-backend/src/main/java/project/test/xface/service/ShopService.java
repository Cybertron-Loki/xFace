package project.test.xface.service;


import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Shop;


public interface ShopService {

    Result checkShops();

    Result checkById(Long id);

    Result addShop(Shop shop);

    Result deleteShop(Long id);

    Result updateShop(Shop shop);

    Result checkShopsByCategory(BlogType blogType);

    Result checkByName(String name);
}
