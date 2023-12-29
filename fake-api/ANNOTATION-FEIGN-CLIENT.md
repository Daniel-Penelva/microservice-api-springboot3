# Feign Clients

Os Feign Clients são uma parte importante do ecossistema Spring Cloud e são usados para simplificar a criação de clientes HTTP em aplicações Java que
se comunicam com serviços RESTful. A biblioteca Feign fornece uma abordagem declarativa para definir interfaces de cliente que mapeiam diretamente para
serviços web.

Pontos-chave sobre Feign Clients:

1. **Declaração Declarativa:** Em vez de criar manualmente implementações de clientes HTTP, o Feign permite que defina interfaces Java anotadas com `@FeignClient`. Essas interfaces são usadas para declarar como as chamadas HTTP devem ser feitas, fornecendo uma forma mais fácil e legível de interagir com serviços remotos.

2. **Anotações do Feign:** As anotações do Feign, como `@FeignClient`, `@GetMapping`, `@PostMapping`, etc., são usadas nas interfaces para descrever operações HTTP específicas e mapear essas operações para métodos Java. Por exemplo, um método anotado com `@GetMapping("/products/{productId}")` em uma interface Feign pode representar uma chamada GET para recuperar um produto específico.

3. **Integração com Spring Cloud:** Os Feign Clients são frequentemente usados em conjunto com o Spring Cloud para facilitar a descoberta de serviços e a carga equilibrada de carga. Ao usar `@FeignClient` em conjunto com Eureka (ou outro serviço de descoberta), você pode referenciar serviços por seus nomes lógicos em vez de URLs explícitas.

4. **Suporte a Hystrix:** O Feign integra-se facilmente com o Hystrix, que é uma biblioteca de tolerância a falhas, permitindo a você adicionar resiliência às chamadas de serviços remotos. Isso é particularmente útil em arquiteturas de microsserviços, onde as falhas em um serviço não devem impactar diretamente outros serviços.

Um exemplo simples de uma interface Feign Client:

```java
@FeignClient(name = "product-service", url = "https://api.example.com/products")
public interface ProductServiceClient {

    @GetMapping("/{productId}")
    Product getProductById(@PathVariable("productId") String productId);

    @PostMapping("/create")
    Product createProduct(@RequestBody ProductRequest request);
}
```

Neste exemplo, `@FeignClient` está sendo usado para definir um cliente chamado "product-service" que se comunica com a URL base "https://api.example.com/products". Os métodos `getProductById` e `createProduct` representam chamadas GET e POST para o serviço remoto, respectivamente.

Ao usar uma implementação de Feign Client, você pode simplificar significativamente a comunicação entre serviços, eliminando a necessidade de escrever manualmente código boilerplate relacionado à criação de clientes HTTP.

## Anotação `@EnableFeignClients`

A anotação `@EnableFeignClients` é uma anotação de configuração utilizada em aplicações Spring Boot para habilitar e configurar o Feign, que é uma biblioteca para criação de clientes REST em Java.

O Feign é parte do ecossistema Spring Cloud e é usado para simplificar a criação de clientes HTTP para se comunicar com serviços RESTful. Ele abstrai muitos detalhes da implementação do cliente HTTP, proporcionando uma forma mais fácil de declarar e integrar chamadas a serviços remotos.

Ao adicionar a anotação `@EnableFeignClients` a uma classe de configuração está ativando o suporte a clientes Feign na sua aplicação. Isso geralmente é usado em conjunto com interfaces Java anotadas com `@FeignClient` que definem as operações e os endpoints remotos que o Feign usará para fazer chamadas HTTP.

Em resumo, `@EnableFeignClients` é uma anotação de configuração que ativa o suporte a clientes Feign em uma aplicação Spring Boot, enquanto `@FeignClient` é usado para definir interfaces que descrevem clientes Feign específicos e suas operações associadas.

## Classe ProductsDto

