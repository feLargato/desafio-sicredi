package com.votacao.model;

import com.votacao.utils.StatusVoto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultadoVotacao {

    private Long votosSim;
    private Long votosNao;
    private Long votosContabilizados;
    private String resultado;
    private String pautaId;


    public ResultadoVotacao contabilizarResultado(List<Voto> votos) {
        this.votosContabilizados = votos.stream().count();
        this.pautaId = votos.get(0).getPautaId().toString();
        this.votosSim = votos.stream().filter( v -> v.getOpcaoDeVoto() == StatusVoto.SIM).count();
        this.votosNao = votos.stream().filter( v -> v.getOpcaoDeVoto() == StatusVoto.NAO).count();
        if(votosSim > votosNao) {
            this.resultado = "A pauta foi aprovada";
        }
        else if(votosNao > votosSim) {
            this.resultado = "A pauta foi rejeitada";
        }
        else {
            this.resultado = "A pauta terminou em empate";
        }

        return this;
    }
}
