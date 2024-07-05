package project.test.xface.service.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.dto.UserDTO;
import project.test.xface.entity.pojo.Blog;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.test.xface.entity.pojo.Collection;
import project.test.xface.mapper.BlogMapper;
import project.test.xface.mapper.CollectionMapper;
import project.test.xface.service.CollectionService;
import project.test.xface.utils.RedisWorker;
import project.test.xface.utils.UserHolder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;
    @Resource
    private RedisWorker redisWorker;

    @Override
    public Result addCollection(Collection collection) {
        Integer count=collectionMapper.count(collection.getUserId());
        if(count>20) {return Result.fail("max count");}
        Long id = redisWorker.nextId("collections:");
        collection.setId(id);
        collectionMapper.add(collection);
        return Result.success();
    }

    @Override
    public Result checkByUserId(Long userId) {
        List<Collection> collections=collectionMapper.checkByUserId(userId);
        for (Collection collection : collections) {
            List<Blog> blogs=collectionMapper.selectBlogs(collection);
            collection.setBlogList(blogs);
        }
        return Result.success(collections);
    }


    @Transactional
    @Override
    public Result deleteByIds(Long[] ids) {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getId();
        collectionMapper.deleteByIds(ids,userId);
        collectionMapper.deleteReflection(ids,userId);
        //userid双重保证不会误删其他人的
        return Result.success();
    }

    @Override
    public Result modifyById(Collection collection) {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getId();
        collectionMapper.modify(collection);
        return Result.success();
    }

    @Override
    public Result export(Long id, HttpServletResponse response) {
        //先把收藏夹下的blog查出来
        Collection collection=new Collection();
        collection.setId(id);
        collection.setUserId(UserHolder.getUser().getId());
        List<Blog> blogs = collectionMapper.selectBlogs(collection);
        InputStream in=this.getClass().getClassLoader().getResourceAsStream("template/collection blogs template.xlsx");
        try{
            XSSFWorkbook sheets=new XSSFWorkbook(in);
            XSSFSheet sheet=sheets.getSheet("Sheet1");
            sheet.getRow(2).getCell(2).setCellValue(LocalDateTime.now());  //时间
            sheet.getRow(3).getCell(2).setCellValue(collection.getName());  //名字


            XSSFRow row;
           for(int i=1;i<blogs.size();i++){
               row=sheet.getRow(4+i);
               Blog blog = blogs.get(i - 1);
               row.getCell(1).setCellValue(blog.getTitle());
               row.getCell(3).setCellValue(blog.getContent());
               //判断是shop还是brand
               if(blog.getBrandId()==null) {
               row.getCell(5).setCellValue(blog.getShopId());
               row.getCell(7).setCellValue(blog.getLocation());
           }
               else {
                row.getCell(5).setCellValue(blog.getBrandId());
               }
               row.getCell(9).setCellValue(blog.getType());
           }


            ServletOutputStream out= response.getOutputStream();
            sheets.write(out);
            out.close();
            sheets.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

}
