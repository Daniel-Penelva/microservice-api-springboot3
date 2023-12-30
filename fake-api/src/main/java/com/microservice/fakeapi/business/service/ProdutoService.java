package com.microservice.fakeapi.business.service;

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

    // Salva os produtos da API Externa no Banco de dados
    public ProdutoEntity salvarProdutos(ProdutoEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produtos" + e);
        }
    }

    // Listando os produtos da API externa
    public List<ProdutoEntity> buscarTodosProdutos() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os produtos" + e);
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
}
