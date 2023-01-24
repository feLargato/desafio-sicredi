package com.votacao.service;

import com.votacao.model.ResultadoVotacao;
import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.utils.Validacoes;
import org.springframework.beans.factory.annotation.Autowired;
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
            encerraVotacao(votacao);
            ResultadoVotacao resultadoVotacao = contabilizaResultado(votacao);
            publicaResultado(resultadoVotacao);
        }, votacao.getDuracao(), TimeUnit.MINUTES);
    }

    public void encerraVotacao(Votacao votacao) {
        votacao.encerrarVotacao();
        votacaoRepository.save(votacao);
    }
    public ResultadoVotacao contabilizaResultado(Votacao votacao) {
        List<Voto> votos = votoRepository.findAllByPautaId(votacao.getPautaId());
        ResultadoVotacao resultadoVotacao = new ResultadoVotacao();
        if(votos.isEmpty()) {
            resultadoVotacao.setResultado("A pauta não recebeu nenhum voto");
            return  resultadoVotacao;
        }

        return resultadoVotacao.contabilizarResultado(votos);

    }

    public void publicaResultado(ResultadoVotacao resultadoVotacao) {
        String message;
        if(resultadoVotacao.getVotosContabilizados() == null) {
            message = resultadoVotacao.getResultado();
        }
        else {
           message = String.format("Votação para a pauta %s encerrada. \n" +
                           "Votos Contabilizados: %s \n" +
                           "Votos SIM: %s \n" +
                           "Votos NAO: %s \n." +
                           resultadoVotacao.getResultado(),
                   resultadoVotacao.getPautaId(), resultadoVotacao.getVotosContabilizados(),
                   resultadoVotacao.getVotosSim(), resultadoVotacao.getVotosNao());
        }

        kafkaTemplate.send("resultado.votacao", message);
        System.out.println("Mensagem enviada \n" + message);
    }

    public void validarAbertura(Votacao votacao) {
        validador.validarExistenciaPauta(votacao.getPautaId());
        validador.validarExistenciaVotacao(votacao);
    }

    public List<Votacao> getVotacoes() {
        return votacaoRepository.findAll();
    }
}
