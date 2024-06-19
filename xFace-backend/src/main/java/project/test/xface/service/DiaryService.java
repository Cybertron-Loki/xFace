package project.test.xface.service;


import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Comment;
import project.test.xface.entity.pojo.Diary;

@Service
public interface DiaryService {
    Result createDiary(Diary diary);

    Result checkDiaries(Integer userId);

    Result checkDiary(Integer dairyId);

    Result createComment(Comment comment);

    Result deleteDiary(Integer id);
}
