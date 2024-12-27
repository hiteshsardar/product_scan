package com.security.product.product_scan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.security.product.product_scan.pojo.DBConfigPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Configuration
@EnableMongoRepositories(basePackages = "com.security.product.product_scan")
public class DBConfig extends AbstractMongoClientConfiguration {

    private final Logger logger = LoggerFactory.getLogger(DBConfig.class);
    private final String dbConfigFile = System.getProperty("user.dir") + "/conf/db-config.json";
    String dbName = "product";
    String appendEncodedPass = "001:";

    @Bean
    public MongoClient mongoClient() {
        String dbUser = "";
        String dbPass = "";
        String dbHost = "";
        String dbPort = "";

        ObjectMapper mapper = new ObjectMapper();
        try {
            DBConfigPojo dbConfig = mapper.readValue(new File(dbConfigFile), DBConfigPojo.class);
            dbUser = dbConfig.getUsername();
            dbPass = dbConfig.getPassword();
            dbHost = dbConfig.getHost();
            dbPort = dbConfig.getPort();

            if(!dbPass.startsWith("001:")) {
                String encodedPassword = Base64.getEncoder().encodeToString(dbPass.getBytes());
                dbPass = appendEncodedPass + encodedPassword;
                dbConfig.setPassword(dbPass);
                updatePassToConfigFile(dbConfig);
                logger.info("Password is not encoded");
            }
        } catch (IOException e) {
            logger.error("Unable to read the db config file. {}", e.getMessage());
        }
        String dbUri = "mongodb://" + dbUser + ":" + new String(Base64.getDecoder().decode(dbPass.split(appendEncodedPass)[1])) + "@" + dbHost + ":" + dbPort + "/?authSource=admin";
        return MongoClients.create(dbUri);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    private void updatePassToConfigFile(DBConfigPojo dbConfig) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("username", dbConfig.getUsername());
        jsonNode.put("password", dbConfig.getPassword());
        jsonNode.put("host", dbConfig.getHost());
        jsonNode.put("port", dbConfig.getHost());

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(dbConfigFile), jsonNode);
        mapper.writeValue(new File(dbConfigFile), dbConfig);
    }

}
