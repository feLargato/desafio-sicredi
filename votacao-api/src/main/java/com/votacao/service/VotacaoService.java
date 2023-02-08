package com.votacao.service;

import com.google.gson.Gson;
import com.votacao.controller.VotacaoController;
import com.votacao.model.ResultadoVotacao;
import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.model.dto.VotacaoDTO;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.utils.Validacoes;
import io.swagger.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);
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

    public ResponseEntity<?> configurarVotacao(VotacaoDTO votacaoDTO) {
        Votacao votacao = new Votacao(votacaoDTO);
        validarAbertura(votacao);
        Votacao votacaoAberta  = abrirVotacao(votacao);
        programarEncerramentoVotacao(votacaoAberta);
        return new ResponseEntity<>(votacaoAberta, HttpStatus.CREATED);
    }

    private Votacao abrirVotacao(Votacao votacao) {
        try {
            LOGGER.info("Abrindo votação: " + votacao);
            votacao.abrirVotacao();

            Votacao votacaoAberta = votacaoRepository.save(votacao);
            LOGGER.info("Votacao aberta:" + votacaoAberta);

            return votacaoAberta;
        }
        catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao abrir a votacao: " + votacao);
            return null;
        }
    }

    private void programarEncerramentoVotacao(Votacao votacao) {
        LOGGER.info(String.format("Votacao %s programada para encerrar em %s",
                votacao.getId(), votacao.getTerminaEm()));
        executor.schedule(() -> {
            encerrarVotacao(votacao);
            ResultadoVotacao resultadoVotacao = contabilizarResultado(votacao);
            publicarResultado(resultadoVotacao);
        }, votacao.getDuracao(), TimeUnit.MINUTES);
    }

    public void encerrarVotacao(Votacao votacao) {
        votacao.encerrarVotacao();
        votacaoRepository.save(votacao);
        LOGGER.info(String.format("Votacao %s encerrada", votacao.getId()));
    }
    public ResultadoVotacao contabilizarResultado(Votacao votacao) {
        LOGGER.info("Contabilizando resultados da votacao");
        List<Voto> votos = votoRepository.findAllByPautaId(votacao.getPautaId());
        ResultadoVotacao resultadoVotacao = new ResultadoVotacao();
        if(votos.isEmpty()) {
            String message = "A pauta não recebeu nenhum voto";
            LOGGER.info(message);
            resultadoVotacao.setResultado(message);
            return  resultadoVotacao;
        }

        return resultadoVotacao.contabilizarResultado(votos);

    }

    public void publicarResultado(ResultadoVotacao resultadoVotacao) {
        LOGGER.info("Publicando resultados da votação");
        String message = new Gson().toJson(resultadoVotacao);

        kafkaTemplate.send("resultado.votacao", message);
        LOGGER.info("resultado \n " + message);;
    }

    public void validarAbertura(Votacao votacao) {
        LOGGER.info("Validando dados para a abertura da votação");
        validador.validarDuracao(votacao);
        validador.validarExistenciaPauta(votacao.getPautaId());
        validador.validarExistenciaVotacao(votacao);
        LOGGER.info("Dados para abertura da votação OK");
    }

    public List<Votacao> getVotacoes() {
        return votacaoRepository.findAll();
    }
}
