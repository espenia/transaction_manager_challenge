package twitter.challenge.espenia.infra.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "twitter.challenge.espenia.infra.mongodb.repository")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    
    @Value("${spring.data.mongodb.host:localhost}")
    private String host;
    
    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }    
    
    @Override
    @Bean
    @Primary
    public MongoClient mongoClient() {
        if (StringUtils.hasText(mongoUri)) {
            log.info("Connecting to MongoDB using URI: {}", mongoUri);
            ConnectionString connectionString = new ConnectionString(mongoUri);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            
            return MongoClients.create(mongoClientSettings);
        } else {
            log.info("Connecting to MongoDB at {}:{} with database {}", host, port, databaseName);
            
            MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
                    .applyToClusterSettings(builder -> 
                        builder.hosts(Collections.singletonList(new ServerAddress(host, port))));
            
            return MongoClients.create(settingsBuilder.build());
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
