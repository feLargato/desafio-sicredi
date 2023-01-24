package com.votacao.model;

import com.votacao.utils.StatusVotacao;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;

@Data
@Document(collection = "votacao")
public class Votacao {

    @Id
    private String id;
    private Long pautaId;
    private StatusVotacao statusVotacao;
    private Integer duracao;
    private LocalDateTime iniciaEm;
    private LocalDateTime terminaEm;

    public void abrirVotacao() {
        setStatusVotacao(StatusVotacao.ABERTO);
        setIniciaEm(LocalDateTime.now());
        setTerminaEm(LocalDateTime.now().plusMinutes(getDuracao()));
    }

    public void encerrarVotacao() {
        setStatusVotacao(StatusVotacao.FECHADO);
    }




}
