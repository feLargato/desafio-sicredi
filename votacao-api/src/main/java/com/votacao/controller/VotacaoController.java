package com.votacao.controller;

import com.votacao.model.Votacao;
import com.votacao.service.VotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votacao")
public class VotacaoController {

    @Autowired
    private final VotacaoService votacaoService;

    public VotacaoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @PostMapping
    private Votacao configurarVotacao(@RequestBody  Votacao votacao) {
        return votacaoService.configurarVotacao(votacao);
    }

    @GetMapping
    private List<Votacao> getVotacoes() {
        return votacaoService.getVotacoes();
    }
}
