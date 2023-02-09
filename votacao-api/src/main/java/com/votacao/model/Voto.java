package com.votacao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.votacao.model.dto.VotoDTO;
import com.votacao.utils.StatusVoto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voto")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Voto {

    @Id
    private String votoId;
    private Long pautaId;
    private String cpf;
    private StatusVoto opcaoDeVoto;
    @JsonIgnore
    @DBRef
    private Votacao votacao;


    public Voto(VotoDTO votoDTO) {
        this.cpf = votoDTO.getCpf();
        this.opcaoDeVoto = votoDTO.getOpcaoDeVoto();
        this.pautaId = votoDTO.getPautaId();
    }
}
