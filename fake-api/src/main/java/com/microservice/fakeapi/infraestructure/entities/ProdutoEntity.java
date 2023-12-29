package com.microservice.fakeapi.infraestructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "ProdutoEntity")
@Table(name = "produtos")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class ProdutoEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String nome;

    @Column(name = "price")
    private BigDecimal preco;

    @Column(name = "category")
    private String categoria;

    @Column(name = "description")
    private String descricao;

    @Column(name = "image")
    private String imagem;
}
