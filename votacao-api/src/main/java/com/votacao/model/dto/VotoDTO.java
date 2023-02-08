package com.votacao.model.dto;

import com.votacao.model.Voto;
import com.votacao.utils.StatusVoto;
import lombok.Data;

@Data
public class VotoDTO {

    private Long pautaId;
    private String cpf;
    private StatusVoto opcaoDeVoto;

}
