package com.pauta.controller;

import com.pauta.model.Pauta;
import com.pauta.service.PautaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PautaController.class);

    private static final String REQUISICAO_RECEBIDA = "Requisição recebida ";

    @Autowired
    private PautaService pautaService;

    @PostMapping
    private Pauta addPauta(@RequestBody Pauta pauta) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para salvar o objeto: " + pauta.toString());
        return pautaService.addPauta(pauta);
    }

    @GetMapping
    private List<Pauta> listPautas() {
        LOGGER.info(REQUISICAO_RECEBIDA + " para listar as pautas cadastradas");
        return pautaService.listPautas();
    }

    @GetMapping("{pautaId}")
    private int validaExistenciaPauta(@PathVariable Long pautaId) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para validar a existência da pauta " + pautaId);
        return pautaService.verificaExistênciaPauta(pautaId);
    }

    @GetMapping("/resultado/{pautaId}")
    private ResponseEntity<?> getResultado(@PathVariable Long pautaId) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para consultar o resultado de votação da pauta: " + pautaId);
        return pautaService.getResultadoVotacao(pautaId);
    }

}
