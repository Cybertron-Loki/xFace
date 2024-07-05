package project.test.xface.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
@Slf4j
public class RedissonConfig {

    private String ip;
    private String port;
    private String password;
    private Integer database;

//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        System.out.println("redis ip: " + ip + ", port: " + port);
//        String redisAddress = String.format("redis://%s:%s", ip, port);
//        config.useSingleServer().setAddress(redisAddress).setPassword(password).setDatabase(database);
////        Config config = Config.fromYAML(new File("config-file.yaml"));
//        RedissonClient redisson = Redisson.create(config);
//        return redisson;
//    }

}