package ming.test.xface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("ming.test.xface.dao")
public class XFaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XFaceApplication.class, args);
    }

}
