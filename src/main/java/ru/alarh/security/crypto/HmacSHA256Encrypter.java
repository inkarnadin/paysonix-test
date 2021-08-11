package ru.alarh.security.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.alarh.property.CryptoProperties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HmacSHA256Encrypter implements Encrypter {

    private static final String ALGORITHM = "HmacSHA256";

    private final CryptoProperties props;

    public Optional<String> sign(String msg) {
        try {
            final Mac mac = Mac.getInstance(ALGORITHM);
            final SecretKeySpec secretKey = new SecretKeySpec(props.getSecretKey().getBytes(), ALGORITHM);
            mac.init(secretKey);
            final byte[] data = mac.doFinal(msg.getBytes());
            StringBuilder result = new StringBuilder();
            for (final byte element : data)
                result.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));

            log.info(result.toString());
            return Optional.of(result.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}