package project.test.xface.service;


import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Shop;


public interface ShopService {

//    Result checkShops();

    Result checkById(Long id);

    Result addShop(Shop shop);

    Result deleteShop(Shop shop);

    Result updateShop(Shop shop);

    Result checkShopsByCategory(Integer blogTypeId, Integer current, Double x, Double y);

    Result checkByName(String name);


}
