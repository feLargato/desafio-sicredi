package com.votacao.repository;

import com.votacao.model.Voto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends MongoRepository<Voto, String> {
        Boolean existsByCpfAndPautaId(String cpf, Long pautaId);

    List<Voto> findAllByPautaId(Long pautaId);
}
