package project.test.xface.service;

import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Collection;

import javax.servlet.http.HttpServletResponse;

public interface CollectionService {
    Result addCollection(Collection collection);

    Result checkByUserId(Long userId);

    Result deleteByIds(Long[] ids);

    Result modifyById(Collection collection);

    Result export(Long id, HttpServletResponse response);
}
