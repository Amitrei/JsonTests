package com.poalim.ci.SwaggerValidator;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class Service {

    private final ObjectMapper objectMapper;
    private Map<JsonNode, String> cacheMap = new ConcurrentHashMap<>();

    public void scanSwagger(JsonNode swagger,String currentPath) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = swagger.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> temp = fieldsIterator.next();
            final String key = temp.getKey();
            final JsonNode value = temp.getValue();


            if (value.isContainerNode()) {
                scanSwagger(value, currentPath +"/" + key);
            }

            if (!(value.path("type").isMissingNode())) {
                System.out.println(key + " -> "+value);
                cacheMap.put(value, key);

            }


            if (value.isArray()) {
                value.forEach(val -> {
                    try {
                        scanSwagger(val,currentPath +"/" + key);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }


            if (key.equals("$ref") && !(cacheMap.containsKey(value))) {
                String[] formattedValue = value.toString().split("/");
                String objectFromRef = formattedValue[formattedValue.length - 1];
                objectFromRef = objectFromRef.substring(0, objectFromRef.length() - 1);
                cacheMap.put(objectMapper.readTree(Swagger.SWAGGER).findPath(formattedValue[1]).findPath(objectFromRef), value.toString());
//                System.out.println(objectMapper.readTree(Swagger.SWAGGER).findPath(formattedValue[1]).findPath(objectFromRef));
                scanSwagger(objectMapper.readTree(Swagger.SWAGGER).findPath(formattedValue[1]).findPath(objectFromRef),currentPath +"/" + key);
            }


        }
        System.out.println(currentPath);
    }


}
