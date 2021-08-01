package com.poalim.ci.SwaggerValidator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class clr implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final Service service;
    @Override
    public void run(String... args) throws Exception {


        service.scanSwagger(objectMapper.readTree(Swagger.SWAGGER),"");

    }
}
