package com.poalim.ci.SwaggerValidator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SwaggerService {

    private final ObjectMapper objectMapper;

    public Map<String, JsonNode> getSwaggerPathsParams(String swagger) throws JsonProcessingException {
        Map<String, JsonNode> pathUrlVsParams = new HashMap<>();
        JsonNode jsonSwagger = objectMapper.readTree(swagger);
        jsonSwagger.path("paths").fields().forEachRemaining(s -> {
            JsonNode pathParams = s.getValue().iterator().next().path("parameters");
            if (!pathParams.isEmpty()) {
                pathUrlVsParams.put(s.getKey(), pathParams);
            }
        });
        return pathUrlVsParams;
    }


    public void recurOverSwagger(final JsonNode node) {
        node.forEach(field -> {
            recurOverJson(field);
        });

    }

    private void recurOverJson(JsonNode field) {

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = field.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> temp = fieldsIterator.next();
            final String key = temp.getKey();
            final JsonNode value = temp.getValue();

            if (value.isContainerNode()) {
                System.out.println(key);
                System.out.println(value);
                recurOverJson(value);

            } else {
                System.out.println("key " + key + " value: " + value);
            }
        }
    }
}



