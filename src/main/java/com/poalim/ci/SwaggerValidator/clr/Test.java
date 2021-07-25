package com.poalim.ci.SwaggerValidator.clr;

import com.fasterxml.jackson.databind.JsonNode;
import com.poalim.ci.SwaggerValidator.Swagger;
import com.poalim.ci.SwaggerValidator.services.SwaggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Test implements CommandLineRunner {



    private final SwaggerService swaggerService;

    @Override
    public void run(String... args) throws Exception {
    for(Map.Entry<String, JsonNode> entry : swaggerService.getSwaggerPathsParams(Swagger.SWAGGER).entrySet()) {
        System.out.println("-------------------------------------" + entry.getKey() + "-------------------------------------");
        swaggerService.recurOverSwagger(entry.getValue());
    }
    }
}
