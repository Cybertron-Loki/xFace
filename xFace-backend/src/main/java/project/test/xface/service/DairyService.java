package project.test.xface.service;


import org.springframework.stereotype.Service;
import project.test.xface.entity.dto.Result;
import project.test.xface.entity.pojo.Diary;

@Service
public interface DairyService {
    Result createDiary(Diary diary);

    Result checkDairies(Integer userId);

    Result checkDairy(Integer dairyId);
}
