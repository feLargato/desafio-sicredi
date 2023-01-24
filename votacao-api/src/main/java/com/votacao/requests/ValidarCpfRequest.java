package com.votacao.requests;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ValidarCpfRequest {

    public Integer validarCpf(String cpf) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost:8080")
                .path("validarCpf/" + cpf)
                .build();

        ResponseEntity<Integer> response = restTemplate.getForEntity(uri.toString(), Integer.class);

        return response.getBody();
    }
}
