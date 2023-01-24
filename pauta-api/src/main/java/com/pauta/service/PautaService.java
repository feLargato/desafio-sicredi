package com.pauta.service;

import com.pauta.model.Pauta;
import com.pauta.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {


    private final PautaRepository pautaRepository;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta addPauta(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public List<Pauta> listPautas() {
        return pautaRepository.findAll();
    }

    public int verificaExistênciaPauta(Long id) {
        Optional<Pauta> pauta = pautaRepository.findById(id);

        if(pauta.isEmpty()) {
            return HttpStatus.NOT_FOUND.value();
        }
        else {
            return HttpStatus.FOUND.value();
        }

    }

    @KafkaListener(topics = "resultado.votacao", groupId = "votacao")
    public void receberResultado(String message) {
        System.out.println(message);
    }
}
