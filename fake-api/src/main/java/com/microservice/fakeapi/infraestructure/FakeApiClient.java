package com.microservice.fakeapi.infraestructure;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "fake-api", url = "${fake-api.url:https://fakestoreapi.com}")
public interface FakeApiClient {
    @GetMapping("/products")
    List<ProductsDTO> buscaListaProdutos();
}
