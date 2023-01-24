package com.associado.associadoapi.controller;

import com.associado.associadoapi.model.Associado;
import com.associado.associadoapi.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associado")
public class AssociadoController {

	@Autowired
	private AssociadoService associadoService;
	@Autowired

	@PostMapping
	private Associado addAssociado(@RequestBody Associado associado) {
		return associadoService.addAssociado(associado);
	}
	
	
}
