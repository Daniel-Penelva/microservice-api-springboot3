package com.microservice.fakeapi.infraestructure.repositories;

import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
    Boolean existsByNome(String nome); // Verifica se já existir um produto com o mesmo nome

    ProdutoEntity findByNome(String nome); // buscar produto por nome

    @Transactional // a anotação @Transactional é essencial ser add quando se tratar de delete e update para garantir que as operações sejam tratadas de forma atômica para evitar inconsistências nos dados.
    void deleteByNome(String nome); // deletar produto por nome
}
