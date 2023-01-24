package com.votacao.repository;

import com.votacao.model.Voto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends MongoRepository<Voto, String> {
        Boolean existsByCpfAndPautaId(String cpf, Long pautaId);
}
