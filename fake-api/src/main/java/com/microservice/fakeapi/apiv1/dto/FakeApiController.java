package com.microservice.fakeapi.apiv1.dto;

import com.microservice.fakeapi.business.service.FakeApiService;
import com.microservice.fakeapi.business.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "fake-api")
public class FakeApiController {

    private final FakeApiService service;
    private final ProdutoService produtoService;

    // http://localhost:8080/produtos/api
    @Operation(summary = "Buscar produtos da API Externa e salvar", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado e salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar os produtos"),
    })
    @PostMapping("/api")
    public ResponseEntity<List<ProductsDTO>> salvarProdutosAPI() {
        return ResponseEntity.ok(service.buscarProdutos());
    }

    // http://localhost:8080/produtos
    @Operation(summary = "Salvar novos produtos", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar os produtos"),
    })
    @PostMapping("/")
    public ResponseEntity<ProductsDTO> salvarProdutos(@RequestBody ProductsDTO dto) {
        return ResponseEntity.ok(produtoService.salvarProdutoDTO(dto));
    }

    // http://localhost:8080/produtos
    @Operation(summary = "Fazer atualização de novos produtos", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar os produtos"),
    })
    @PutMapping("/")
    public ResponseEntity<ProductsDTO> updateProdutos(@RequestParam("id") String id, @RequestBody ProductsDTO dto) {
        return ResponseEntity.ok(produtoService.updateProduto(id, dto));
    }

    // http://localhost:8080/produtos
    @Operation(summary = "Deleta produtos", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar os produtos"),
    })
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteProdutos(@RequestParam("nome") String nome) {
         produtoService.deletarProduto(nome);
         return ResponseEntity.accepted().build();
    }

    // http://localhost:8080/produtos
    @Operation(summary = "Buscar todos produtos cadastrados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os produtos"),
    })
    @GetMapping("/")
    public ResponseEntity<List<ProductsDTO>> buscaTodosProdutos() {
        return ResponseEntity.ok(produtoService.buscarTodosProdutos());
    }

    // http://localhost:8080/produtos
    @Operation(summary = "Buscar produtos cadastrados por nome", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado por nome com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os produtos"),
    })
    @GetMapping("/{nome}")
    public ResponseEntity<ProductsDTO> buscaProdutosPorNome(@PathVariable("nome") String nome) {
        return ResponseEntity.ok(produtoService.buscarProdutoPorNome(nome));
    }

}
