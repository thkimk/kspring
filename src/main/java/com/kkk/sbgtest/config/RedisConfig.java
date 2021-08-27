package com.kkk.sbgtest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;

import java.time.Duration;


//@Configuration
public class RedisConfig {
    private final String HOSTNAME;
    private final int PORT;
    private final int DATABASE;
    private final String PASSWORD;
    private final long TIMEOUT;

    public RedisConfig(
            @Value("${redis.hostname}") String hostname,
            @Value("${redis.port}") int port,
            @Value("${redis.database}") int database,
            @Value("${redis.password}") String password,
            @Value("${redis.timeout}") long timeout
    ) {
        this.HOSTNAME = hostname;
        this.PORT = port;
        this.DATABASE = database;
        this.PASSWORD = password;
        this.TIMEOUT = timeout;
    }

    // Redis 접속용 빈
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(HOSTNAME);
        config.setPort(PORT);
        config.setDatabase(DATABASE);
        config.setPassword(PASSWORD);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(TIMEOUT))
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    // 텍스트를 저장하기 위한 빈
    @Bean
    public StringRedisTemplate stringRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        return template;
    }

    // 객체를 저장하기 위한 빈
    @Bean
    public RedisTemplate<String, byte[]> messagePackRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setEnableDefaultSerializer(false);

        return template;
    }

    // 객체의 저장이나 조회시 serialize를 위한 빈
    @Bean
    public ObjectMapper messagePackObjectMapper() {
        // 추가로 날짜를 저장하기 위해 JavaTimeModule을 등록하고, timestamp 형식으로 저장하는 기능을 비활성화하는 옵션을 적용
        // 아래 설정 없이 기본 설정으로 사용하면 날짜형식의 데이터를 핸들링할 때 timestamp 형식으로 사용하게 됩니다.
        return new ObjectMapper(new MessagePackFactory())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
