package ming.test.xface.enity.pojo;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Integer userId;
    private String city;
    private String hobby;
    private String userName;
    private String introduce;
    private DateTime birthDay;
    private Integer fans;
    private Integer followee;
    private char gender;
    private Integer age;
    private DateTime createTime;
    private DateTime updateTime;   //todo:这里后期可能要改成aop环绕通知

}
