package ru.alarh.security;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.alarh.dto.RequestObject;
import ru.alarh.http.RequestWrapper;
import ru.alarh.property.CryptoProperties;
import ru.alarh.security.crypto.Encrypter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestTokenFilter implements Filter {

    private static final String tokenHeaderName = "token";

    private final CryptoProperties props;
    private final Encrypter encrypter;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        RequestWrapper requestWrapper =  new RequestWrapper((HttpServletRequest) request);
        String payload = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());
        RequestObject requestObject = new RequestObject(payload);

        log.info(requestObject.toString());

        Optional<String> signature = encrypter.sign(requestObject.toString());
        signature.ifPresent(x ->  requestWrapper.addHeader(tokenHeaderName, x));

        if (signature.isEmpty() || !props.getAccessSignatures().contains(signature.get())) {
            log.warn("Access denied. Wrong or missing signature");

            HttpServletResponse modifiedResponse = (HttpServletResponse) response;
            modifiedResponse.setStatus(403);

            return;
        }

        chain.doFilter(requestWrapper, response);
    }

}