package com.associado.associadoapi.controller;

import com.associado.associadoapi.service.ValidadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validar_cpf")
public class ValidadorController {

	@Autowired
	private ValidadorService associadoService;

	@GetMapping("/{cpf}")
	public Integer verificarCpf(@PathVariable String cpf) {
		return associadoService.validarCpf(cpf);
	}
	
	
}
