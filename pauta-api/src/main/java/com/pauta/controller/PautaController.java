package com.pauta.controller;

import com.pauta.model.Pauta;
import com.pauta.service.PautaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PautaController.class);

    private static final String REQUISICAO_RECEBIDA = "Requisição recebida ";

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<?> addPauta(@RequestBody Pauta pauta) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para salvar o objeto: " + pauta.toString());
        return pautaService.addPauta(pauta);
    }

    @GetMapping
    public List<Pauta> listPautas() {
        LOGGER.info(REQUISICAO_RECEBIDA + " para listar as pautas cadastradas");
        return pautaService.listPautas();
    }

    @GetMapping("/{pautaId}")
    public int validarExistenciaPauta(@PathVariable Long pautaId) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para validar a existência da pauta " + pautaId);
        return pautaService.verificaExistênciaPauta(pautaId);
    }

    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<?> getResultado(@PathVariable Long pautaId) {
        LOGGER.info(REQUISICAO_RECEBIDA + " para consultar o resultado de votação da pauta: " + pautaId);
        return pautaService.getResultadoVotacao(pautaId);
    }

}
