package com.votacao.service;

import com.votacao.model.Votacao;
import com.votacao.repository.VotacaoRepository;
import com.votacao.requests.PautaRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private final PautaRequests pautaRequests;
    private ScheduledExecutorService executor;

    @Autowired
    public VotacaoService(VotacaoRepository votacaoRepository, PautaRequests pautaRequests) {
        this.votacaoRepository = votacaoRepository;
        this.pautaRequests = pautaRequests;
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
        Votacao votacaoPersistida = votacaoRepository.findByPautaId(votacao.getPautaId());

        if(votacaoPersistida != null)
          throw new IllegalArgumentException(String.format("Já existe uma votação com o status %s para essa pauta",
               votacaoPersistida.getStatusVotacao()));

        pautaRequests.getPautas(votacao.getPautaId());
    }

    public List<Votacao> getVotacoes() {
        return votacaoRepository.findAll();
    }
}
