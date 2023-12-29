package com.microservice.fakeapi.infraestructure.repositories;

import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
}
