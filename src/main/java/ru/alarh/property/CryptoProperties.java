package ru.alarh.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@ConfigurationProperties(prefix = "crypto")
@Data
public class CryptoProperties {

    private String secretKey;
    private ArrayList<String> accessSignatures;

}