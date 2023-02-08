package com.pauta.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ResultadoVotacao {

    @Id
    private Long pautaId;
    @Column
    private Long votosSim;
    @Column
    private Long votosNao;
    @Column
    private Long votosContabilizados;
    @Column
    private String resultado;


}
