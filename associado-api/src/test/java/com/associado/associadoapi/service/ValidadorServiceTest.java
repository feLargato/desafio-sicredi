package com.associado.associadoapi.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ValidadorServiceTest {

    @InjectMocks
    private ValidadorService validadorService;

    @Test
    public void validarCpfComCpfValidoDeveRetornarFOUND() {
        Integer statusCPF = validadorService.validarCpf("03682535209");

        assertEquals(statusCPF, HttpStatus.FOUND.value());
    }

    @Test
    public void validarCpfComCpfValidoDeveRetornarNOTFOUND() {
        Integer statusCPF = validadorService.validarCpf("0362535209");

        assertEquals(statusCPF, HttpStatus.NOT_FOUND.value());
    }

}
