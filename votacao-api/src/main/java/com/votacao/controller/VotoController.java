package com.votacao.controller;

import com.votacao.model.Voto;
import com.votacao.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votar")
public class VotoController {

    @Autowired
    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }


    @PostMapping
    private Voto votar(@RequestBody Voto voto) {
        return votoService.votar(voto);
    }
}
