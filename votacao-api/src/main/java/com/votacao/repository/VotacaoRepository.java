package com.votacao.repository;

import com.votacao.model.Votacao;
import com.votacao.utils.StatusVotacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotacaoRepository extends MongoRepository<Votacao, String> {

    Votacao findByPautaId(Long pautaId);

}
