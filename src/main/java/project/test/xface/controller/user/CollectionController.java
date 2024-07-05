package project.test.xface.controller.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Collection;
import project.test.xface.service.CollectionService;

import javax.servlet.http.HttpServletResponse;


/**
 * 收藏夹
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * 增
     */
    @PostMapping()
    private Result addCollection(@RequestBody Collection collection) {
        return collectionService.addCollection(collection);
    }
    /**
     * 根据userid 查 注：不分页，有数量限制
     */
    @GetMapping("/{userId}")
    private Result checkByUserId(@PathVariable Long userId){
        return collectionService.checkByUserId(userId);
    }
    /**
     * 删
     */
    @DeleteMapping("/{ids}")
    private Result deleteByIds(@PathVariable Long[] ids){
        return collectionService.deleteByIds(ids);
    }
    /**
     * 改
     */
    @PutMapping()
    private Result modifyById(@RequestBody Collection collection){
        return collectionService.modifyById(collection);
    }

    /**
     * 导出到excle
     */
    @GetMapping("/export/{collectionId}")
    private Result export(@PathVariable("collectionId") Long id, HttpServletResponse httpServletResponse){
        return collectionService.export(id,httpServletResponse);
    }


}
