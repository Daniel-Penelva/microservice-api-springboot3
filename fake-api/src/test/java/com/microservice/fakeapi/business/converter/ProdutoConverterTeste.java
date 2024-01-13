package com.microservice.fakeapi.business.converter;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoConverterTeste {

    @InjectMocks
    ProdutoConverter converter;

    @Test
    void testeConverterParaProdutoEntityComSucesso(){

       ProductsDTO productsDTO = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntity = converter.toEntity(productsDTO);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());

        assertNotNull(produtoEntity.getId());
        assertNotNull(produtoEntity.getDataInclusao());
    }

    @Test
    void testeConverterParaProdutoEntityUpdateComSucesso(){

        ProductsDTO productsDTO = ProductsDTO.builder()
                //.nome("Jaqueta Vermelha")  não vou alterar o nome, logo não precisa passar
                //.categoria("Roupas") não vou alterar a categoria, logo não precisa passar
                .descricao("Jaqueta Vermelha com bolso e listas azuis")
                .preco(new BigDecimal(250.00))
                .build();

        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso e listas azuis")
                .preco(new BigDecimal(250.00))
                .build();

        String id = "12345";

        ProdutoEntity entity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntity = converter.toEntityUpdate(entity, productsDTO, id);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());
        assertEquals(produtoEntityEsperado.getId(), produtoEntity.getId());
        assertEquals(produtoEntityEsperado.getDataInclusao(), produtoEntity.getDataInclusao());

        assertNotNull(produtoEntity.getDataAtualizacao());
    }

    @Test
    void testeConverterParaProdutoDtoComSucesso(){

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO productsDTOEsperado = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO produtodto = converter.toDTO(produtoEntity);

        assertEquals(productsDTOEsperado.getNome(), produtoEntity.getNome());
        assertEquals(productsDTOEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(productsDTOEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(productsDTOEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(productsDTOEsperado.getImagem(), produtoEntity.getImagem());

        assertNull(productsDTOEsperado.getId());
        assertNotNull(produtoEntity.getId());
    }

    @Test
    void testeConverterParaListaProdutoDtoComSucesso(){

        List<ProductsDTO> listaProdutosDto = new ArrayList<>();
        List<ProdutoEntity> listaProdutosEntity = new ArrayList<>();

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO productsDTO = ProductsDTO.builder()
                .entityId("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        listaProdutosDto.add(productsDTO);
        listaProdutosEntity.add(produtoEntity);

        List<ProductsDTO> gerandoListaProdutoDto = converter.toListDTO(listaProdutosEntity);

        assertEquals(listaProdutosDto, gerandoListaProdutoDto);
    }
}
