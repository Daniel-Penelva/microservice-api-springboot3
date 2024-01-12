package com.microservice.fakeapi.apiV1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.fakeapi.apiv1.dto.FakeApiController;
import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.FakeApiService;
import com.microservice.fakeapi.business.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FakeApiControllerTest {

    @InjectMocks
    FakeApiController controller;
    @Mock
    FakeApiService fakeApiService;
    @Mock
    ProdutoService produtoService;
    private MockMvc mockMvc;
    private String json;
    private String url;
    private ProductsDTO productsDTO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws JsonProcessingException {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();

        url = "/produtos";

        productsDTO = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        json = objectMapper.writeValueAsString(productsDTO);
    }

    @Test
    void testeBuscarProdutosFakeApiESalvarComSucesso() throws Exception {

        when(fakeApiService.buscarProdutos()).thenReturn(Collections.singletonList(productsDTO));

        mockMvc.perform(post(url + "/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(fakeApiService).buscarProdutos();
        verifyNoMoreInteractions(fakeApiService);
    }

    @Test
    void testeSalvarProdutosDtoComSucesso() throws Exception {

        when(produtoService.salvarProdutoDTO(productsDTO)).thenReturn(productsDTO);

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).salvarProdutoDTO(productsDTO);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestCasoProdutoDtoSejaNull() throws Exception {

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeAtualizarProdutosDtoComSucesso() throws Exception {

        String id = "1245";

        when(produtoService.updateProduto(id, productsDTO)).thenReturn(productsDTO);

        mockMvc.perform(put(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).updateProduto(id, productsDTO);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestCasoIdSejaNull() throws Exception {

        mockMvc.perform(put(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeDeletarProdutosDtoComSucesso() throws Exception {

        String nome = "Jaqueta Vermelha";

        doNothing().when(produtoService).deletarProduto(nome);

        mockMvc.perform(delete(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("nome", nome)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()); // Vale ressaltar que o status que estou esperando é o isAccepted() de acordo como está implementado no método HTTP Delete deleteProdutos (FakeApiController).

        verify(produtoService).deletarProduto(nome);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestDeleteCasoNomeSejaNull() throws Exception {

        mockMvc.perform(delete(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeBuscarProdutosDtoPorNomeComSucesso() throws Exception {

        String nome = "Jaqueta Vermelha";

        when(produtoService.buscarProdutoPorNome(nome)).thenReturn(productsDTO);

        mockMvc.perform(get(url + "/" + nome)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Vale ressaltar que o status que estou esperando é o isOk() de acordo como está implementado no método HTTP Get buscaProdutosPorNome (FakeApiController).

        verify(produtoService).buscarProdutoPorNome(nome);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeBuscarTodosProdutosDtoComSucesso() throws Exception {

        List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeBuscarVerificandoSeExisteUmProdutoDtoComSucesso() throws Exception {

        List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Verificando se a resposta contém a lista com um produto

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }
}
