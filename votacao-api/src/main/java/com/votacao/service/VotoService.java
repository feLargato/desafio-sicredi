package com.votacao.service;

import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.requests.PautaRequests;
import com.votacao.utils.Validacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VotoService {


    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository;
    private final Validacoes validador;

    public VotoService(VotacaoRepository votacaoRepository, VotoRepository votoRepository,
                       Validacoes validacoes) {
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
        this.validador = validacoes;

    }


    public Voto votar(Voto voto) {
        validarVoto(voto);
        Votacao votacao = votacaoRepository.findByPautaId(voto.getPautaId());
        voto.setVotacao(votacao);
        return votoRepository.save(voto);

    }

    public void validarVoto(Voto voto) {
        Long pautaId = voto.getPautaId();
        validador.validarExistenciaPauta(pautaId);
        validador.validarPautaEstaEmVotacao(pautaId);
        validador.validarVotacaoAbertaParaVoto(pautaId);
        validador.validarExistenciadoVotoPorPautaIdCpf(voto.getCpf(), pautaId);
    }
}
