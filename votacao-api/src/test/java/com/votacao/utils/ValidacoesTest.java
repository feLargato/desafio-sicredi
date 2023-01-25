package com.votacao.utils;

import com.votacao.model.Votacao;
import com.votacao.repository.VotacaoRepository;
import com.votacao.repository.VotoRepository;
import com.votacao.requests.PautaRequests;
import com.votacao.requests.ValidarCpfRequest;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class ValidacoesTest {

    @Mock
    private PautaRequests pautaRequests;

    @Mock
    private ValidarCpfRequest validarCpfRequest;

    @Mock
    private VotacaoRepository votacaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private Validacoes validacoes;

    private final static Long PAUTA_ID = 1L;
    private final static String CPF = "123.456.789-00";

    @BeforeEach
    void setUp() {
        validacoes = new Validacoes(pautaRequests, votacaoRepository, votoRepository, validarCpfRequest);
    }

    @Test
    public void validarExistenciaPautaDeveLancarExcecao() {
        when(pautaRequests.getPautas(PAUTA_ID)).thenReturn(HttpStatus.NOT_FOUND.value());

        assertThrows(IllegalArgumentException.class, () -> validacoes.validarExistenciaPauta(PAUTA_ID));
        verify(pautaRequests, times(1));
    }

    @Test
    public void validarCpfDeveLancarExcecao() {
        when(validarCpfRequest.validarCpf(CPF)).thenReturn(HttpStatus.NOT_FOUND.value());

        assertThrows(IllegalArgumentException.class, () -> validacoes.validarCpf(CPF));
        verify(validarCpfRequest, times(1));
    }

    @Test
    public void validarPautaEstaEmVotacaoDeveLancarExcecao() {
        when(votacaoRepository.existsByPautaId(PAUTA_ID)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> validacoes.validarPautaEstaEmVotacao(PAUTA_ID));
        verify(votacaoRepository, times(1));
    }

    @Test
    public void validarExistenciaVotacaoDeveLancarExcecao() {
        Votacao votacao = new Votacao();
        votacao.setPautaId(PAUTA_ID);

        when(votacaoRepository.findByPautaId(PAUTA_ID)).thenReturn(votacao);
        assertThrows(IllegalArgumentException.class, () -> validacoes.validarExistenciaVotacao(votacao));
        verify(votacaoRepository, times(1));
    }

    @Test
    public void validarExistenciadoVotoPorPautaIdCpfDeveLancarExcecao() {

        when(votoRepository.existsByCpfAndPautaId(CPF, PAUTA_ID)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> validacoes.validarExistenciadoVotoPorPautaIdCpf(CPF, PAUTA_ID));
        verify(votoRepository, times(1));
    }

    @Test
    public void validarVotacaoAbertaParaVotoDeveLancarExcecao() {
        when(votacaoRepository.existsByPautaIdAndStatusVotacao(PAUTA_ID, StatusVotacao.FECHADO)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> validacoes.validarVotacaoAbertaParaVoto(PAUTA_ID));
        verify(votacaoRepository, times(1));
    }

}
