package com.votacao.utils;

import com.votacao.controller.VotacaoController;
import com.votacao.model.Votacao;
import com.votacao.model.Voto;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.requests.PautaRequests;
import com.votacao.requests.ValidarCpfRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Validacoes {

    private static final Logger LOGGER = LoggerFactory.getLogger(VotacaoController.class);
    private final PautaRequests pautaRequests;
    private final ValidarCpfRequest validarCpfRequest;
    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository;

    @Autowired
    public Validacoes(PautaRequests pautaRequests, VotacaoRepository votacaoRepository, VotoRepository votoRepository,
                      ValidarCpfRequest validarCpfRequest) {
        this.pautaRequests = pautaRequests;
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
        this.validarCpfRequest = validarCpfRequest;
    }

    public void validarDuracao(Votacao votacao) {
        if(votacao.getDuracao() == null)
            votacao.setDefault();
    }


    public void validarOpcaoDeVoto(StatusVoto statusVoto) {
        validarNulo(statusVoto, "statusVoto");

        if (!statusVoto.equals(StatusVoto.SIM) && !statusVoto.equals(StatusVoto.NAO)) {
            String message = "Opção de voto inválida (SIM/NAO)";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }

    }

    public void validarExistenciaPauta(Long pautaId) {
        validarNulo(pautaId, "pautaId");
        Integer response = pautaRequests.getPautas(pautaId);

        if(response == HttpStatus.NOT_FOUND.value()) {
            String message = String.format("Não existe uma pauta com o id %s",
                    pautaId);
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public void validarCpf(String cpf) {

        validarNulo(cpf, "CPF");

        //Integer response = validarCpfRequest.validarCpf(cpf);

        /*if(response == HttpStatus.NOT_FOUND.value())
            throw new IllegalArgumentException(String.format("Cpf %s inválido", cpf));*/
    }

    public void validarPautaEstaEmVotacao(Long pautaId) {
       Boolean votacao = votacaoRepository.existsByPautaId(pautaId);

       if(!votacao) {
           String message = String.format("A pauta %s não está em votação", pautaId);
           LOGGER.error(message);
           throw new IllegalArgumentException(message);

       }
    }

    public void validarExistenciaVotacao(Votacao votacao) {
        Votacao votacaoPersistida = votacaoRepository.findByPautaId(votacao.getPautaId());

        if(votacaoPersistida != null) {
            String message = String.format("Já existe uma votação com o status %s para essa pauta",
                    votacaoPersistida.getStatusVotacao());
            LOGGER.error(message);
            throw new IllegalArgumentException(message);

        }
    }

    public void validarExistenciadoVotoPorPautaIdCpf(String cpf, Long pautaId) {
        Boolean votoExiste = votoRepository.existsByCpfAndPautaId(cpf, pautaId);

        if(votoExiste) {
            String message = String.format("Já existe um voto o cpf %s para a pauta %s",
                    cpf, pautaId);
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public void validarVotacaoAbertaParaVoto(Long pautaId) {
         Boolean votacaoFechada = votacaoRepository.existsByPautaIdAndStatusVotacao(pautaId, StatusVotacao.FECHADO);

         if(votacaoFechada) {
            String message = String.format("A votação para a pauta %s está encerrada",
                    pautaId);
            LOGGER.error(message);
             throw new IllegalArgumentException(message);
         }

    }

    private void validarNulo(Object obj, String nomeCampo) {
        if(obj == null || obj.equals("")) {
            String message = nomeCampo + " Não pode ser nulo ou vazio";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
