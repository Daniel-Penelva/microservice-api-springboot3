# Introdução

Os scripts fornecidos neste contexto estão destinados a serem utilizados em conjunto com uma API externa de terceiros, conhecida como Fake Store API. Esta API, acessível através do link [https://fakestoreapi.com/docs](https://fakestoreapi.com/docs), oferece uma simulação de uma loja online, disponibilizando endpoints que fornecem informações sobre produtos fictícios.

Os scripts abordam diferentes aspectos da integração com a Fake Store API, desde a comunicação com o serviço (cliente Feign) até a manipulação e persistência de dados no contexto de um microserviço. Além disso, há a exposição de endpoints através de um controlador que segue as práticas recomendadas do Swagger/OpenAPI para documentação de APIs.

A Fake Store API serve como uma fonte simulada de dados, facilitando o desenvolvimento, teste e aprendizado de integrações com APIs externas. Ao explorar esses scripts, é possível entender como lidar com a obtenção de dados, conversão de objetos, e a construção de serviços e controladores para oferecer funcionalidades baseadas nesses dados simulados.

Vale ressaltar que é bom se certificar de consultar a documentação oficial da Fake Store API [https://fakestoreapi.com/docs](https://fakestoreapi.com/docs) para obter informações detalhadas sobre os endpoints disponíveis, os tipos de dados retornados e quaisquer considerações específicas da API.

# DTO (Data Transfer Object) ProductsDTO

A classe `ProductsDTO` é um objeto de transferência de dados que representa informações sobre produtos na API versão 1 (APIv1) da Fake API. Este objeto é utilizado para a comunicação entre diferentes camadas da aplicação, especialmente entre o serviço que consome a Fake API e o restante do sistema.

```java
package com.microservice.fakeapi.apiv1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ProductsDTO {

    @JsonProperty(value = "id")
    @JsonIgnore // para o id não aparecer no response do corpo json
    private Long id;
    @JsonProperty(value = "entity_id")
    private String entityId;
    @JsonProperty(value = "title")
    private String nome;
    @JsonProperty(value = "price")
    private BigDecimal preco;
    @JsonProperty(value = "category")
    private String categoria;
    @JsonProperty(value = "description")
    private String descricao;
    @JsonProperty(value = "image")
    private String imagem;
}
```

## Atributos da Classe

### `id` (Long)

- **Descrição:** Identificador único do produto.
- **Anotações Jackson:** `@JsonProperty(value = "id")`
- **Restrições:** Pode ser nulo. O atributo é ignorado durante a serialização JSON através da anotação `@JsonIgnore`.

### `entityId` (String)

- **Descrição:** Identificador da entidade do produto.
- **Anotações Jackson:** `@JsonProperty(value = "entity_id")`

### `nome` (String)

- **Descrição:** Nome do produto.
- **Anotações Jackson:** `@JsonProperty(value = "title")`

### `preco` (BigDecimal)

- **Descrição:** Preço do produto.
- **Anotações Jackson:** `@JsonProperty(value = "price")`

### `categoria` (String)

- **Descrição:** Categoria à qual o produto pertence.
- **Anotações Jackson:** `@JsonProperty(value = "category")`

### `descricao` (String)

- **Descrição:** Descrição detalhada do produto.
- **Anotações Jackson:** `@JsonProperty(value = "description")`

### `imagem` (String)

- **Descrição:** URL da imagem associada ao produto.
- **Anotações Jackson:** `@JsonProperty(value = "image")`

## Anotações Lombok

A classe faz uso das anotações Lombok para reduzir a quantidade de código boilerplate. As anotações utilizadas incluem:

- `@Getter`: Gera automaticamente os métodos getters para todos os atributos.
- `@Setter`: Gera automaticamente os métodos setters para todos os atributos.
- `@AllArgsConstructor`: Gera um construtor que recebe todos os atributos como parâmetros.
- `@NoArgsConstructor`: Gera um construtor padrão sem parâmetros.
- `@Builder`: Gera um construtor de construção para facilitar a criação de instâncias da classe.
- `@EqualsAndHashCode`: Gera automaticamente os métodos `equals` e `hashCode` com base nos atributos da classe.

Essas anotações ajudam a manter o código limpo e legível, evitando a repetição de código comum.

# Interface FakeApiClient

A interface `FakeApiClient` define um cliente Feign para interagir com a Fake API, permitindo a busca de informações sobre produtos. Esta interface utiliza a biblioteca Spring Cloud OpenFeign para simplificar a comunicação com serviços HTTP.

```java
package com.microservice.fakeapi.infraestructure.client;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "fake-api", url = "${fake-api.url:https://fakestoreapi.com}")
public interface FakeApiClient {
    @GetMapping("/products")
    List<ProductsDTO> buscaListaProdutos();
}
```

## Anotações

### `@FeignClient`

- **Descrição:** Define esta interface como um cliente Feign para interação com serviços HTTP.
- **Atributos:**
    - `value`: Nome do cliente Feign.
    - `url`: URL base do serviço a ser consumido (padrão: "https://fakestoreapi.com").

## Métodos

### `buscaListaProdutos`

- **Descrição:** Realiza uma requisição HTTP GET para obter a lista de produtos da Fake API.
- **Anotações:** `@GetMapping("/products")`
- **Retorno:** Uma lista de objetos `ProductsDTO` representando informações sobre os produtos.

## Configurações Adicionais

### Propriedade `fake-api.url`

- **Descrição:** Esta propriedade é utilizada para configurar dinamicamente a URL da Fake API. Caso não seja especificada, o valor padrão é "https://fakestoreapi.com".

# Configuração application-dev.yaml (Parte 1 de desenvolvimento)

O script de configuração apresenta configurações relacionadas ao uso do Spring Cloud e à definição do servidor embutido. 

```yaml
spring:
  cloud:
    loadbalancer:
      ribbon:
        enable: true

server:
  port: 8181
```

Analisando cada parte do script:

```yaml
spring:
  cloud:
    loadbalancer:
      ribbon:
        enable: true
```

## Configuração do Spring Cloud Load Balancer

Esta parte do script está relacionada à configuração do Spring Cloud Load Balancer com o uso do Ribbon.

- **Descrição:** Habilita o uso do Ribbon como o balanceador de carga no contexto do Spring Cloud.
- **Atributos:**
    - `ribbon.enable`: Define como `true` para ativar o Ribbon.

```yaml
server:
  port: 8181
```

## Configuração do Servidor Embutido

Esta parte do script configura o servidor embutido fornecido pelo Spring Boot.

- **Descrição:** Define as propriedades relacionadas ao servidor embutido.
- **Atributos:**
    - `port`: Especifica o número da porta em que o servidor embutido será executado (neste caso, a porta 8181).

## Considerações Finais

Este script é relevante em ambientes onde é necessário configurar um balanceador de carga e definir propriedades específicas para o servidor embutido.

# Classe FakeApiService (Parte 1 de desenvolvimento)

A classe `FakeApiService` é um serviço responsável por interagir com a Fake API para buscar a lista de produtos. Utiliza o cliente Feign `FakeApiClient` para realizar a chamada à Fake API.

```java
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
```

## Atributos da Classe

### `cliente` (FakeApiClient)

- **Descrição:** Cliente Feign para interação com a Fake API e obtenção da lista de produtos.
- **Injeção de Dependência:** Realizada através do construtor, marcado com `@RequiredArgsConstructor`.

## Métodos

### `buscarProdutos`

- **Descrição:** Realiza uma chamada à Fake API para obter a lista de produtos.
- **Retorno:** Lista de objetos `ProductsDTO` representando as informações dos produtos na Fake API.

## Anotações

### `@Service`

- **Descrição:** Indica que a classe é um componente de serviço gerenciado pelo Spring.

### `@RequiredArgsConstructor`

- **Descrição:** Gera um construtor com todos os campos marcados como `final` ou `@NonNull`.

## Fluxo de Execução

1. Utiliza o cliente Feign (`cliente`) para realizar uma requisição à Fake API.
2. Retorna a lista de produtos obtida da Fake API.

# Classe FakeApiController (Parte 1 de desenvolvimento)

A classe `FakeApiController` é um controlador responsável por expor endpoints relacionados à API Fake. Utiliza a classe de serviço `FakeApiService` para buscar e fornecer informações sobre produtos.

```java
package com.microservice.fakeapi.apiv1.dto;

import com.microservice.fakeapi.business.FakeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "fake-api")
public class FakeApiController {

    private final FakeApiService service;

    @Operation(summary = "Busca todos os produtos", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar os produtos"),
    })
    @GetMapping("")
    public ResponseEntity<List<ProductsDTO>> buscarProdutos() {
        return ResponseEntity.ok(service.buscarProdutos());
    }

}
```

## Atributos da Classe

### `service` (FakeApiService)

- **Descrição:** Serviço que realiza a lógica de negócios para buscar produtos da Fake API.
- **Injeção de Dependência:** Realizada através do construtor, marcado com `@RequiredArgsConstructor`.

## Endpoints

### `GET /produtos`

- **Descrição:** Endpoint para buscar todos os produtos da Fake API.
- **Operação Swagger:** `@Operation(summary = "Busca todos os produtos", method = "GET")`
- **Respostas Swagger:**
    - **200 OK:** Retorna a lista de produtos com sucesso.
    - **500 Internal Server Error:** Indica um erro interno ao tentar buscar os produtos.

## Anotações

### `@RestController`

- **Descrição:** Indica que a classe é um controlador Spring MVC.

### `@RequestMapping("/produtos")`

- **Descrição:** Define o caminho base para todos os endpoints da classe como "/produtos".

### `@RequiredArgsConstructor`

- **Descrição:** Gera um construtor com todos os campos marcados como `final` ou `@NonNull`.

### `@Tag(name = "fake-api")`

- **Descrição:** Anotação Swagger que categoriza este controlador com a tag "fake-api".

### `@Operation`

- **Descrição:** Anotação Swagger que fornece informações sobre a operação do endpoint, como resumo, método HTTP, etc.

### `@ApiResponses`

- **Descrição:** Anotação Swagger que fornece informações sobre as possíveis respostas do endpoint, incluindo códigos de status HTTP e descrições.

## Exemplo de Uso

```java
// Exemplo de uso do endpoint em um controlador.
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final FakeApiController fakeApiController;

    @Autowired
    public ApiController(FakeApiController fakeApiController) {
        this.fakeApiController = fakeApiController;
    }

    @GetMapping("/produtos/listar")
    public ResponseEntity<List<ProductsDTO>> listarProdutos() {
        return fakeApiController.buscarProdutos();
    }
}
```

Neste exemplo, a classe `ApiController` utiliza o método `listarProdutos` para chamar o endpoint `GET /produtos` do controlador `FakeApiController` e obter a lista de produtos da Fake API. O resultado é então retornado como uma resposta HTTP.

# Entidade Produto - ProdutoEntity

A classe `ProdutoEntity` representa a entidade de produto em um sistema utilizando a API falsa (fake API). Essa entidade é mapeada para uma tabela no banco de dados, permitindo a persistência de informações relacionadas aos produtos.

```java
package com.microservice.fakeapi.infraestructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "ProdutoEntity")
@Table(name = "produtos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title", length = 800)
    private String nome;

    @Column(name = "price")
    private BigDecimal preco;

    @Column(name = "category", length = 800)
    private String categoria;

    @Column(name = "description", length = 800)
    private String descricao;

    @Column(name = "image", length = 800)
    private String imagem;

    @Column(name = "data_inclusao")
    private LocalDateTime dataInclusao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
```

## Atributos da Classe

### `id` (String)

- **Descrição:** Identificador único do produto.
- **Anotações JPA:** `@Id`, `@Column(name = "id")`
- **Restrições:** Não nulo.

### `nome` (String)

- **Descrição:** Nome do produto.
- **Anotações JPA:** `@Column(name = "title", length = 800)`
- **Restrições:** O comprimento máximo do nome é de 800 caracteres.

### `preco` (BigDecimal)

- **Descrição:** Preço do produto.
- **Anotações JPA:** `@Column(name = "price")`
- **Restrições:** Não nulo.

### `categoria` (String)

- **Descrição:** Categoria à qual o produto pertence.
- **Anotações JPA:** `@Column(name = "category", length = 800)`
- **Restrições:** O comprimento máximo da categoria é de 800 caracteres.

### `descricao` (String)

- **Descrição:** Descrição detalhada do produto.
- **Anotações JPA:** `@Column(name = "description", length = 800)`
- **Restrições:** O comprimento máximo da descrição é de 800 caracteres.

### `imagem` (String)

- **Descrição:** URL da imagem associada ao produto.
- **Anotações JPA:** `@Column(name = "image", length = 800)`
- **Restrições:** O comprimento máximo da URL da imagem é de 800 caracteres.

### `dataInclusao` (LocalDateTime)

- **Descrição:** Data e hora da inclusão do produto no sistema.
- **Anotações JPA:** `@Column(name = "data_inclusao")`
- **Restrições:** Não nulo.

### `dataAtualizacao` (LocalDateTime)

- **Descrição:** Data e hora da última atualização das informações do produto.
- **Anotações JPA:** `@Column(name = "data_atualizacao")`
- **Restrições:** Não nulo.

## Anotações Lombok

A classe faz uso das anotações Lombok para reduzir a quantidade de código boilerplate. As anotações utilizadas incluem:

- `@Getter`: Gera automaticamente os métodos getters para todos os atributos.
- `@Setter`: Gera automaticamente os métodos setters para todos os atributos.
- `@AllArgsConstructor`: Gera um construtor que recebe todos os atributos como parâmetros.
- `@NoArgsConstructor`: Gera um construtor padrão sem parâmetros.
- `@Builder`: Gera um construtor de construção para facilitar a criação de instâncias da classe.

Essas anotações ajudam a manter o código limpo e legível, evitando a repetição de código comum.

# Repositório de Produto - ProdutoRepository

O script fornecido define uma interface de repositório para a entidade `ProdutoEntity`. Este repositório utiliza o Spring Data JPA, proporcionando operações de persistência e consulta relacionadas aos produtos no banco de dados. Abaixo está a documentação detalhada do `ProdutoRepository`.

```java
package com.microservice.fakeapi.infraestructure.repositories;

import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {

    /**
     * Verifica se já existe um produto com o mesmo nome.
     *
     * @param nome O nome do produto a ser verificado.
     * @return `true` se um produto com o mesmo nome existe, `false` caso contrário.
     */
    Boolean existsByNome(String nome);

    /**
     * Busca um produto pelo nome.
     *
     * @param nome O nome do produto a ser buscado.
     * @return Uma instância de `ProdutoEntity` representando o produto encontrado.
     */
    ProdutoEntity findByNome(String nome);

    /**
     * Deleta um produto pelo nome.
     * 
     * @param nome O nome do produto a ser deletado.
     */
    @Transactional
    void deleteByNome(String nome);
}
```

## Métodos

### `existsByNome`

- **Descrição:** Verifica se já existe um produto com o mesmo nome no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser verificado.
- **Retorno:** `true` se um produto com o mesmo nome existe, `false` caso contrário.

### `findByNome`

- **Descrição:** Busca um produto pelo nome no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser buscado.
- **Retorno:** Uma instância de `ProdutoEntity` representando o produto encontrado.

### `deleteByNome`

- **Descrição:** Deleta um produto pelo nome no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser deletado.

## Anotações

### `@Repository`

- **Descrição:** Indica que a interface é um componente de repositório gerenciado pelo Spring.

### `@Transactional`

- **Descrição:** A anotação é essencial para operações que envolvem delete e update. Garante que as operações sejam tratadas de forma atômica, evitando inconsistências nos dados.

# Conversor de Produto

O script apresenta um componente `ProdutoConverter` que realiza a conversão entre objetos DTO (Data Transfer Object) e entidades `ProdutoEntity`. Este componente é responsável por facilitar a transição de dados entre as camadas do sistema, especialmente entre o serviço e o repositório.

```java
package com.microservice.fakeapi.business.converter;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ProdutoConverter {

    /**
     * Converte um objeto DTO para uma entidade.
     *
     * @param dto O objeto DTO a ser convertido.
     * @return Uma instância de ProdutoEntity.
     */
    public ProdutoEntity toEntity(ProductsDTO dto) {
        return ProdutoEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .imagem(dto.getImagem())
                .dataInclusao(LocalDateTime.now())
                .build();
    }

    /**
     * Converte um objeto DTO para uma entidade para ação de atualização.
     *
     * @param entity A entidade existente antes da atualização.
     * @param dto O objeto DTO a ser convertido para ação de atualização.
     * @param id O identificador único da entidade.
     * @return Uma instância de ProdutoEntity atualizada.
     */
    public ProdutoEntity toEntityUpdate(ProdutoEntity entity, ProductsDTO dto, String id) {
        return ProdutoEntity.builder()
                .id(id)
                .nome(dto.getNome() != null ? dto.getNome() : entity.getNome())
                .categoria(dto.getCategoria() != null ? dto.getCategoria() : entity.getCategoria())
                .descricao(dto.getDescricao() != null ? dto.getDescricao() : entity.getDescricao())
                .preco(dto.getPreco() != null ? dto.getPreco() : entity.getPreco())
                .imagem(dto.getImagem() != null ? dto.getImagem() : entity.getImagem())
                .dataInclusao(entity.getDataInclusao())
                .dataAtualizacao(LocalDateTime.now())
                .build();
    }

    /**
     * Converte uma entidade para um objeto DTO.
     *
     * @param entity A entidade a ser convertida.
     * @return Uma instância de ProductsDTO.
     */
    public ProductsDTO toDTO(ProdutoEntity entity) {
        return ProductsDTO.builder()
                .entityId(entity.getId())
                .nome(entity.getNome())
                .categoria(entity.getCategoria())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .imagem(entity.getImagem())
                .build();
    }

    /**
     * Converte uma lista de entidades para uma lista de objetos DTO.
     *
     * @param entityList A lista de entidades a ser convertida.
     * @return Uma lista de ProductsDTO.
     */
    public List<ProductsDTO> toListDTO(List<ProdutoEntity> entityList) {
       return entityList.stream().map(this::toDTO).toList();
    }
}
```

## Métodos

### `toEntity`

- **Descrição:** Converte um objeto DTO para uma entidade `ProdutoEntity`.
- **Parâmetros:**
    - `dto`: O objeto DTO a ser convertido.
- **Retorno:** Uma instância de `ProdutoEntity`.

### `toEntityUpdate`

- **Descrição:** Converte um objeto DTO para uma entidade `ProdutoEntity` para ação de atualização.
- **Parâmetros:**
    - `entity`: A entidade existente antes da atualização.
    - `dto`: O objeto DTO a ser convertido para ação de atualização.
    - `id`: O identificador único da entidade.
- **Retorno:** Uma instância de `ProdutoEntity` atualizada.

### `toDTO`

- **Descrição:** Converte uma entidade `ProdutoEntity` para um objeto DTO `ProductsDTO`.
- **Parâmetros:**
    - `entity`: A entidade a ser convertida.
- **Retorno:** Uma instância de `ProductsDTO`.

### `toListDTO`

- **Descrição:** Converte uma lista de entidades `ProdutoEntity` para uma lista de objetos DTO `ProductsDTO`.
- **Parâmetros:**
    - `entityList`: A lista de entidades a ser convertida.
- **Retorno:** Uma lista de `ProductsDTO`.

## Anotações

### `@Component`

- **Descrição:** Indica que a classe é um componente gerenciado pelo Spring.

Este componente `ProdutoConverter` desempenha um papel fundamental na integração entre as camadas do sistema, tornando mais fácil a manipulação e transformação de dados relacionados a produtos. Ele fornece métodos para converter objetos DTO em entidades e vice-versa, simplificando o processo de manipulação de dados dentro do microserviço.

# Serviço FakeApiService (Parte 2 desenvolvimento - refatorando)

O script apresenta a classe `FakeApiService`, um serviço responsável por integrar-se à Fake API, converter dados e gerenciar a persistência de produtos no banco de dados local. A classe utiliza o Feign Client (`FakeApiClient`), o conversor (`ProdutoConverter`) e o serviço de produto (`ProdutoService`) para executar essas operações.

```java
package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.client.FakeApiClient;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeApiService {

    private final FakeApiClient cliente;
    private final ProdutoConverter converter;
    private final ProdutoService produtoService;

    /**
     * Busca produtos na Fake API, converte para entidades e persiste no banco de dados.
     *
     * @return Lista de objetos ProductsDTO representando os produtos no banco de dados.
     * @throws ConflictException Se um produto já existente for encontrado no banco de dados.
     * @throws RuntimeException Se ocorrer um erro durante a busca e persistência de produtos.
     */
    public List<ProductsDTO> buscarProdutos() {

        try {
            // Obtém a lista de produtos da Fake API
            List<ProductsDTO> dto = cliente.buscaListaProdutos();

            // Para cada produto no DTO, verifica se já existe no banco de dados
            dto.forEach(produto -> {
                Boolean retorno = produtoService.existsPorNome(produto.getNome());

                // Se o produto não existe, converte e persiste no banco de dados
                if (retorno.equals(false)) {
                    produtoService.salvarProdutos(converter.toEntity(produto));
                } else {
                    // Se o produto já existe, lança uma exceção de conflito
                    throw new ConflictException("Produto já existente no banco de dados: " + produto.getNome());
                }
            });

            // Retorna a lista completa de produtos no banco de dados
            return produtoService.buscarTodosProdutos();

        } catch (ConflictException e) {
            // Captura e relança exceção de conflito
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            // Captura e relança exceção genérica em caso de erro
            throw new RuntimeException("Erro ao buscar e gravar produtos no banco de dados", e);
        }
    }
}
```

## Métodos

### `buscarProdutos`

- **Descrição:** Busca a lista de produtos na Fake API, converte para entidades e persiste no banco de dados.
- **Retorno:** Lista de objetos `ProductsDTO` representando os produtos no banco de dados.
- **Exceções:**
    - `ConflictException`: Lançada se um produto já existente for encontrado no banco de dados.
    - `RuntimeException`: Lançada em caso de erro durante a busca e persistência de produtos.

## Anotações

### `@Service`

- **Descrição:** Indica que a classe é um componente de serviço gerenciado pelo Spring.

### `@RequiredArgsConstructor`

- **Descrição:** Gera um construtor com todos os campos marcados como `final` ou `@NonNull`.

## Fluxo de Execução

1. Utiliza o Feign Client (`cliente`) para obter a lista de produtos da Fake API.
2. Para cada produto na lista, verifica se já existe no banco de dados usando o serviço de produto (`produtoService`).
3. Se o produto não existe, converte o objeto `ProductsDTO` em uma entidade `ProdutoEntity` usando o conversor (`converter`) e o persiste no banco de dados usando o serviço de produto (`produtoService`).
4. Se o produto já existe, lança uma `ConflictException`.
5. Retorna a lista completa de produtos no banco de dados.

# Serviço de Produto - ProdutoService

O script apresenta a classe `ProdutoService`, um serviço que gerencia a lógica de negócios relacionada aos produtos, incluindo a interação com o banco de dados, validações e operações específicas. Este serviço é crucial para a funcionalidade do microserviço, oferecendo métodos para salvar, buscar, deletar e atualizar produtos.

```java
package com.microservice.fakeapi.business.service;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.converter.ProdutoConverter;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import com.microservice.fakeapi.infraestructure.exceptions.UnprocessableEntityException;
import com.microservice.fakeapi.infraestructure.message.producer.FakeApiProducer;
import com.microservice.fakeapi.infraestructure.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoConverter converter;
    private final FakeApiProducer producer;

    /**
     * Salva uma entidade de produto no banco de dados.
     *
     * @param entity A entidade de produto a ser salva.
     * @return A entidade de produto salva.
     * @throws BusinessException Se ocorrer um erro ao salvar os produtos.
     */
    public ProdutoEntity salvarProdutos(ProdutoEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao salvar produtos" + e);
        }
    }

    /**
     * Salva um objeto DTO de produto no banco de dados.
     *
     * @param dto O objeto DTO de produto a ser salvo.
     * @return O objeto DTO de produto salvo.
     * @throws ConflictException Se um produto já existente for encontrado no banco de dados.
     * @throws BusinessException Se ocorrer um erro ao salvar os produtos.
     */
    public ProductsDTO salvarProdutoDTO(ProductsDTO dto) {
        try {
            Boolean retorno = existsPorNome(dto.getNome());
            if (retorno.equals(true)) {
                throw new ConflictException("Produto já existente no banco de dados" + dto.getNome());
            }
            ProdutoEntity entity = converter.toEntity(dto);
            return converter.toDTO(repository.save(entity));
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao salvar produtos" + e);
        }
    }

    /**
     * Salva um objeto DTO de produto e envia uma resposta via mensagem.
     *
     * @param dto O objeto DTO de produto a ser salvo.
     * @throws ConflictException Se um produto já existente for encontrado no banco de dados.
     * @throws BusinessException Se ocorrer um erro ao salvar os produtos.
     */
    public void salvaProdutoConsumer(ProductsDTO dto) {
        try {
            Boolean retorno = existsPorNome(dto.getNome());
            if (retorno.equals(true)) {
                producer.enviaRespostaCadastroProdutos("Produto " + dto.getNome() + " já existente no banco de dados.");
                throw new ConflictException("Produto já existente no banco de dados " + dto.getNome());
            }
            repository.save(converter.toEntity(dto));
            producer.enviaRespostaCadastroProdutos("Produto " + dto.getNome() + " gravado com sucesso.");
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            producer.enviaRespostaCadastroProdutos("Erro ao gravar o produto " + dto.getNome());
            throw new BusinessException("Erro ao salvar Produtos" + e);
        }
    }

    /**
     * Busca um produto pelo nome no banco de dados.
     *
     * @param nome O nome do produto a ser buscado.
     * @return O objeto DTO de produto encontrado.
     * @throws UnprocessableEntityException Se não forem encontrados produtos com o nome especificado.
     * @throws BusinessException Se ocorrer um erro ao buscar o produto por nome.
     */
    public ProductsDTO buscarProdutoPorNome(String nome) {
        try {
            ProdutoEntity produto = repository.findByNome(nome);
            if (Objects.isNull(produto)) {
                throw new UnprocessableEntityException("Não foram encontrados produtos com o nome: " + nome);
            }
            return converter.toDTO(produto);
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao buscar produto por nome = %s ", nome) + e);
        }
    }

    /**
     * Busca todos os produtos no banco de dados.
     *
     * @return Lista de objetos DTO de produtos.
     * @throws BusinessException Se ocorrer um erro ao buscar todos os produtos.
     */
    public List<ProductsDTO> buscarTodosProdutos() {
        try {
            return converter.toListDTO(repository.findAll());
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar todos os produto por {}", e);
        }
    }

    /**
     * Deleta um produto pelo nome no banco de dados.
     *
     * @param nome O nome do produto a ser deletado.
     * @throws UnprocessableEntityException Se não for possível deletar o produto devido à sua inexistência.
     * @throws BusinessException Se ocorrer um erro ao deletar o produto por nome.
     */
    public void deletarProduto(String nome) {
        try {
            Boolean retorno = existsPorNome(nome);
            if (retorno.equals(false)) {
                throw new UnprocessableEntityException("Não foi possível deletar o produto, pois não existe produto com o nome: " + nome);
            } else {
                repository.deleteByNome(nome);
            }
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao deletar produto por nome = %s ", nome) + e);
        }
    }

    /**
     * Verifica se já existe um produto com o nome especificado no banco de dados.
     *
     * @param nome O nome do produto a ser verificado.
     * @return `true` se o produto existe, `false` se não existe.
     * @throws BusinessException Se ocorrer um erro ao verificar a existência do produto por nome.
     */
    public Boolean existsPorNome(String nome) {
        try {
            return repository.existsByNome(nome);
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao buscar produto por nome = %s ", nome) + e);
        }
    }

    /**
     * Atualiza um produto no banco de dados.
     *
     * @param id O identificador único do produto a ser atualizado.
     * @param dto O objeto DTO de produto contendo os dados atualizados.
     * @return O objeto DTO de produto atualizado.
     * @throws UnprocessableEntityException Se o produto não for encontrado na base de dados.
     * @throws BusinessException Se ocorrer um erro ao atualizar o produto.
     */
    public ProductsDTO updateProduto(String id, ProductsDTO dto) {
        try {
            ProdutoEntity entity = repository.findById(id).orElseThrow(() -> new UnprocessableEntityException("Produto não encontrado na base de dados"));
            salvarProdutos(converter.toEntityUpdate(entity, dto, id));
            return converter.toDTO(repository.findByNome(entity.getNome()));
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao atualizar o produto", e);
        }
    }
}
```

## Métodos

A classe `ProdutoService` oferece os seguintes métodos:

### `salvarProdutos`

- **Descrição:** Salva uma entidade de produto no banco de dados.
- **Parâmetros:**
    - `entity`: A entidade de produto a ser salva.
- **Retorno:** A entidade de produto salva.

### `salvarProdutoDTO`

- **Descrição:** Salva um objeto DTO de produto no banco de dados.
- **Parâmetros:**
    - `dto`: O objeto DTO de produto a ser salvo.
- **Retorno:** O objeto DTO de produto salvo.

### `salvaProdutoConsumer`

- **Descrição:** Salva um objeto DTO de produto e envia uma resposta via mensagem.
- **Parâmetros:**
    - `dto`: O objeto DTO de produto a ser salvo.

### `buscarProdutoPorNome`

- **Descrição:** Busca um produto pelo nome no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser buscado.
- **Retorno:** O objeto DTO de produto encontrado.

### `buscarTodosProdutos`

- **Descrição:** Busca todos os produtos no banco de dados.
- **Retorno:** Lista de objetos DTO de produtos.

### `deletarProduto`

- **Descrição:** Deleta um produto pelo nome no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser deletado.

### `existsPorNome`

- **Descrição:** Verifica se já existe um produto com o nome especificado no banco de dados.
- **Parâmetros:**
    - `nome`: O nome do produto a ser verificado.
- **Retorno:** `true` se o produto existe, `false` se não existe.

### `updateProduto`

- **Descrição:** Atualiza um produto no banco de dados.
- **Parâmetros:**
    - `id`: O identificador único do produto a ser atualizado.
    - `dto`: O objeto DTO de produto contendo os dados atualizados.
- **Retorno:** O objeto DTO de produto atualizado.

## Anotações

### `@Service`

- **Descrição:** Indica que a classe é um componente de serviço gerenciado pelo Spring.

### `@RequiredArgsConstructor`

- **Descrição:** Gera um construtor com todos os campos marcados como `final` ou `@NonNull`.

## Fluxo de Execução

1. Os métodos `salvarProdutoDTO` e `salvaProdutoConsumer` verificam se o produto já existe no banco de dados antes de salvá-lo.
2. Em caso afirmativo, uma exceção de `ConflictException` é lançada.
3. Caso contrário, o produto é convertido para uma entidade e persistido no banco de dados.
4. O método `salvaProdutoConsumer` envia uma resposta via mensagem após a conclusão da operação.
5. Os métodos de busca (`buscarProdutoPorNome` e `buscarTodosProdutos`) e deleção (`deletarProduto`) realizam operações específicas no banco de dados.
6. O método `existsPorNome` verifica a existência de um produto com o nome especificado.
7. O método `updateProduto` atualiza um produto existente no banco de dados.

# Classe FakeApiController (Parte 2 de desenvolvimento - refatorando)

O script apresenta a classe `FakeApiController`, um controlador REST que expõe endpoints para interagir com o serviço de produtos. Este controlador lida com operações como buscar, salvar, atualizar e deletar produtos, fazendo chamadas aos serviços `FakeApiService` e `ProdutoService`. As operações são documentadas usando anotações Swagger para facilitar a compreensão e integração com ferramentas de documentação.

## FakeApiController

```java
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

    /**
     * Busca produtos da API Externa e salva no banco de dados.
     *
     * @return Lista de objetos DTO de produtos salvos.
     */
    @Operation(summary = "Buscar produtos da API Externa e salvar", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado e salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar os produtos"),
    })
    @PostMapping("/api")
    public ResponseEntity<List<ProductsDTO>> salvarProdutosAPI() {
        return ResponseEntity.ok(service.buscarProdutos());
    }

    /**
     * Salva novos produtos no banco de dados.
     *
     * @param dto O objeto DTO de produto a ser salvo.
     * @return O objeto DTO de produto salvo.
     */
    @Operation(summary = "Salvar novos produtos", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar os produtos"),
    })
    @PostMapping("/")
    public ResponseEntity<ProductsDTO> salvarProdutos(@RequestBody ProductsDTO dto) {
        return ResponseEntity.ok(produtoService.salvarProdutoDTO(dto));
    }

    /**
     * Atualiza novos produtos no banco de dados.
     *
     * @param id  O identificador único do produto a ser atualizado.
     * @param dto O objeto DTO de produto contendo os dados atualizados.
     * @return O objeto DTO de produto atualizado.
     */
    @Operation(summary = "Fazer atualização de novos produtos", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar os produtos"),
    })
    @PutMapping("/")
    public ResponseEntity<ProductsDTO> updateProdutos(@RequestParam("id") String id, @RequestBody ProductsDTO dto) {
        return ResponseEntity.ok(produtoService.updateProduto(id, dto));
    }

    /**
     * Deleta produtos do banco de dados.
     *
     * @param nome O nome do produto a ser deletado.
     * @return Resposta HTTP indicando sucesso na deleção.
     */
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

    /**
     * Busca todos os produtos cadastrados no banco de dados.
     *
     * @return Lista de objetos DTO de produtos.
     */
    @Operation(summary = "Buscar todos produtos cadastrados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os produtos"),
    })
    @GetMapping("/")
    public ResponseEntity<List<ProductsDTO>> buscaTodosProdutos() {
        return ResponseEntity.ok(produtoService.buscarTodosProdutos());
    }

    /**
     * Busca produtos cadastrados no banco de dados por nome.
     *
     * @param nome O nome do produto a ser buscado.
     * @return O objeto DTO de produto encontrado.
     */
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
```

## Métodos

A classe `FakeApiController` oferece os seguintes endpoints:

### `salvarProdutosAPI`

- **Descrição:** Busca produtos da API externa e salva no banco de dados.
- **Método HTTP:** POST
- **URL:** /produtos/api
- **Retorno:** Lista de objetos DTO de produtos salvos.

### `salvarProdutos`

- **Descrição:** Salva novos produtos no banco de dados.
- **Método HTTP:** POST
- **URL:** /produtos/
- **Parâmetros de Requisição:** Body contendo o objeto DTO de produto.
- **Retorno:** Objeto DTO de produto salvo.

### `updateProdutos`

- **Descri

ção:** Atualiza novos produtos no banco de dados.
- **Método HTTP:** PUT
- **URL:** /produtos/
- **Parâmetros de Requisição:** `id` (identificador único do produto) e Body contendo o objeto DTO de produto atualizado.
- **Retorno:** Objeto DTO de produto atualizado.

### `deleteProdutos`

- **Descrição:** Deleta produtos do banco de dados.
- **Método HTTP:** DELETE
- **URL:** /produtos/
- **Parâmetros de Requisição:** `nome` (nome do produto a ser deletado).
- **Retorno:** Resposta HTTP indicando sucesso na deleção.

### `buscaTodosProdutos`

- **Descrição:** Busca todos os produtos cadastrados no banco de dados.
- **Método HTTP:** GET
- **URL:** /produtos/
- **Retorno:** Lista de objetos DTO de produtos.

### `buscaProdutosPorNome`

- **Descrição:** Busca produtos cadastrados no banco de dados por nome.
- **Método HTTP:** GET
- **URL:** /produtos/{nome}
- **Parâmetros de Requisição:** `nome` (nome do produto a ser buscado).
- **Retorno:** Objeto DTO de produto encontrado.

## Anotações

### `@RestController`

- **Descrição:** Indica que a classe é um controlador REST.

### `@RequestMapping("/produtos")`

- **Descrição:** Define o mapeamento base para todas as URLs relacionadas a produtos neste controlador.

### `@RequiredArgsConstructor`

- **Descrição:** Gera um construtor com todos os campos marcados como `final` ou `@NonNull`.

### `@Tag(name = "fake-api")`

- **Descrição:** Especifica uma tag para este controlador na documentação Swagger.

### `@Operation`

- **Descrição:** Especifica detalhes da operação para a documentação Swagger, como resumo, método HTTP e outros detalhes.

### `@ApiResponses`

- **Descrição:** Especifica as respostas HTTP possíveis para a documentação Swagger.

### `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@GetMapping`

- **Descrição:** Especifica o mapeamento do método para os diferentes métodos HTTP.

### `@RequestBody`

- **Descrição:** Indica que o parâmetro do método deve ser extraído do corpo da solicitação HTTP.

### `@RequestParam`

- **Descrição:** Indica que o parâmetro do método deve ser extraído dos parâmetros da solicitação HTTP.

### `@PathVariable`

- **Descrição:** Indica que o parâmetro do método deve ser extraído de uma variável da URL.

## Fluxo de Execução

1. **Salvar Produtos da API Externa:**
    - O endpoint `/produtos/api` dispara a operação `salvarProdutosAPI` no serviço `FakeApiService`.
    - A lista de produtos buscada da API externa é salva no banco de dados.
    - A resposta HTTP contendo a lista de produtos salvos é retornada.

2. **Salvar Novos Produtos:**
    - O endpoint `/produtos/` dispara a operação `salvarProdutos` no serviço `ProdutoService`.
    - O produto recebido no corpo da solicitação é salvo no banco de dados.
    - A resposta HTTP contendo o produto salvo é retornada.

3. **Atualizar Novos Produtos:**
    - O endpoint `/produtos/` dispara a operação `updateProdutos` no serviço `ProdutoService`.
    - O produto identificado pelo `id` é atualizado com os dados fornecidos no corpo da solicitação.
    - A resposta HTTP contendo o produto atualizado é retornada.

4. **Deletar Produtos:**
    - O endpoint `/produtos/` dispara a operação `deleteProdutos` no serviço `ProdutoService`.
    - O produto identificado pelo `nome` é removido do banco de dados.
    - Uma resposta HTTP indicando sucesso na deleção é retornada.

5. **Buscar Todos os Produtos:**
    - O endpoint `/produtos/` dispara a operação `buscaTodosProdutos` no serviço `ProdutoService`.
    - Todos os produtos cadastrados no banco de dados são retornados como uma lista de objetos DTO.

6. **Buscar Produtos por Nome:**
    - O endpoint `/produtos/{nome}` dispara a operação `buscaProdutosPorNome` no serviço `ProdutoService`.
    - O produto identificado pelo `nome` é retornado como um objeto DTO.

## Exemplo de Uso

Para interagir com os métodos HTTP do controlador de produtos, você pode utilizar o Swagger, que oferece uma interface amigável para testar e documentar APIs. O Swagger está disponível no seguinte link: [http://localhost:8181/swagger-ui/index.html](http://localhost:8181/swagger-ui/index.html).

Acesse esse link em seu navegador para visualizar a documentação interativa gerada automaticamente para a API. Lá, você poderá explorar os diferentes endpoints, visualizar detalhes sobre cada operação, fornecer entradas para os parâmetros e executar as operações diretamente na interface do Swagger.

A documentação Swagger facilita a compreensão dos endpoints, seus métodos e os parâmetros esperados, tornando a interação com a API mais intuitiva e amigável.


# Exceções

Este conjunto de classes compõe o pacote `com.microservice.fakeapi.infraestructure.exceptions`, fornecendo exceções personalizadas para lidar com situações específicas no contexto da aplicação.

## `BusinessException`

```java
package com.microservice.fakeapi.infraestructure.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }
}

```

### Descrição
Esta exceção é lançada quando ocorre um erro relacionado a regras de negócios na aplicação.

### Construtores

1. **`public BusinessException(String message)`**
    - Descrição: Construtor que aceita uma mensagem para descrever a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.

2. **`public BusinessException(String message, Throwable cause)`**
    - Descrição: Construtor que aceita uma mensagem e uma causa para a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.
        - `cause` (Throwable): Causa original da exceção.

## `ConflictException`

```java
package com.microservice.fakeapi.infraestructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException{

    public ConflictException(String message){
        super(message);
    }

    public ConflictException(String message, Throwable cause){
        super(message, cause);
    }
}
```

### Descrição
Esta exceção é lançada quando ocorre um conflito na aplicação, geralmente associado a operações de dados duplicadas ou conflitantes.

### Construtores

1. **`public ConflictException(String message)`**
    - Descrição: Construtor que aceita uma mensagem para descrever a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.

2. **`public ConflictException(String message, Throwable cause)`**
    - Descrição: Construtor que aceita uma mensagem e uma causa para a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.
        - `cause` (Throwable): Causa original da exceção.

## `UnprocessableEntityException`

```java
package com.microservice.fakeapi.infraestructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

}
```

### Descrição
Esta exceção é lançada quando ocorre uma tentativa de processar uma entidade que não pode ser processada corretamente.

### Construtores

1. **`public UnprocessableEntityException(String message)`**
    - Descrição: Construtor que aceita uma mensagem para descrever a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.

2. **`public UnprocessableEntityException(String message, Throwable cause)`**
    - Descrição: Construtor que aceita uma mensagem e uma causa para a exceção.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.
        - `cause` (Throwable): Causa original da exceção.

## Anotações

### `@ResponseStatus`

- **Descrição:** Essa anotação é usada para associar a exceção a um status HTTP específico, definido pelo valor do código de status na anotação. Isso pode ser útil para comunicar ao cliente da API sobre o estado da exceção.

### `HttpStatus`

- **Descrição:** Enumeração que representa os códigos de status HTTP. Utilizada em conjunto com a anotação `@ResponseStatus` para especificar o código de status associado a uma exceção.

# Exception Handler

O pacote `com.microservice.fakeapi.infraestructure.exceptions.handler` contém classes relacionadas ao tratamento de exceções na aplicação, fornecendo respostas personalizadas em casos de erros.

## `ErrorResponse`

```java
package com.microservice.fakeapi.infraestructure.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String messagem;
    private LocalDateTime data;
    private int status;
    private String path;
}
```

### Descrição
Esta classe representa a estrutura de dados para as respostas de erro padrão geradas pelo Exception Handler. Contém informações sobre a mensagem de erro, data e hora, status HTTP e o caminho da requisição que gerou o erro.

### Campos

1. **`messagem` (String):**
    - Descrição: Mensagem detalhando o motivo da exceção.

2. **`data` (LocalDateTime):**
    - Descrição: Data e hora em que ocorreu a exceção.

3. **`status` (int):**
    - Descrição: Código de status HTTP associado à exceção.

4. **`path` (String):**
    - Descrição: Caminho da requisição que gerou a exceção.

## `GlobalExceptionHandler`

```java
package com.microservice.fakeapi.infraestructure.exceptions.handler;

import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.exceptions.ConflictException;
import com.microservice.fakeapi.infraestructure.exceptions.UnprocessableEntityException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> response(final String message, final HttpServletRequest request, final HttpStatus status, LocalDateTime data) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, data, status.value(), request.getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handlerBusinessException(BusinessException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> handlerUnprocessableEntityException(UnprocessableEntityException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handlerConflictException(ConflictException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.CONFLICT, LocalDateTime.now());
    }

}
```

### Descrição
Esta classe é um `ControllerAdvice` responsável por centralizar o tratamento de exceções em toda a aplicação. Cada método neste controlador trata um tipo específico de exceção, gerando uma resposta de erro padronizada.

### Métodos

1. **`handlerBusinessException`**
    - Descrição: Trata exceções do tipo `BusinessException`.
    - Parâmetros:
        - `ex` (BusinessException): A exceção a ser tratada.
        - `request` (HttpServletRequest): A requisição HTTP que gerou a exceção.
    - Retorno: ResponseEntity contendo uma resposta padronizada de erro.

2. **`handlerUnprocessableEntityException`**
    - Descrição: Trata exceções do tipo `UnprocessableEntityException`.
    - Parâmetros:
        - `ex` (UnprocessableEntityException): A exceção a ser tratada.
        - `request` (HttpServletRequest): A requisição HTTP que gerou a exceção.
    - Retorno: ResponseEntity contendo uma resposta padronizada de erro.

3. **`handlerConflictException`**
    - Descrição: Trata exceções do tipo `ConflictException`.
    - Parâmetros:
        - `ex` (ConflictException): A exceção a ser tratada.
        - `request` (HttpServletRequest): A requisição HTTP que gerou a exceção.
    - Retorno: ResponseEntity contendo uma resposta padronizada de erro.

4. **`response`**
    - Descrição: Método auxiliar para construir a resposta padronizada de erro.
    - Parâmetros:
        - `message` (String): Mensagem detalhando o motivo da exceção.
        - `request` (HttpServletRequest): A requisição HTTP que gerou a exceção.
        - `status` (HttpStatus): Status HTTP associado à exceção.
        - `data` (LocalDateTime): Data e hora em que ocorreu a exceção.
    - Retorno: ResponseEntity contendo uma resposta padronizada de erro.

### Anotações

#### `@ControllerAdvice`

- **Descrição:** Indica que a classe é responsável por tratar exceções em todo o aplicativo.

#### `@ExceptionHandler`

- **Descrição:** Indica que o método é responsável por lidar com uma exceção específica.

#### `@ResponseStatus`

- **Descrição:** Especifica o código de status HTTP associado a uma exceção. Utilizado nos métodos de tratamento de exceções para definir o status HTTP da resposta.

# Ambiente Kafka com Docker Compose - docker-compose-bitname.yml

O script abaixo representa um arquivo `docker-compose.yml` que define a configuração para criar e executar os serviços do Apache Kafka e do Apache ZooKeeper em contêineres Docker.

```yml
version: "3"
services:
  zookeeper:
    container_name: zookeeper
    image: "bitnami/zookeeper:latest"
    networks:
      - kafka-net
    ports:
      - "2181:2181"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    container_name: kafka
    image: "bitnami/kafka:latest"
    networks:
      - kafka-net
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: "1"
      KAFKA_ZOOKEEPER_CONNECT: "zookeeper:2181"
      KAFKA_ADVERTISED_HOST_NAME: "localhost"
      KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://localhost:9092"
      ALLOW_PLAINTEXT_LISTENER: "yes"
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    depends_on:
      - zookeeper

networks:
  kafka-net:
    driver: bridge
```

## `docker-compose.yml`

### Descrição
Este arquivo Docker Compose define dois serviços: um para o Apache ZooKeeper e outro para o Apache Kafka. Ambos os serviços são configurados para serem executados em uma rede específica chamada `kafka-net`.

### Serviços

1. **Serviço ZooKeeper**
    - **Container Name:** `zookeeper`
    - **Imagem Docker:** `bitnami/zookeeper:latest`
    - **Portas Expostas:**
        - `2181:2181` - Porta padrão do ZooKeeper.
    - **Variáveis de Ambiente:**
        - `ALLOW_ANONYMOUS_LOGIN=yes` - Permite login anônimo no ZooKeeper.
    - **Redes:**
        - `kafka-net`

2. **Serviço Kafka**
    - **Container Name:** `kafka`
    - **Imagem Docker:** `bitnami/kafka:latest`
    - **Portas Expostas:**
        - `9092:9092` - Porta padrão do Kafka.
    - **Variáveis de Ambiente:**
        - `KAFKA_BROKER_ID: "1"` - Identificador do broker Kafka.
        - `KAFKA_ZOOKEEPER_CONNECT: "zookeeper:2181"` - Endereço do ZooKeeper para coordenação.
        - `KAFKA_ADVERTISED_HOST_NAME: "localhost"` - Hostname pelo qual o Kafka é acessado.
        - `KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://localhost:9092"` - Endereço do broker Kafka.
        - `ALLOW_PLAINTEXT_LISTENER: "yes"` - Permite ouvir em texto simples.
        - `KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"` - Permite a criação automática de tópicos.
    - **Depende de:**
        - `zookeeper`
    - **Redes:**
        - `kafka-net`

### Redes

1. **`kafka-net`**
    - **Driver:** `bridge` - A rede bridge é usada para conectar os contêineres dos serviços Kafka e ZooKeeper.

### Notas

- O serviço Kafka depende do serviço ZooKeeper, garantindo que o ZooKeeper esteja pronto antes do Kafka iniciar.
- As imagens Docker usadas são da comunidade Bitnami, que fornece imagens prontas para uso com várias configurações padrão.
- As portas `2181` e `9092` são mapeadas para permitir a comunicação com o ZooKeeper e o Kafka, respectivamente.
- O arquivo Docker Compose facilita a criação de um ambiente Kafka/ZooKeeper local para desenvolvimento e teste.

Esse ambiente Docker permite a rápida criação de um cluster Kafka totalmente funcional e é útil para desenvolvimento, testes e ambientes locais de experimentação.

### Comando para criação da imagem docker-compose
Criação da Imagem no docker-compose:
**`docker-compose -f docker-compose-bitnami.yml up -d`**

# Configurador do Consumidor Kafka - KafkaConsumerConfig

O script abaixo configura a integração do consumidor Kafka na aplicação, utilizando a biblioteca Spring Kafka.

```java
package com.microservice.fakeapi.infraestructure.message.configs;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ProductsDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(GROUP_ID_CONFIG, "javanauta");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new
                JsonDeserializer<>(ProductsDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductsDTO>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductsDTO> factory = new
                ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

## `KafkaConsumerConfig`

### Descrição
Esta classe é responsável por configurar o consumidor Kafka na aplicação. Ela define as propriedades essenciais para estabelecer a conexão com o cluster Kafka, bem como as configurações específicas para deserialização de mensagens.

### Métodos

1. **`consumerFactory`**
    - Descrição: Configura e retorna a fábrica de consumidores Kafka.
    - Retorno: Uma instância de `ConsumerFactory` configurada para deserializar chaves como strings e valores como objetos `ProductsDTO`.

2. **`kafkaListenerContainerFactory`**
    - Descrição: Configura e retorna a fábrica de contêineres do ouvinte Kafka.
    - Retorno: Uma instância de `ConcurrentKafkaListenerContainerFactory` configurada com a `ConsumerFactory` definida.

### Beans

1. **`consumerFactory`**
    - Descrição: Define o bean responsável pela criação da fábrica de consumidores Kafka.
    - Retorno: Uma instância de `ConsumerFactory` configurada.

2. **`kafkaListenerContainerFactory`**
    - Descrição: Define o bean responsável pela criação da fábrica de contêineres do ouvinte Kafka.
    - Retorno: Uma instância de `ConcurrentKafkaListenerContainerFactory` configurada.

### Configurações

- **`BOOTSTRAP_SERVERS_CONFIG` (String):**
    - Descrição: Endereço do(s) servidor(es) Kafka.
    - Valor padrão: `localhost:9092`.

- **`GROUP_ID_CONFIG` (String):**
    - Descrição: Identificador do grupo de consumidores Kafka.
    - Valor padrão: `javanauta`.

- **`KEY_DESERIALIZER_CLASS_CONFIG` (String):**
    - Descrição: Classe de desserialização para chaves das mensagens Kafka.
    - Valor padrão: `StringDeserializer`.

- **`VALUE_DESERIALIZER_CLASS_CONFIG` (String):**
    - Descrição: Classe de desserialização para valores das mensagens Kafka.
    - Valor padrão: `JsonDeserializer` para desserialização de objetos `ProductsDTO`.

- **`AUTO_OFFSET_RESET_CONFIG` (String):**
    - Descrição: Define o ponto de início para a leitura de partições quando não há um offset inicial ou o offset inicial não existe.
    - Valor padrão: `earliest` (lê a partir do início).

### Notas

- A biblioteca `Spring Kafka` é utilizada para facilitar a integração do consumidor Kafka na aplicação Spring Boot.
- O consumidor está configurado para desserializar automaticamente objetos `ProductsDTO` provenientes do tópico Kafka.
- Os métodos e beans configurados são essenciais para a criação e funcionamento correto do consumidor Kafka na aplicação.

# Configurador do Produtor Kafka - KafkaProducerConfig

O script abaixo configura a integração do produtor Kafka na aplicação, utilizando a biblioteca Spring Kafka.

```java
package com.microservice.fakeapi.infraestructure.message.configs;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

## `KafkaProducerConfig`

### Descrição
Esta classe é responsável por configurar o produtor Kafka na aplicação. Ela define as propriedades essenciais para estabelecer a conexão com o cluster Kafka, bem como as configurações específicas para serialização de mensagens.

### Métodos

1. **`producerFactory`**
    - Descrição: Configura e retorna a fábrica de produtores Kafka.
    - Retorno: Uma instância de `ProducerFactory` configurada para serializar chaves e valores como strings.

2. **`kafkaTemplate`**
    - Descrição: Configura e retorna o modelo Kafka para facilitar a produção de mensagens.
    - Retorno: Uma instância de `KafkaTemplate` configurada com a `ProducerFactory` definida.

### Beans

1. **`producerFactory`**
    - Descrição: Define o bean responsável pela criação da fábrica de produtores Kafka.
    - Retorno: Uma instância de `ProducerFactory` configurada.

2. **`kafkaTemplate`**
    - Descrição: Define o bean responsável pela criação do modelo Kafka.
    - Retorno: Uma instância de `KafkaTemplate` configurada.

### Configurações

- **`BOOTSTRAP_SERVERS_CONFIG` (String):**
    - Descrição: Endereço do(s) servidor(es) Kafka.
    - Valor padrão: `localhost:9092`.

- **`KEY_SERIALIZER_CLASS_CONFIG` (Class):**
    - Descrição: Classe de serialização para chaves das mensagens Kafka.
    - Valor padrão: `StringSerializer` para serialização de strings.

- **`VALUE_SERIALIZER_CLASS_CONFIG` (Class):**
    - Descrição: Classe de serialização para valores das mensagens Kafka.
    - Valor padrão: `StringSerializer` para serialização de strings.

### Notas

- A biblioteca `Spring Kafka` é utilizada para facilitar a integração do produtor Kafka na aplicação Spring Boot.
- O produtor está configurado para serializar automaticamente chaves e valores como strings.
- Os métodos e beans configurados são essenciais para a criação e funcionamento correto do produtor Kafka na aplicação.

# Produtor Kafka para a Fake API - FakeApiProducer

O script abaixo implementa um produtor Kafka na aplicação, responsável por enviar mensagens para um tópico específico no cluster Kafka.

```java
package com.microservice.fakeapi.infraestructure.message.producer;

import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FakeApiProducer {

    @Value("${topico.fake-api.producer.nome}")
    private String topico;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public FakeApiProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviaRespostaCadastroProdutos(final String mensagem) {
        try {
            kafkaTemplate.send(topico, mensagem);
        } catch (Exception e) {
            throw new BusinessException("Erro ao produzir mensagem do kafka");
        }
    }
}

```

## `FakeApiProducer`

### Descrição
Esta classe atua como um componente Spring gerenciado e é responsável por produzir mensagens e enviá-las para um tópico Kafka configurado. O produtor é utilizado para comunicar eventos ou respostas para outros consumidores no sistema.

### Atributos

1. **`topico` (String):**
    - Descrição: Nome do tópico Kafka para o qual as mensagens serão enviadas.
    - Valor padrão: Definido pelo valor da propriedade `${topico.fake-api.producer.nome}` no arquivo de configuração.

2. **`kafkaTemplate` (KafkaTemplate<String, String>):**
    - Descrição: Instância do `KafkaTemplate` fornecida pelo Spring Kafka, utilizada para enviar mensagens para o tópico Kafka.

### Construtor

1. **`FakeApiProducer`**
    - Descrição: Construtor da classe que recebe uma instância de `KafkaTemplate` como argumento.
    - Parâmetro:
        - `kafkaTemplate`: Uma instância de `KafkaTemplate<String, String>`.

### Método

1. **`enviaRespostaCadastroProdutos`**
    - Descrição: Envia uma mensagem para o tópico Kafka configurado.
    - Parâmetro:
        - `mensagem` (String): Mensagem a ser enviada para o tópico.
    - Exceção: Lança uma exceção `BusinessException` se ocorrer algum erro ao produzir a mensagem no Kafka.

### Configurações

- **`topico` (String):**
    - Descrição: Define o tópico Kafka para o qual as mensagens serão enviadas.
    - Valor padrão: Obtido da propriedade `${topico.fake-api.producer.nome}`.

### Notas

- O componente é anotado com `@Component`, indicando que é um componente gerenciado pelo Spring.
- A injeção de dependência é realizada no construtor para obter uma instância de `KafkaTemplate`.
- O método `enviaRespostaCadastroProdutos` é utilizado para enviar mensagens para o tópico Kafka configurado.
- Se ocorrer um erro durante o envio da mensagem, uma exceção `BusinessException` será lançada para indicar o problema.

Essa classe é crucial para a comunicação assíncrona entre diferentes partes do sistema por meio do Kafka, permitindo que eventos ou respostas sejam disseminados de forma eficiente.

# Consumidor Kafka para a Fake API - FakeApiConsumer

O script abaixo implementa um consumidor Kafka na aplicação, responsável por ouvir mensagens de um tópico específico no cluster Kafka.

```java
package com.microservice.fakeapi.infraestructure.message.consumer;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.ProdutoService;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FakeApiConsumer {

    private final ProdutoService produtoService;
    private static final Logger logger = LoggerFactory.getLogger(FakeApiConsumer.class);

    @KafkaListener(topics = "${topico.fake-api.consumer.nome}", groupId = "${topico.fake-api.consumer.group-id}")
    public void recebeProdutosDTO(ProductsDTO productsDTO) {
        try{
            produtoService.salvaProdutoConsumer(productsDTO);
        } catch (Exception exception) {
            logger.error("Erro ao consumir mensagem do Kafka", exception);
            throw new BusinessException("Erro ao consumir mensagem do kafka ");
        }
    }
}
```

## `FakeApiConsumer`

### Descrição
Esta classe atua como um componente Spring gerenciado e é responsável por consumir mensagens de um tópico Kafka configurado. O consumidor é utilizado para processar eventos ou mensagens enviadas por produtores em outras partes do sistema.

### Atributos

1. **`produtoService` (ProdutoService):**
    - Descrição: Instância de `ProdutoService` responsável por processar as mensagens recebidas.
    - Injeção de Dependência: Realizada por meio do construtor.

2. **`logger` (Logger):**
    - Descrição: Instância do logger SLF4J utilizada para registrar informações e erros relacionados ao consumo de mensagens.
    - Valor Padrão: Obtido através do método `LoggerFactory.getLogger(FakeApiConsumer.class)`.

### Construtor

1. **`FakeApiConsumer`**
    - Descrição: Construtor da classe que recebe uma instância de `ProdutoService` como argumento.
    - Parâmetro:
        - `produtoService`: Uma instância de `ProdutoService`.

### Método

1. **`recebeProdutosDTO`**
    - Descrição: Método anotado com `@KafkaListener` que é chamado automaticamente quando uma mensagem é recebida no tópico Kafka configurado.
    - Parâmetro:
        - `productsDTO` (ProductsDTO): Mensagem recebida do tópico.
    - Exceção: Lança uma exceção `BusinessException` se ocorrer algum erro ao consumir a mensagem do Kafka.

### Anotações

- **`@KafkaListener`**
    - Descrição: Indica que o método `recebeProdutosDTO` deve ser invocado automaticamente ao receber mensagens do tópico Kafka configurado.
    - Atributos:
        - `topics` (String): Nome do tópico Kafka ao qual o consumidor está inscrito.
        - `groupId` (String): Identificador do grupo de consumidores Kafka ao qual o consumidor pertence.

### Notas

- O componente é anotado com `@Component`, indicando que é um componente gerenciado pelo Spring.
- A injeção de dependência é realizada no construtor para obter uma instância de `ProdutoService`.
- O método `recebeProdutosDTO` é acionado automaticamente quando uma mensagem é recebida no tópico Kafka configurado.
- Se ocorrer um erro durante o consumo da mensagem, uma exceção `BusinessException` será lançada para indicar o problema.
- O logger é utilizado para registrar informações e erros relacionados ao consumo de mensagens no Kafka.

Essa classe desempenha um papel essencial no processamento assíncrono de mensagens no sistema, permitindo que eventos ou dados sejam consumidos e processados de forma eficiente.

## Exemplo de Uso do Apache Kafka com Docker Compose

Neste exemplo, demonstrarei como produzir e consumir mensagens usando o Apache Kafka e o Docker Compose, com base nas configurações fornecidas no arquivo `docker-compose.yml` anteriormente discutido.

### Produzir Mensagens via Kafka

Para produzir uma mensagem usando o `kafka-console-producer.sh`, siga as instruções abaixo no prompt de comando:

```bash
docker exec -it kafka kafka-console-producer.sh --topic fake-api-consumer-products-v2 --bootstrap-server localhost:9092
```

Após executar o comando acima, você estará interagindo com um console onde pode inserir mensagens. Utilize o exemplo de mensagem fornecido:

```json
{"title": "Calça Jeans Azul", "price": "148.00", "category": "Calças", "description": "Calça Jeans Azul com Bolsos Laterais"}
```

Pressione Enter após inserir a mensagem para enviá-la para o tópico especificado (`fake-api-consumer-products-v2`).

### Consumir Mensagens via Kafka

Para consumir mensagens usando o `kafka-console-consumer.sh`, siga as instruções abaixo no pormpt de comando:

```bash
docker exec -it kafka kafka-console-consumer.sh --topic fake-api-producer-products-v2 --bootstrap-server localhost:9092
```

Isso abrirá um console que começará a exibir as mensagens produzidas no tópico especificado (`fake-api-producer-products-v2`). O console permanecerá ativo e exibirá mensagens conforme são consumidas.

Lembre-se de ajustar os tópicos (`fake-api-consumer-products-v2` e `fake-api-producer-products-v2`) conforme necessário com base na sua configuração e requisitos específicos.

Esse exemplo ilustra a produção e o consumo de mensagens em tempo real usando o Apache Kafka e o Docker Compose. 

**Para iniciar outro teste - por exemplo:**

Produzir a mensagem via kafka:
```bash
docker exec -it kafka kafka-console-producer.sh --topic fake-api-consumer-products-v3 --bootstrap-server localhost:9092
```

Exemplo de Mensagem:
```json
{"title": "Calça Jeans Azul", "price": "148.00", "category": "Calças", "description": "Calça Jeans Azul com Bolsos Laterais"}
```

Consumir a mensagem via kafka:
```bash
docker exec -it kafka kafka-console-consumer.sh --topic fake-api-producer-products-v3 --bootstrap-server localhost:9092
```

# Autor
## Feito por: `Daniel Penelva de Andrade`
