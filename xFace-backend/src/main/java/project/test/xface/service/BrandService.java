package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.BlogType;
import project.test.xface.entity.pojo.Brand;

/**
 * @author XiaoMing
 * @description 针对表【brand(商家/品牌表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface BrandService {

    Result checkBrands();

    Result checkById(Long id);

    Result addBrand(Brand brand);

    Result deleteBrand(Long id);

    Result updateBrand(Brand brand);

    Result checkBrandsByCategory(BlogType blogType);

    Result checkByName(String name);
}
