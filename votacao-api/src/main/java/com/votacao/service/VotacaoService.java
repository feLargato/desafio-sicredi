package com.votacao.service;

import com.votacao.model.Votacao;
import com.votacao.repository.VotacaoRepository;
import com.votacao.requests.PautaRequests;
import com.votacao.utils.Validacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private ScheduledExecutorService executor;
    private final Validacoes validador;

    @Autowired
    public VotacaoService(VotacaoRepository votacaoRepository, Validacoes validacoes) {
        this.votacaoRepository = votacaoRepository;
        this.validador = validacoes;
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public Votacao configurarVotacao(Votacao votacao) {
        validarAbertura(votacao);
        Votacao votacaoAberta  = abrirVotacao(votacao);
        programarEncerramentoVotacao(votacaoAberta);
        return votacaoAberta;
    }

    private Votacao abrirVotacao(Votacao votacao) {
        votacao.abrirVotacao();
         return votacaoRepository.save(votacao);
    }

    private void programarEncerramentoVotacao(Votacao votacao) {
        executor.schedule(() -> {
            votacao.encerrarVotacao();
            votacaoRepository.save(votacao);
        }, votacao.getDuracao(), TimeUnit.MINUTES);
    }

    public void validarAbertura(Votacao votacao) {
        validador.validarExistenciaPauta(votacao.getPautaId());
        validador.validarExistenciaVotacao(votacao);
    }

    public List<Votacao> getVotacoes() {
        return votacaoRepository.findAll();
    }
}
