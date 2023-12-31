package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.client.FakeApiClient;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FakeApiService {

    private final FakeApiClient cliente;
    private final ProdutoConverter converter;
    private final ProdutoService produtoService;

    public List<ProductsDTO> buscarProdutos() {

        try {
            List<ProductsDTO> dto = cliente.buscaListaProdutos();
            dto.forEach(produto -> {
                Boolean retorno = produtoService.existsPorNome(produto.getNome());
                if (retorno.equals(false)) {
                    produtoService.salvarProdutos(converter.toEntity(produto));
                } else {
                    throw new ConflictException("Produto já existente no banco de dados {}" + produto.getNome());
                }
            });
            return produtoService.buscarTodosProdutos();
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar e gravar produtos no banco de dados");
        }
    }
}
