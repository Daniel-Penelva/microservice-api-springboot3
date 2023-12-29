package com.microservice.fakeapi.business;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.infraestructure.FakeApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeApiService {

    private final FakeApiClient cliente;

    public List<ProductsDTO> buscarProdutos(){
        return cliente.buscaListaProdutos();
    }
}
