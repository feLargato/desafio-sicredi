package com.associado.associadoapi.service;

import com.associado.associadoapi.model.Associado;
import com.associado.associadoapi.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociadoService {

	@Autowired
	private AssociadoRepository associadoRepository;

	public Associado addAssociado(Associado associado) {
		return associadoRepository.save(associado);
	}
	
	
	
}
