package com.microservice.fakeapi.infraestructure.repositories;

import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
    Boolean existsByNome(String nome); // Verifica se já existir um produto com o mesmo nome

    ProdutoEntity findByNome(String nome); // buscar produto por nome

    void deleteByNome(String nome); // deletar produto por nome
}
