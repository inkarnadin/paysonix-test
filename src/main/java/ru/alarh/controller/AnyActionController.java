package ru.alarh.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alarh.dto.ResponseObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/do")
public class AnyActionController {

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> anyAction(HttpServletRequest request, @RequestBody String payload, @RequestParam String operationId) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus("success");

        ArrayList<Map<String, String>> result = new ArrayList<>();
        Map<String, String> values = new HashMap<>();
        values.put("signature", request.getHeader("token"));
        result.add(values);

        responseObject.setResult(result);

        return ResponseEntity.ok(responseObject);
    }

}