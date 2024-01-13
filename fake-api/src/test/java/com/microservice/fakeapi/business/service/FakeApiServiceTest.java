package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.client.FakeApiClient;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiServiceTest {

    @InjectMocks
    FakeApiService service;

    @Mock
    FakeApiClient client;
    @Mock
    ProdutoConverter converter;
    @Mock
    ProdutoService produtoService;

    @Test
    void testeBuscarProdutosEGravarComSucesso() {

        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();

        ProductsDTO produtoDTO = ProductsDTO.builder()
                .entityId("1245").nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        listaProdutosDTO.add(produtoDTO);

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        when(client.buscaListaProdutos()).thenReturn(listaProdutosDTO);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(false);
        when(converter.toEntity(produtoDTO)).thenReturn(produtoEntity);
        when(produtoService.salvarProdutos(produtoEntity)).thenReturn(produtoEntity);
        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutosDTO);

        List<ProductsDTO> listaProdutosDTORetorno = service.buscarProdutos();

        assertEquals(listaProdutosDTO, listaProdutosDTORetorno);
        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verify(converter).toEntity(produtoDTO);
        verify(produtoService).salvarProdutos(produtoEntity);
        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(client, produtoService, converter);
    }

    @Test
    void testeBuscarProdutosENaoGravarCasoRetornoTrue() {

        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();

        ProductsDTO produtoDTO = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        listaProdutosDTO.add(produtoDTO);

        when(client.buscaListaProdutos()).thenReturn(listaProdutosDTO);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(true);

        ConflictException e = assertThrows(ConflictException.class, () -> service.buscarProdutos());
        System.out.println("Mensagem real da exceção: " + e.getMessage());

        assertThat(e.getMessage(), containsString("Produto já existente no banco de dados"));
        assertThat(e.getMessage(), containsString("Jaqueta Vermelha"));

        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verifyNoMoreInteractions(client, produtoService);
        verifyNoInteractions(converter);
    }

    @Test
    void testeGerarExceptionCasoErroAoBuscarProdutosViaClient() {

        when(client.buscaListaProdutos()).thenThrow(new RuntimeException("Erro ao buscar Lista de Produtos"));

        assertThrows(RuntimeException.class, () -> service.buscarProdutos());

        verify(client).buscaListaProdutos();
        verifyNoMoreInteractions(client);
        verifyNoInteractions(converter, produtoService);
    }
}
