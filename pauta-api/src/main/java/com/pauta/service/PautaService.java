package com.pauta.service;

import com.pauta.model.Pauta;
import com.pauta.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public Pauta addPauta(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public List<Pauta> listPautas() {
        return pautaRepository.findAll();
    }

    public HttpStatus verificaExistênciaPauta(Long id) {
        Optional<Pauta> pauta = pautaRepository.findById(id);

        if(pauta.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        else {
            return HttpStatus.FOUND;
        }

    }
}
