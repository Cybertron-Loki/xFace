package project.test.xface.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "baidu.map")
@Data
public class BaiduMapProperties {
     private  String  ak;
     private String URL;

}
