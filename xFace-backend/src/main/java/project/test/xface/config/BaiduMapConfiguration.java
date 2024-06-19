package project.test.xface.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.test.xface.utils.BaiduMapUtils;

@Configuration

public class BaiduMapConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public  BaiduMapUtils baiduMapUtils( BaiduMapProperties baiduMapProperties) {
            return new BaiduMapUtils(baiduMapProperties.getAk(),baiduMapProperties.getURL());
//      return null;
    }

}
