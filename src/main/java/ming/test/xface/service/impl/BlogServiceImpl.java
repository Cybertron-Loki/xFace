package ming.test.xface.service.impl;

import ming.test.xface.dao.BlogMapper;
import ming.test.xface.enity.dto.Result;
import ming.test.xface.enity.dto.UserDTO;
import ming.test.xface.enity.pojo.Blog;
import ming.test.xface.enity.pojo.BlogType;
import ming.test.xface.service.BlogService;
import ming.test.xface.utils.BaiduMapUtils;
import ming.test.xface.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ming.test.xface.utils.BaiduMapUtils.AK;
import static ming.test.xface.utils.BaiduMapUtils.URL;

/**
 * @author XiaoMing
 * @description 针对表【blog(帖子表)】的数据库操作Service实现
 * @createDate 2024-06-05 20:48:03
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Result createBlog(Blog blog) {
        UserDTO userDTO = UserHolder.getUser();
        Integer userid = userDTO.getId();
        blog.setUserid(userid);
        blogMapper.addBlog(blog);
           return Result.success();
    }

    @Override
    public Result locationList() throws Exception {

//        blogMapper.locationList();
        /**
         * 选择了ak或使用IP白名单校验：
         */
            BaiduMapUtils snCal = new BaiduMapUtils();
            Map params = new LinkedHashMap<String, String>();
            params.put("keyword", "全国");
            params.put("sub_admin", "2");
            params.put("ak", AK);
            snCal.requestGetAK(URL, params);
        return Result.success(URL);
    }

    @Override
    public Result typeList() {
        List<BlogType> blogTypes = blogMapper.listType();
        return Result.success(blogTypes);
    }
}




