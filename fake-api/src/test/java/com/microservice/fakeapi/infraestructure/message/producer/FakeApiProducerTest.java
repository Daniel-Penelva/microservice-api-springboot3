package com.microservice.fakeapi.infraestructure.message.producer;

import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiProducerTest {

    @InjectMocks
    FakeApiProducer producer;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testeEnviarRespostaCadastroProdutosComSucesso(){
        String mensagem = "Produto cadastro com sucesso";

        producer.enviaRespostaCadastroProdutos(mensagem);

        verify(kafkaTemplate).send(any(), messageCaptor.capture());
        assertEquals(mensagem, messageCaptor.getValue());
    }

    @Test
    void testeRetornarExceptionCasoOcorraErroNaProducaoDaMensagem(){
        doThrow(new RuntimeException("Erro ao produzir mensagem")).when(kafkaTemplate).send(any(), any());

        BusinessException e = assertThrows(BusinessException.class, () -> producer.enviaRespostaCadastroProdutos(null));

        assertThat(e.getMessage(), is("Erro ao produzir mensagem do kafka"));
        verifyNoMoreInteractions(kafkaTemplate);
    }
}
