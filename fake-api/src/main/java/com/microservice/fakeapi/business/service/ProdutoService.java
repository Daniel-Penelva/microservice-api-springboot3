package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import com.microservice.fakeapi.infraestructure.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoConverter converter;

    // Salva os produtos da API Externa no Banco de dados
    public ProdutoEntity salvarProdutos(ProdutoEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produtos" + e);
        }
    }

    // Salvar ProdutoDTO
    public ProductsDTO salvarProdutoDTO(ProductsDTO dto) {
        try {
            ProdutoEntity entity = converter.toEntity(dto);
            return converter.toDTO(repository.save(entity));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produtos" + e);
        }
    }

    // Buscar produto por nome
    public ProductsDTO buscarProdutoPorNome(String nome) {
        try {
            return converter.toDTO(repository.findByNome(nome));
        } catch (Exception e) {
            throw new RuntimeException(format("Erro ao buscar produto por nome", nome) + e);
        }
    }

    // Buscar todos os produtos DTOs
    public List<ProductsDTO>  buscarTodosProdutos() {
        try {
            return converter.toListDTO(repository.findAll());
        } catch (Exception e) {
            throw new RuntimeException(format("Erro ao buscar todos os produto por") + e);
        }
    }

    // Deletar produto por nome
    public void deletarProduto(String nome) {
        try {
            repository.deleteByNome(nome);
        } catch (Exception e) {
            throw new RuntimeException(format("Erro ao deletar produto por nome", nome) + e);
        }
    }

    // Verifica se já existe o nome do produto
    public Boolean existsPorNome(String nome) {
        try {
            return repository.existsByNome(nome);
        } catch (Exception e) {
            throw new RuntimeException(format("Erro ao buscar produto por nome", nome) + e);
        }
    }

    // Atualizar Produto
    public ProductsDTO updateProduto(String id, ProductsDTO dto) {
        try {
            ProdutoEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Id: " + id + "não existe no banco de dados!"));
            salvarProdutos(converter.toEntityUpdate(entity, dto, id));
            return converter.toDTO(repository.findByNome(entity.getNome()));
        } catch (Exception e) {
            throw new RuntimeException(format("Erro ao atualizar o produto"));
        }
    }
}
