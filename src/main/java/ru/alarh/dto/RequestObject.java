package ru.alarh.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Data
public class RequestObject {

    private final Map<String, String> params = new TreeMap<>();

    public RequestObject(String payload) {
        for (String param : payload.split("&")) {
            String[] p = param.split("=");
            params.put(p[0], p[1]);
        }

        params.putAll((Map<? extends String, ? extends String>) params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new)
                ));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> param : params.entrySet())
            joiner.add(param.toString());

        return joiner.toString();
    }

}