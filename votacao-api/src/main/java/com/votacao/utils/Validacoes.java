package com.votacao.utils;

import com.votacao.model.Votacao;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.requests.PautaRequests;
import com.votacao.requests.ValidarCpfRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Validacoes {

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

    public void validarExistenciaPauta(Long pautaId) {
        Integer response = pautaRequests.getPautas(pautaId);

        if(response == HttpStatus.NOT_FOUND.value())
            throw new IllegalArgumentException(String.format("Não existe uma pauta com o id %s",
                    pautaId));
    }

    public void validarCpf(String cpf) {
        Integer response = validarCpfRequest.validarCpf(cpf);

        if(response == HttpStatus.NOT_FOUND.value())
            throw new IllegalArgumentException(String.format("Cpf %s inválido", cpf));
    }

    public void validarPautaEstaEmVotacao(Long pautaId) {
       Boolean votacao = votacaoRepository.existsByPautaId(pautaId);

       if(!votacao)
           throw new IllegalArgumentException(String.format("A pauta %s não está em votação", pautaId));
    }

    public void validarExistenciaVotacao(Votacao votacao) {
        Votacao votacaoPersistida = votacaoRepository.findByPautaId(votacao.getPautaId());

        if(votacaoPersistida != null)
            throw new IllegalArgumentException(String.format("Já existe uma votação com o status %s para essa pauta",
                    votacaoPersistida.getStatusVotacao()));
    }

    public void validarExistenciadoVotoPorPautaIdCpf(String cpf, Long pautaId) {
        Boolean votoExiste = votoRepository.existsByCpfAndPautaId(cpf, pautaId);

        if(votoExiste)
            throw new IllegalArgumentException(String.format("Já existe um voto o cpf %s para a pauta %s",
                    cpf, pautaId));
    }

    public void validarVotacaoAbertaParaVoto(Long pautaId) {
         Boolean votacaoFechada = votacaoRepository.existsByPautaIdAndStatusVotacao(pautaId, StatusVotacao.FECHADO);

         if(votacaoFechada)
             throw new IllegalArgumentException(String.format("A votação para a pauta %s está encerrada",
                     pautaId));

    }

}
