package com.votacao.service;

import com.votacao.controller.VotacaoController;
import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.model.dto.VotoDTO;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.utils.Validacoes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class VotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);
    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository;
    private final Validacoes validador;

    @Autowired
    public VotoService(VotacaoRepository votacaoRepository, VotoRepository votoRepository,
                       Validacoes validacoes) {
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
        this.validador = validacoes;

    }

    public ResponseEntity<?> registrarVoto(VotoDTO votoDTO) {
        Voto voto = new Voto(votoDTO);
        validarVoto(voto);

        Votacao votacao = votacaoRepository.findByPautaId(voto.getPautaId());
        voto.setVotacao(votacao);

        Voto votoValido = votoRepository.save(voto);
        LOGGER.info("Voto registrado" + voto);
        return new ResponseEntity<>(votoValido, HttpStatus.CREATED);
    }

    public void validarVoto(Voto voto) {
        LOGGER.info("Validando dados do voto");
        Long pautaId = voto.getPautaId();
        validador.validarCpf(voto.getCpf());
        validador.validarOpcaoDeVoto(voto.getOpcaoDeVoto());
        validador.validarExistenciaPauta(pautaId);
        validador.validarPautaEstaEmVotacao(pautaId);
        validador.validarVotacaoAbertaParaVoto(pautaId);
        validador.validarExistenciadoVotoPorPautaIdCpf(voto.getCpf(), pautaId);
        LOGGER.info("Dados do voto OK");
    }
}
