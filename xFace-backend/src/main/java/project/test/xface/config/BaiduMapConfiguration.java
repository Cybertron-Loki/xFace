package project.test.xface.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import project.test.xface.utils.BaiduMapUtils;

//@Configuration

public class BaiduMapConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public  BaiduMapUtils baiduMapUtils( BaiduMapProperties baiduMapProperties) {
//            return new BaiduMapUtils(baiduMapProperties.getAk());
      return null;
    }

}
