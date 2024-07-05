package project.test.xface.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.test.xface.utils.AliOssUtil;

@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){

        return new AliOssUtil(aliOssProperties.getEndpoint(),
        aliOssProperties.getAccessKeyId(),
        aliOssProperties.getAccessKeySecret(),
        aliOssProperties.getBucketName());
    }
}
