package com.pauta.controller;

import com.pauta.model.Pauta;
import com.pauta.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    private Pauta addPauta(@RequestBody Pauta pauta) {
        return pautaService.addPauta(pauta);
    }

    @GetMapping
    private List<Pauta> listPautas() {
        return pautaService.listPautas();
    }

    @GetMapping("{pautaId}")
    private HttpStatus validaExistenciaPauta(@PathVariable Long pautaId) {
        return pautaService.verificaExistênciaPauta(pautaId);
    }

}
