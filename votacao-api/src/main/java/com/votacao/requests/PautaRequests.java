package com.votacao.requests;

import com.votacao.dto.PautaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PautaRequests {

    @Autowired
    private RestTemplate restTemplate;

    public HttpStatus getPautas(Long id) {
        ResponseEntity<PautaDTO> response = restTemplate.exchange(
                "https://localhost:8081/pauta/" + id,
                HttpMethod.GET,
                null,
                PautaDTO.class
        );
        return response.getStatusCode();
    }

}
