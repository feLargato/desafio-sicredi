package com.pauta.service;

import com.google.gson.Gson;
import com.pauta.controller.PautaController;
import com.pauta.model.Pauta;
import com.pauta.model.ResultadoVotacao;
import com.pauta.repository.PautaRepository;
import com.pauta.repository.ResultadoVotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PautaController.class);
    private final PautaRepository pautaRepository;
    private final ResultadoVotacaoRepository resultadoVotacaoRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository, ResultadoVotacaoRepository resultadoVotacaoRepository) {
        this.pautaRepository = pautaRepository;
        this.resultadoVotacaoRepository = resultadoVotacaoRepository;
    }

    public Pauta addPauta(Pauta pauta) {
        try{
            pautaRepository.save(pauta);
            LOGGER.info("A pauta foi cadastrada:" + pauta);
        }
        catch (Exception e) {
            LOGGER.error("Ocorrreu um erro ao cadastrar a pauta: " + e.getMessage());
        }
        return pauta;

    }

    public List<Pauta> listPautas() {
        return pautaRepository.findAll();
    }

    public int verificaExistênciaPauta(Long id) {
        Optional<Pauta> pauta = pautaRepository.findById(id);

        if(pauta.isEmpty()) {
            LOGGER.info(String.format("A pauta %s não existe", id));
            return HttpStatus.NOT_FOUND.value();
        }
        else {
            LOGGER.info(String.format("Pauta %s encontrada", id));
            return HttpStatus.FOUND.value();
        }

    }

    public ResponseEntity<?> getResultadoVotacao(Long pautaId) {
        LOGGER.info("Buscando resultado da votacao para a pauta:" + pautaId);
        Optional<ResultadoVotacao> resultadoVotacao = resultadoVotacaoRepository.findById(pautaId);

        if(resultadoVotacao.isEmpty()){
            LOGGER.info("Resultado ainda não publicado");
            return new ResponseEntity<>("O resultado da votação dessa pauta ainda não foi publicado", HttpStatus.NOT_FOUND);

        }

        LOGGER.info("Resultado encontrado:" + resultadoVotacao);
        return new ResponseEntity<>(resultadoVotacao, HttpStatus.CREATED);
    }


    @KafkaListener(topics = "resultado.votacao", groupId = "votacao")
    public void receberResultado(String message) {

        LOGGER.info("Recebido resultado da votacao: " + message);
        ResultadoVotacao resultadoVotacao = new Gson().fromJson(message, ResultadoVotacao.class);

        try{
            ResultadoVotacao resultadoSalvo = resultadoVotacaoRepository.save(resultadoVotacao);
            LOGGER.info("O resultado da votação foi persistido: " + resultadoSalvo.toString());
        }
        catch (Exception e) {
            LOGGER.error("Erro ao persistir resultado da votação: " + e.getMessage());
        }

    }
}
