package ming.test.xface.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ming.test.xface.config.BaiduMapProperties;
import ming.test.xface.utils.BaiduMapUtils;

//@Configuration

public class BaiduMapConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public  BaiduMapUtils baiduMapUtils( BaiduMapProperties baiduMapProperties) {
//            return new BaiduMapUtils(baiduMapProperties.getAk());
      return null;
    }

}
