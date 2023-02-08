package com.votacao.controller;

import com.votacao.model.Votacao;
import com.votacao.service.VotacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votacao")
public class VotacaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);

    @Autowired
    private final VotacaoService votacaoService;

    public VotacaoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @PostMapping
    private ResponseEntity<?> configurarVotacao(@RequestBody  Votacao votacao) {
        LOGGER.info("Recebida requisição para configurar uma votação para a pauta:" + votacao.getPautaId());
        return votacaoService.configurarVotacao(votacao);
    }

    @GetMapping
    private List<Votacao> getVotacoes() {
        LOGGER.info("Recebida requisição para obter lista de votações");
        return votacaoService.getVotacoes();
    }
}
