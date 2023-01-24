package com.votacao.model;

import com.votacao.utils.StatusVoto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voto")
@Data
public class Voto {

    @Id
    private String votoId;
    private Long pautaId;
    private String cpf;
    private StatusVoto opcaoDeVoto;
    @DBRef
    private Votacao votacao;


}
