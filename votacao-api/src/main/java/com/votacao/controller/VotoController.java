package com.votacao.controller;

import com.votacao.model.dto.VotoDTO;
import com.votacao.service.VotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votar")
public class VotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);

    @Autowired
    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    private ResponseEntity<?> votar(@RequestBody VotoDTO votoDTO) {
        LOGGER.info("Recebendo requisição para realização de voto");
        return votoService.registrarVoto(votoDTO);
    }
}
