package ming.test.xface.service;

import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.pojo.Blog;

/**
 * @author XiaoMing
 * @description 针对表【blog(帖子表)】的数据库操作Service
 * @createDate 2024-06-05 20:48:03
 */
public interface BlogService {

    Result createBlog(Blog blog);

    Result locationList() throws Exception;

    Result typeList();
}
