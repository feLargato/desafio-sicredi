package com.votacao.model;

import com.votacao.utils.StatusVoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Voto {

    private String cpf;
    private StatusVoto opcaoDeVoto;


}