A classe `ProductsDto` representa um objeto de transferência de dados (DTO) para manipular informações relacionadas a produtos.

```java
package com.microservice.apiproduct.apiv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductsDto {

    @JsonProperty(value = "id")
    private Long id;

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

Analisando as anotações e os campos presentes na classe `ProductsDto`:

1. **`@Getter`, `@Setter`:** Essas anotações são do projeto Lombok. Elas geram automaticamente métodos getters e setters para todos os campos da classe. Isso elimina a necessidade de escrever esses métodos manualmente, reduzindo a verbosidade do código.

2. **`@AllArgsConstructor`:** Essa anotação do Lombok gera automaticamente um construtor que aceita todos os campos da classe como argumentos. Isso é útil para criar instâncias da classe com todos os campos inicializados de uma vez.

3. **`@NoArgsConstructor`:** Essa anotação do Lombok gera automaticamente um construtor sem argumentos. Isso é útil em situações onde você precisa criar uma instância da classe sem fornecer valores iniciais para os campos.

4. **`@EqualsAndHashCode`:** Essa anotação do Lombok é usada para gerar automaticamente os métodos `equals()` e `hashCode()` que levam em consideração todos os campos da classe. Esses métodos são comumente usados em Java para comparar objetos quanto à igualdade e calcular códigos de hash.

5. **`@Builder`:** Essa anotação do Lombok gera um padrão de design Builder para a classe. O padrão Builder é útil quando tem muitos campos opcionais em uma classe e deseja criar instâncias de forma mais legível e fluente, especialmente quando há muitas combinações de parâmetros possíveis.

6. **Campos da Classe:**
    - **`id` (tipo `Long`):** Representa o identificador único do produto.
    - **`nome` (tipo `String`):** Representa o título ou nome do produto.
    - **`preco` (tipo `BigDecimal`):** Representa o preço do produto, usando `BigDecimal` para precisão em operações monetárias.
    - **`categoria` (tipo `String`):** Representa a categoria à qual o produto pertence.
    - **`descricao` (tipo `String`):** Representa uma descrição do produto.
    - **`imagem` (tipo `String`):** Representa o caminho ou URL da imagem associada ao produto.

A anotação `@JsonProperty` é uma anotação da biblioteca Jackson, que é usada para mapear os campos da classe para nomes específicos ao serializar ou desserializar objetos JSON. Neste caso, os nomes específicos para os campos JSON são fornecidos como argumentos para `@JsonProperty(value = "nomeDoCampo")`.

Em resumo, essa classe `ProductsDto` é um exemplo de DTO que pode ser usada para transportar dados relacionados a produtos entre diferentes partes de uma aplicação, como entre o frontend e o backend. As anotações do Lombok ajudam a reduzir a boilerplate, enquanto `@JsonProperty` ajuda na serialização/desserialização JSON.

## Interface FakeApiClient

Essa inteface utiliza a anotação `@FeignClient`, que faz parte do Spring Cloud OpenFeign, uma ferramenta que facilita a comunicação entre microservices por meio de chamadas HTTP.

Explicação detalhada do script:

```java
@FeignClient(value = "fake-api", url = "${fake-api.url:https://fakestoreapi.com}")
public interface FakeApiClient {
    @GetMapping("/products")
    List<ProductsDTO> buscaListaProdutos();
}
```

1. **`@FeignClient`:**
    - `@FeignClient` é uma anotação do Spring Cloud OpenFeign que é usada para declarar uma interface feign, que é uma maneira de definir clientes HTTP declarativamente.
    - `value = "fake-api"`: Esse valor é usado como o nome do serviço ao qual o Feign fará as chamadas. Isso é usado para identificar o serviço no sistema de descoberta (se estiver usando Eureka, por exemplo) ou no balanceador de carga.

2. **`url = "${fake-api.url:https://fakestoreapi.com}"`:**
    - `url` é usado para fornecer a URL base para o serviço que está sendo chamado. Aqui, a URL base é obtida a partir da propriedade `${fake-api.url}`.
    - `${fake-api.url:https://fakestoreapi.com}`: Isso significa que, por padrão, a URL será `https://fakestoreapi.com`, mas se a propriedade `fake-api.url` estiver definida em algum lugar (por exemplo, em um arquivo de propriedades ou variável de ambiente), ela substituirá o valor padrão.

3. **Interface e Método:**
    - A interface `FakeApiClient` define os métodos que serão chamados no serviço remoto.
    - `@GetMapping("/products")`: Indica que o método `buscaListaProdutos` deve ser mapeado para uma requisição HTTP GET para o caminho `/products` na URL base definida pela anotação `@FeignClient`.

4. **`List<ProductsDTO> buscaListaProdutos();`:**
    - Este método representa a operação que deseja executar no serviço remoto. Neste caso, é uma chamada para obter uma lista de produtos, e a resposta é mapeada para uma lista de objetos `ProductsDTO`.

Em resumo, esse script cria uma interface que define as operações que serão chamadas no serviço remoto `fake-api`. O Spring Cloud OpenFeign cuida da implementação concreta dessa interface e facilita a chamada de serviços HTTP de forma declarativa. A utilização de propriedades, como `${fake-api.url}`, torna o código configurável e flexível, permitindo que você altere a URL do serviço sem modificar o código-fonte.

## Classe FakeApiService

Esse script representa um serviço Spring chamado `FakeApiService`, que utiliza a interface Feign `FakeApiClient` para interagir com um serviço remoto. 

```java
@Service
@RequiredArgsConstructor
public class FakeApiService {

    private final FakeApiClient cliente;

    public List<ProductsDTO> buscarProdutos(){
        return cliente.buscaListaProdutos();
    }
}
```

Explicação detalhada do script:

1. **`@Service`:**
    - `@Service` é uma anotação do Spring que indica que a classe é um componente de serviço. No contexto do Spring, os serviços são geralmente usados para conter a lógica de negócios.

2. **`@RequiredArgsConstructor`:**
    - `@RequiredArgsConstructor` é uma anotação do projeto Lombok. Ela gera automaticamente um construtor que aceita todos os campos marcados como `final` ou `@NonNull`. Neste caso, como há um campo `final` (`FakeApiClient cliente`), o Lombok gera um construtor que aceita automaticamente o `FakeApiClient` como argumento.

3. **`private final FakeApiClient cliente;`:**
    - Declara um campo `cliente` do tipo `FakeApiClient`. Esse campo é injetado automaticamente pelo Spring, graças ao construtor gerado pelo Lombok e à anotação `@RequiredArgsConstructor`. A injeção de dependência é uma prática comum no Spring, permitindo que você injete componentes, como clientes Feign, em outras partes do seu código.

4. **`public List<ProductsDTO> buscarProdutos()`:**
    - Declara um método público chamado `buscarProdutos` que retorna uma lista de objetos `ProductsDTO`.
    - Este método é a camada de serviço responsável por orquestrar a chamada ao serviço remoto. Ele chama o método `buscaListaProdutos` na instância de `FakeApiClient` (injetada como `cliente`), que por sua vez faz a chamada para o serviço remoto usando Feign.
    - Essa abstração ajuda a separar a lógica de negócios específica da aplicação (como buscar produtos) da lógica de comunicação com serviços remotos.

Ao utilizar essa abordagem, a aplicação pode ser estendida ou modificada facilmente, substituindo a implementação de `FakeApiClient` ou introduzindo lógica adicional na camada de serviço sem afetar diretamente os controladores ou outros componentes da aplicação.

## Classe FakeApiController

Esse script representa um controlador Spring chamado `FakeApiController` que expõe endpoints HTTP para interagir com o serviço `FakeApiService`. 

```java
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
Analisando as partes importantes desse script:

1. **`@RestController`:**
    - `@RestController` é uma anotação do Spring que combina `@Controller` e `@ResponseBody`. Indica que todos os métodos deste controlador retornarão diretamente dados formatados, sem a necessidade de uma visualização (por exemplo, renderização de uma página HTML). Neste caso, os métodos retornarão dados no formato JSON.

2. **`@RequestMapping("/produtos")`:**
    - `@RequestMapping` é uma anotação do Spring usada para mapear URLs a métodos de controlador. Aqui, estamos especificando que todos os endpoints neste controlador começarão com "/produtos".

3. **`@RequiredArgsConstructor`:**
    - Semelhante ao que vimos anteriormente, essa anotação do Lombok gera automaticamente um construtor que aceita todos os campos marcados como `final` ou `@NonNull`. Neste caso, injeta automaticamente a instância de `FakeApiService` no construtor.

4. **`@Tag(name = "fake-api")`:**
    - Essa anotação faz parte do projeto Springdoc OpenAPI, que é uma ferramenta para gerar documentação da API com base em anotações no código. A tag é usada para categorizar o controlador na documentação da API.

5. **`private final FakeApiService service;`:**
    - Um campo `service` do tipo `FakeApiService`, que é injetado automaticamente pelo Spring. Ele representa a camada de serviço que encapsula a lógica de negócios.

6. **`@Operation` e `@ApiResponses`:**
    - Essas anotações são parte do Springdoc OpenAPI e são usadas para documentar a operação da API. `@Operation` fornece informações sobre a operação, como um resumo e o método HTTP usado. `@ApiResponses` especifica as respostas possíveis para a operação, neste caso, uma resposta bem-sucedida (código 200) e uma resposta de erro (código 500).

7. **`@GetMapping("")`:**
    - Mapeia o método `buscarProdutos` para requisições HTTP GET no caminho "/produtos". Isso significa que este método será invocado quando uma requisição GET for feita para "/produtos".

8. **`public ResponseEntity<List<ProductsDTO>> buscarProdutos()`:**
    - Um método que retorna uma `ResponseEntity` contendo uma lista de objetos `ProductsDTO`. `ResponseEntity` é usada para fornecer controle adicional sobre o status HTTP e os cabeçalhos da resposta.

9. **`return ResponseEntity.ok(service.buscarProdutos());`:**
    - Retorna uma resposta HTTP 200 OK contendo a lista de produtos obtida chamando o método `buscarProdutos` na instância de `FakeApiService`.

Em resumo, este controlador define um endpoint GET `/produtos` que retorna uma lista de produtos. As anotações de documentação são usadas para gerar automaticamente informações sobre a API, facilitando a compreensão da operação da API e das respostas possíveis.

## Arquivo de Configuração `application-dev.yaml`

Esse arquivo YAML contém configurações específicas do Spring Boot e do Spring Cloud para um aplicativo Java. 

```yaml
spring:
  cloud:
    loadbalancer:
      ribbon:
        enable: true

server:
  port: 8181
```

Explicação das principais configurações presentes:

1. **`spring` - Configurações Gerais do Spring:**
    - `cloud` - Configurações específicas do Spring Cloud.
        - `loadbalancer` - Configurações relacionadas ao balanceamento de carga.
            - `ribbon` - Configurações específicas do Ribbon, um cliente de balanceamento de carga do Spring Cloud.
                - `enable: true` - Habilita o uso do Ribbon para balanceamento de carga. O Ribbon é um componente que permite distribuir o tráfego entre várias instâncias de um serviço.

2. **`server` - Configurações do Servidor Embutido:**
    - `port: 8181` - Define a porta na qual o servidor embutido (como o Tomcat incorporado) será iniciado. Neste caso, o servidor estará escutando na porta 8181.

Essas configurações indicam que o aplicativo está usando o Ribbon como cliente de balanceamento de carga e será iniciado em uma porta específica.

Além disso, é importante observar que o arquivo YAML pode conter outras configurações específicas da sua aplicação, como configurações de banco de dados, perfis ativos, propriedades personalizadas e assim por diante. No entanto, as configurações fornecidas neste snippet são voltadas principalmente para o uso do Ribbon e para a definição da porta do servidor embutido.

## Configurando o VM Options da IDE Intellij Idea para rodar na porta 8181

Configurar as VM options na IDE IntelliJ IDEA para rodar na porta 8181, isso geralmente se refere à configuração de propriedades do sistema Java ao iniciar a aplicação. 

Explicando um pouco mais sobre esse conceito.

**O que são VM options:**
As VM options (opções da Máquina Virtual) são parâmetros que pode passar para a Máquina Virtual Java (JVM) ao iniciar uma aplicação. Elas são usadas para configurar vários aspectos da execução da JVM, como memória, configurações de sistema e outras opções específicas.

**Como configurar VM options na IntelliJ IDEA:**
Na IntelliJ IDEA, você pode configurar VM options para um projeto específico ou para uma configuração de execução. Aqui estão as etapas básicas:

1. Abra o IntelliJ IDEA e abra o projeto que você está trabalhando.
2. No canto superior direito, você verá uma caixa suspensa chamada "Configurations" (Configurações). Clique nela e escolha "Edit Configurations...".
3. Isso abrirá uma janela onde você pode configurar várias opções para suas execuções. 
4. Vai em `Modify options > add VM Options`. É aqui que você pode adicionar as opções da Máquina Virtual.
5. No meu caso eu adicionei no campo a seguinte configuração: `-Dspring.profiles.active=dev`

A opção `-Dspring.profiles.active=dev` nas VM options é uma maneira de ativar o perfil de ambiente "dev" para a sua aplicação Spring. O perfil de ambiente no Spring permite que você configure diferentes conjuntos de propriedades ou comportamentos com base no ambiente em que sua aplicação está sendo executada.

No meu caso, estou usando o perfil "dev", indicando que a aplicação deve usar configurações específicas destinadas ao ambiente de desenvolvimento. Isso é útil porque você pode ter diferentes configurações para desenvolvimento, teste e produção, e o Spring facilita a gestão dessas configurações por meio de perfis de ambiente.

Ao usar `-Dspring.profiles.active=dev`:

- A aplicação Spring irá procurar por arquivos de propriedades específicos do perfil "dev", como `application-dev.yml` ou `application-dev.properties`.
- Qualquer configuração específica do perfil "dev" definida nesses arquivos será aplicada.

Esta é uma prática comum para garantir que a aplicação use configurações adequadas ao ambiente de desenvolvimento, separadas das configurações usadas em ambientes de teste ou produção.

Se você precisar configurar outras propriedades específicas para o ambiente de desenvolvimento, você pode adicioná-las ao arquivo `application-dev.yml` ou `application-dev.properties`. Isso permite que ajuste o comportamento da aplicação de acordo com as necessidades do ambiente em que está sendo desenvolvida.

6. Outra forma de configuração

Neste campo, você pode adicionar opções específicas, como `-Dserver.port=8181` para definir a porta do servidor. O `-D` é usado para definir propriedades do sistema Java.

**Exemplo:**
Se quiser definir a porta do servidor para 8181, você adicionaria `-Dserver.port=8181` nas VM options. O campo VM options pode conter várias opções separadas por espaço.

Lembre-se de que isso é uma solução específica para o ambiente de desenvolvimento na sua IDE. Quando você executa a aplicação fora do ambiente de desenvolvimento, como em um ambiente de produção, essas configurações específicas podem ser diferentes. Normalmente, no ambiente de produção, as configurações são gerenciadas externamente por meio de arquivos de configuração, variáveis de ambiente ou outros mecanismos de configuração específicos da plataforma.

# Autor
## Feito por: `Daniel Penelva de Andrade`

