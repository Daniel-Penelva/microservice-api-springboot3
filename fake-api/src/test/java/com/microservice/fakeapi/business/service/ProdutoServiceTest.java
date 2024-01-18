package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import com.microservice.fakeapi.infraestructure.exceptions.UnprocessableEntityException;
import com.microservice.fakeapi.infraestructure.message.producer.FakeApiProducer;
import com.microservice.fakeapi.infraestructure.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @InjectMocks
    ProdutoService produtoService;
    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    ProdutoConverter produtoConverter;
    @Mock
    FakeApiProducer fakeApiProducer;

    @Test
    void testSalvarProduto(){

        ProdutoEntity entityToSave = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configurando o comportamento dos mocks
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(entityToSave);

        // Execução do método que iremos testar
        ProdutoEntity savedEntity = produtoService.salvarProdutos(entityToSave);

        verify(produtoRepository, times(1)).save(any(ProdutoEntity.class)); // Verifica se o método save foi chamado uma vez
        assertNotNull(savedEntity);
        assertEquals("Jaqueta Vermelha", savedEntity.getNome()); // Verifica se o nome foi salvo corretamente

    }

    @Test
    void testSalvarProdutoDto(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productDtoToSaved.getNome())).thenReturn(false); // Produto não existe
        when(produtoConverter.toEntity(productDtoToSaved)).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia
        when(produtoConverter.toDTO(any(ProdutoEntity.class))).thenReturn(productDtoToSaved); // Pode retornar o mesmo DTO passado como argumento
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia

        // Execução do método que queremos testar
        ProductsDTO savedDto = produtoService.salvarProdutoDTO(productDtoToSaved);

        // Verificações
        verify(produtoConverter, times(1)).toEntity(productDtoToSaved); // Verifica se o método toEntity foi chamado uma vez
        verify(produtoConverter, times(1)).toDTO(any(ProdutoEntity.class)); // Verifica se o método toDTO foi chamado uma vez
        verify(produtoRepository, times(1)).save(any(ProdutoEntity.class)); // Verifica se o método save foi chamado uma vez

        assertNotNull(savedDto); // Verifica se o DTO retornado não é nulo
        assertEquals(productDtoToSaved, savedDto); // Verifica se o DTO retornado é o mesmo que foi passado como argumento
    }

    @Test
    void testSalvarProdutoDtoComProdutoExistente(){
        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productDtoToSaved.getNome())).thenReturn(true); // Produto já existente

        // Execução do método que vai ser testado
        assertThrows(ConflictException.class, () -> produtoService.salvarProdutoDTO(productDtoToSaved));

        // Verificações
        verify(produtoConverter, never()).toEntity(any()); // Verifica se o método toEntity não foi chamado
        verify(produtoRepository, never()).save(any()); // Verifica se o método save não foi chamado
    }

    @Test
    void testBuscarProdutoPorNome(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoRepository.findByNome(productDtoToSaved.getNome())).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia
        when(produtoConverter.toDTO(any(ProdutoEntity.class))).thenReturn(productDtoToSaved); // Pode retornar o mesmo DTO passado como argumento

        // Execução do método que vai ser testado
        ProductsDTO foundDto = produtoService.buscarProdutoPorNome(productDtoToSaved.getNome());

        // Verificações
        verify(produtoRepository, times(1)).findByNome(productDtoToSaved.getNome()); // Verifica se o método findByNome foi chamado uma vez
        verify(produtoConverter, times(1)).toDTO(any(ProdutoEntity.class)); // Verifica se o método toDTO foi chamado uma vez

        assertNotNull(foundDto); // Verifica se o DTO retornado não é nulo
        assertNotNull(productDtoToSaved.getNome()); // verifica que o nome não pode ser nulo
        assertEquals(productDtoToSaved, foundDto); // Verifica se o DTO retornado é o mesmo que foi passado como argumento
    }

    @Test
    void testBuscarProdutoPorNomeComProdutoNaoEncontrado(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoRepository.findByNome(productDtoToSaved.getNome())).thenReturn(null); // Produto não encontrado

        // Execução do método que vai testar a verificação da exceção
        assertThrows(UnprocessableEntityException.class, () -> produtoService.buscarProdutoPorNome(productDtoToSaved.getNome()));

        // Verificações
        verify(produtoRepository, times(1)).findByNome(productDtoToSaved.getNome()); // Verifica se o método findByNome foi chamado uma vez
        verify(produtoConverter, never()).toDTO(any(ProdutoEntity.class)); // Não deve chamar o método toDTO

    }

    @Test
    void testBuscarTodosProdutos(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Criação de uma lista de entidades simuladas
        List<ProdutoEntity> listaProdutosEntity = Arrays.asList(new ProdutoEntity());

        // Criação de uma lista de DTOs simulada
        List<ProductsDTO> listaProdutoDto = Arrays.asList(productDtoToSaved);

        // Configuração do comportamento dos mocks
        when(produtoRepository.findAll()).thenReturn(listaProdutosEntity); // Pode retornar uma lista de entidades simuladas
        when(produtoConverter.toListDTO(listaProdutosEntity)).thenReturn(listaProdutoDto);

        // Execução do método que vai ser testado
        List<ProductsDTO> foundDtoList = produtoService.buscarTodosProdutos();

        // Verificações
        verify(produtoRepository, times(1)).findAll(); // Verifica se o método findAll foi chamado uma vez
        verify(produtoConverter, times(1)).toListDTO(listaProdutosEntity); // Verifica se o método toListDTO foi chamado uma vez

        assertNotNull(foundDtoList);  // Verifica se a lista de DTOs retornada não é nula
        assertEquals(listaProdutoDto, foundDtoList); // Verifica se a lista de DTOs retornada é a mesma que foi simulada

    }

  @Test
    void testBuscarTodosProdutosComExcecao(){

        // Configuração do comportamento dos mocks para simular uma exceção
        when(produtoRepository.findAll()).thenThrow(new RuntimeException("Erro ao buscar produtos"));

        // Execução do método que vai testar e verificar a exceção
        assertThrows(BusinessException.class, () -> produtoService.buscarTodosProdutos());

        // Verificações
        verify(produtoRepository, times(1)).findAll(); // Verifica se o método findAll foi chamado uma vez
        verify(produtoConverter, never()).toListDTO(anyList()); // Não deve chamar o método toListDTO

    }

    @Test
    void testDeletarProduto(){

        // Criação de um ProductsDTO para ser salvo
        ProdutoEntity productEntityToSaved = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productEntityToSaved.getNome())).thenReturn(true);

        // Execução do método que queremos testar
        assertDoesNotThrow(() -> produtoService.deletarProduto(productEntityToSaved.getNome()));

        // Verificações
        verify(produtoRepository, times(1)).deleteByNome(productEntityToSaved.getNome()); // Verifica se o método deleteByNome foi chamado uma vez

    }

    @Test
    void testDeletarProdutoComProdutoInexistente() {

        // Criação de um ProductsDTO para ser salvo
        ProdutoEntity productEntityToSaved = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productEntityToSaved.getNome())).thenReturn(false);

        // Execução do método que vai testar e verificar da exceção
        UnprocessableEntityException excecao = assertThrows(UnprocessableEntityException.class, () -> produtoService.deletarProduto(productEntityToSaved.getNome()));

        // Verificações
        verify(produtoRepository, never()).deleteByNome(productEntityToSaved.getNome()); // Não deve chamar o método deleteByNome

        // Verifica se a mensagem da exceção é a esperada
        assertEquals("Não foi possível deletar o produto, pois não existe produto com o nome: " + productEntityToSaved.getNome(), excecao.getMessage());

    }

}
