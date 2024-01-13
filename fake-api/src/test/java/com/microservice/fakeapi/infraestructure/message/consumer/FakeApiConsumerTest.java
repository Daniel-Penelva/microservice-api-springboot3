package com.microservice.fakeapi.infraestructure.message.consumer;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.ProdutoService;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.message.consumer.FakeApiConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiConsumerTest {

    @InjectMocks
    FakeApiConsumer consumer;

    @Mock
    ProdutoService service;

    @Test
    void testeReceberMensagemProdutoDTOComSucesso(){
        ProductsDTO produtoDTO = ProductsDTO.builder().descricao("Jaqueta Vermelha com bolsos laterais e Listras").preco(new BigDecimal(250.00)).build();

        doNothing().when(service).salvaProdutoConsumer(produtoDTO);

        consumer.recebeProdutosDTO(produtoDTO);

        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }

    @Test
    void testeGerarExceptionCasoErroNoConsumer(){
        ProductsDTO produtoDTO = ProductsDTO.builder().descricao("Jaqueta Vermelha com bolsos laterais e Listras").preco(new BigDecimal(250.00)).build();

        doThrow(new RuntimeException("Erro ao consumir mensagem")).when(service).salvaProdutoConsumer(produtoDTO);

        BusinessException e = assertThrows(BusinessException.class, () -> consumer.recebeProdutosDTO(produtoDTO));

        assertThat(e.getMessage(), is("Erro ao consumir mensagem do kafka "));
        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }
}
