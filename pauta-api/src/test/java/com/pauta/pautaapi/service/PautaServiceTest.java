package com.pauta.pautaapi.service;

import com.pauta.model.Pauta;
import com.pauta.repository.PautaRepository;
import com.pauta.service.PautaService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Test
    public void deveAdicionarPauta() {
        Pauta p = new Pauta("Pauta teste");

        when(pautaRepository.save(p)).thenReturn(p);

        pautaService.addPauta(p);

        verify(pautaRepository, times(1));

    }

    @Test
    public void verificaExistênciaPautaDeveRetornarNOT_FOUND() {
        Optional<Pauta> pauta = Optional.empty();

        when(pautaRepository.findById(any(Long.class))).thenReturn(pauta);

        Integer notFoundValue = pautaService.verificaExistênciaPauta(1L);

        assertEquals(notFoundValue, HttpStatus.NOT_FOUND.value());
        verify(pautaRepository, times(1));
    }

    @Test
    public void verificaExistênciaPautaDeveRetornarFOUND() {
        Long pautaId = 1L;
        Optional<Pauta> pauta = Optional.of(new Pauta(pautaId, "pauta Teste"));

        when(pautaRepository.findById(pautaId)).thenReturn(pauta);

        Integer notFoundValue = pautaService.verificaExistênciaPauta(pautaId);

        assertEquals(notFoundValue, HttpStatus.FOUND.value());
        verify(pautaRepository, times(1));
    }



}
