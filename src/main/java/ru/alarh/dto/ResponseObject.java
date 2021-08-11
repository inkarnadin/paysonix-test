package ru.alarh.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseObject {

    private String status;
    private List<Map<String, String>> result;

}