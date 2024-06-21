package project.test.xface.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.test.xface.entity.pojo.Diary;
import project.test.xface.entity.pojo.DiaryType;

import java.util.List;

/**
 * 按照分类返回对应的日记们
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryTypeVO {
  private DiaryType diaryType;
  private List<Diary> diaries;
}
