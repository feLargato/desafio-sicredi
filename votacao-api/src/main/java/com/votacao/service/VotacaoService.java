package com.votacao.service;

import com.google.gson.Gson;
import com.votacao.model.ResultadoVotacao;
import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.utils.Validacoes;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository;
    private ScheduledExecutorService executor;
    private final Validacoes validador;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public VotacaoService(VotacaoRepository votacaoRepository, VotoRepository votoRepository,
                          Validacoes validacoes,
                          KafkaTemplate kafkaTemplate) {
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
        this.validador = validacoes;
        this.executor = Executors.newScheduledThreadPool(1);
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<?> configurarVotacao(Votacao votacao) {
        validarAbertura(votacao);
        Votacao votacaoAberta  = abrirVotacao(votacao);
        programarEncerramentoVotacao(votacaoAberta);
        return new ResponseEntity<>(votacaoAberta, HttpStatus.CREATED);
    }

    private Votacao abrirVotacao(Votacao votacao) {
        votacao.abrirVotacao();
        return votacaoRepository.save(votacao);
    }

    private void programarEncerramentoVotacao(Votacao votacao) {
        executor.schedule(() -> {
            encerrarVotacao(votacao);
            ResultadoVotacao resultadoVotacao = contabilizarResultado(votacao);
            publicarResultado(resultadoVotacao);
        }, votacao.getDuracao(), TimeUnit.MINUTES);
    }

    public void encerrarVotacao(Votacao votacao) {
        votacao.encerrarVotacao();
        votacaoRepository.save(votacao);
    }
    public ResultadoVotacao contabilizarResultado(Votacao votacao) {
        List<Voto> votos = votoRepository.findAllByPautaId(votacao.getPautaId());
        ResultadoVotacao resultadoVotacao = new ResultadoVotacao();
        if(votos.isEmpty()) {
            resultadoVotacao.setResultado("A pauta não recebeu nenhum voto");
            return  resultadoVotacao;
        }

        return resultadoVotacao.contabilizarResultado(votos);

    }

    public void publicarResultado(ResultadoVotacao resultadoVotacao) {

        String message = new Gson().toJson(resultadoVotacao);

        kafkaTemplate.send("resultado.votacao", message);
        System.out.println("Mensagem enviada \n" + message);
    }

    public void validarAbertura(Votacao votacao) {
        validador.validarDuracao(votacao);
        validador.validarExistenciaPauta(votacao.getPautaId());
        validador.validarExistenciaVotacao(votacao);
    }

    public List<Votacao> getVotacoes() {
        return votacaoRepository.findAll();
    }
}
