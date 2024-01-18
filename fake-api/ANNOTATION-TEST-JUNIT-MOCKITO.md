# Classe de Teste `FakeApiControllerTest`

O teste unitário utiliza a estrutura do framework Mockito para simular o comportamento de alguns serviços e validar o funcionamento correto da lógica na classe `FakeApiController`. Analisando o script por partes:

```java
package com.microservice.fakeapi.apiV1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.fakeapi.apiv1.dto.FakeApiController;
import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.FakeApiService;
import com.microservice.fakeapi.business.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FakeApiControllerTest {

    @InjectMocks
    FakeApiController controller;
    @Mock
    FakeApiService fakeApiService;
    @Mock
    ProdutoService produtoService;
    private MockMvc mockMvc;
    private String json;
    private String url;
    private ProductsDTO productsDTO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws JsonProcessingException {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();

        url = "/produtos";

        productsDTO = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        json = objectMapper.writeValueAsString(productsDTO);
    }

    @Test
    void testeBuscarProdutosFakeApiESalvarComSucesso() throws Exception {

        when(fakeApiService.buscarProdutos()).thenReturn(Collections.singletonList(productsDTO));

        mockMvc.perform(post(url + "/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(fakeApiService).buscarProdutos();
        verifyNoMoreInteractions(fakeApiService);
    }

    @Test
    void testeSalvarProdutosDtoComSucesso() throws Exception {

        when(produtoService.salvarProdutoDTO(productsDTO)).thenReturn(productsDTO);

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).salvarProdutoDTO(productsDTO);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestCasoProdutoDtoSejaNull() throws Exception {

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeAtualizarProdutosDtoComSucesso() throws Exception {

        String id = "1245";

        when(produtoService.updateProduto(id, productsDTO)).thenReturn(productsDTO);

        mockMvc.perform(put(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).updateProduto(id, productsDTO);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestCasoIdSejaNull() throws Exception {

        mockMvc.perform(put(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeDeletarProdutosDtoComSucesso() throws Exception {

        String nome = "Jaqueta Vermelha";

        doNothing().when(produtoService).deletarProduto(nome);

        mockMvc.perform(delete(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("nome", nome)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()); // Vale ressaltar que o status que estou esperando é o isAccepted() de acordo como está implementado no método HTTP Delete deleteProdutos (FakeApiController).

        verify(produtoService).deletarProduto(nome);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeNaoEnviarRequestDeleteCasoNomeSejaNull() throws Exception {

        mockMvc.perform(delete(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }

    @Test
    void testeBuscarProdutosDtoPorNomeComSucesso() throws Exception {

        String nome = "Jaqueta Vermelha";

        when(produtoService.buscarProdutoPorNome(nome)).thenReturn(productsDTO);

        mockMvc.perform(get(url + "/" + nome)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Vale ressaltar que o status que estou esperando é o isOk() de acordo como está implementado no método HTTP Get buscaProdutosPorNome (FakeApiController).

        verify(produtoService).buscarProdutoPorNome(nome);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeBuscarTodosProdutosDtoComSucesso() throws Exception {

        List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    void testeBuscarVerificandoSeExisteUmProdutoDtoComSucesso() throws Exception {

        List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Verificando se a resposta contém a lista com um produto

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }
}
```

## Método `setup()`

Analisando o método `setup()` passo a passo:

```java
    @BeforeEach
    void setup() throws JsonProcessingException {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();

        url = "/produtos";

        productsDTO = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        json = objectMapper.writeValueAsString(productsDTO);
    }
```

1. **`mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();`**
    - `MockMvcBuilders.standaloneSetup(controller)`: Configura o `MockMvc` para trabalhar com um controlador específico (`controller`).
    - `.alwaysDo(print())`: Adiciona uma ação para imprimir detalhes da execução das solicitações, útil para debug. Neste caso, os detalhes da solicitação serão impressos no console.
    - `.build()`: Constrói o objeto `MockMvc` configurado.

2. **`url = "/produtos";`**
    - Define a variável `url` com o valor `"/produtos"`.

3. **Configuração do `ProductsDTO`:**
    - `ProductsDTO.builder()`: Cria um construtor de `ProductsDTO`.
    - `.nome("Jaqueta Vermelha")`: Configura o nome do produto como "Jaqueta Vermelha".
    - `.categoria("Roupas")`: Configura a categoria do produto como "Roupas".
    - `.descricao("Jaqueta Vermelha com bolso")`: Configura a descrição do produto.
    - `.preco(new BigDecimal(500.00))`: Configura o preço do produto como um `BigDecimal` de 500.00.
    - `.build()`: Constrói o objeto `ProductsDTO` com as configurações fornecidas.

4. **`json = objectMapper.writeValueAsString(productsDTO);`**
    - `objectMapper.writeValueAsString(productsDTO)`: Converte o objeto `ProductsDTO` em uma representação JSON como uma string.
    - A string resultante é atribuída à variável `json`.

Em resumo, o método `setup()` é responsável por configurar o ambiente para os testes. Ele cria uma instância do `MockMvc` configurada para trabalhar com o controlador `controller`, define a URL base para os testes como `"/produtos"`, cria um objeto `ProductsDTO` com dados específicos e converte esse objeto para uma representação JSON, que é armazenada na variável `json`. Essas configurações são essenciais para a execução dos testes unitários na classe `FakeApiControllerTest`.

## Método `testeBuscarProdutosFakeApiESalvarComSucesso()`

Analisando o método `testeBuscarProdutosFakeApiESalvarComSucesso()` passo a passo:

```java
    @Test
    void testeBuscarProdutosFakeApiESalvarComSucesso() throws Exception {

        when(fakeApiService.buscarProdutos()).thenReturn(Collections.singletonList(productsDTO));

        mockMvc.perform(post(url + "/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(fakeApiService).buscarProdutos();
        verifyNoMoreInteractions(fakeApiService);
    }
```

1. **`when(fakeApiService.buscarProdutos()).thenReturn(Collections.singletonList(productsDTO));`**
    - `when`: Configura um comportamento esperado para uma chamada de método em um mock.
    - `fakeApiService.buscarProdutos()`: Especifica a chamada do método `buscarProdutos()` no mock `fakeApiService`.
    - `.thenReturn(Collections.singletonList(productsDTO))`: Indica que, quando o método `buscarProdutos()` for chamado, o mock deve retornar uma lista contendo o objeto `productsDTO`. Isso simula o comportamento da chamada ao serviço externo `FakeApiService`, retornando uma lista fictícia de produtos.

2. **`mockMvc.perform(post(url + "/api").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());`**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `post(url + "/api")`: Configura a solicitação HTTP como um método POST para a URL `"/produtos/api"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso.

3. **`verify(fakeApiService).buscarProdutos();`**
    - `verify`: Verifica se o método especificado foi chamado no mock.
    - `fakeApiService`: O mock em que se está verificando a chamada.
    - `.buscarProdutos()`: O método específico que está sendo verificado.

4. **`verifyNoMoreInteractions(fakeApiService);`**
    - `verifyNoMoreInteractions`: Garante que não haja mais interações com o mock `fakeApiService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço externo `FakeApiService` ao buscar produtos fictícios. Ele configura o comportamento esperado para o método `buscarProdutos()` do `fakeApiService`, simula uma chamada HTTP POST para a URL `"/produtos/api"`, verifica se a resposta tem um código de status 200 e, em seguida, verifica se o método `buscarProdutos()` foi chamado no `fakeApiService` e se não houve mais interações inesperadas.

## Método `testeSalvarProdutosDtoComSucesso()`

Analisando o método `testeSalvarProdutosDtoComSucesso()` passo a passo:

```java
    @Test
    void testeSalvarProdutosDtoComSucesso() throws Exception {

        when(produtoService.salvarProdutoDTO(productsDTO)).thenReturn(productsDTO);

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(produtoService).salvarProdutoDTO(productsDTO);
        verifyNoMoreInteractions(produtoService);
    }
```

1. **`when(produtoService.salvarProdutoDTO(productsDTO)).thenReturn(productsDTO);`**
    - `when`: Configura um comportamento esperado para uma chamada de método em um mock.
    - `produtoService.salvarProdutoDTO(productsDTO)`: Especifica a chamada do método `salvarProdutoDTO(productsDTO)` no mock `produtoService`.
    - `.thenReturn(productsDTO)`: Indica que, quando o método `salvarProdutoDTO(productsDTO)` for chamado, o mock deve retornar o objeto `productsDTO`. Isso simula o comportamento de sucesso ao salvar um produto no serviço `produtoService`.

2. **`mockMvc.perform(post(url + "/").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());`**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `post(url + "/")`: Configura a solicitação HTTP como um método POST para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.content(json)`: Adiciona o corpo da solicitação, que é a representação JSON do objeto `productsDTO` criada no método `setup()`.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso.

3. **`verify(produtoService).salvarProdutoDTO(productsDTO);`**
    - `verify`: Verifica se o método especificado foi chamado no mock.
    - `produtoService`: O mock em que se está verificando a chamada.
    - `.salvarProdutoDTO(productsDTO)`: O método específico que está sendo verificado.

4. **`verifyNoMoreInteractions(produtoService);`**
    - `verifyNoMoreInteractions`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao salvar um produto. Ele configura o comportamento esperado para o método `salvarProdutoDTO(productsDTO)` do `produtoService`, simula uma chamada HTTP POST para a URL `"/produtos/"`, verifica se a resposta tem um código de status 200 e, em seguida, verifica se o método `salvarProdutoDTO(productsDTO)` foi chamado no `produtoService` e se não houve mais interações inesperadas.

## Método `testeNaoEnviarRequestCasoProdutoDtoSejaNull()`

Analisando o método `testeNaoEnviarRequestCasoProdutoDtoSejaNull()` passo a passo:

```java
    @Test
    void testeNaoEnviarRequestCasoProdutoDtoSejaNull() throws Exception {

        mockMvc.perform(post(url + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(produtoService);
    }
```

1. **`mockMvc.perform(post(url + "/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());`**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `post(url + "/")`: Configura a solicitação HTTP como um método POST para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isBadRequest())`: Verifica se o código de status da resposta é 400 (Bad Request). Isso assegura que a solicitação falhou porque o corpo da solicitação estava ausente ou nulo.

2. **`verifyNoInteractions(produtoService);`**
    - `verifyNoInteractions`: Verifica se não houve interações com o mock `produtoService`. Isso é usado para garantir que o serviço não foi chamado, pois a condição para realizar a solicitação não foi atendida (não há corpo JSON).

Em resumo, este método de teste verifica se a `FakeApiController` trata corretamente o caso em que a solicitação HTTP POST para `"/produtos/"` não contém um corpo JSON válido (nulo). Ele espera que a resposta tenha um código de status 400 (Bad Request) e verifica que o serviço `produtoService` não foi interagido, já que não deveria ter sido chamado quando a condição de envio do corpo não foi atendida.


## Método `testeAtualizarProdutosDtoComSucesso()`

Analisando o método `testeAtualizarProdutosDtoComSucesso()` passo a passo:

```java
    @Test
    void testeAtualizarProdutosDtoComSucesso() throws Exception {

            String id = "1245";

            when(produtoService.updateProduto(id, productsDTO)).thenReturn(productsDTO);

            mockMvc.perform(put(url + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .param("id", id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

            verify(produtoService).updateProduto(id, productsDTO);
            verifyNoMoreInteractions(produtoService);
    }
```

1. **Configuração dos Dados de Entrada:**
    - `String id = "1245";`: Cria uma string `id` com um valor fictício para representar a identificação do produto.

2. **Configuração do Comportamento do Mock:**
    - `when(produtoService.updateProduto(id, productsDTO)).thenReturn(productsDTO);`: Configura o mock `produtoService` para retornar `productsDTO` quando o método `updateProduto` é chamado com os argumentos especificados (`id` e `productsDTO`).

3. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `put(url + "/")`: Configura a solicitação HTTP como um método PUT para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.content(json)`: Adiciona o corpo da solicitação, que é a representação JSON do objeto `productsDTO`.
    - `.param("id", id)`: Adiciona um parâmetro à solicitação, representando o ID do produto a ser atualizado.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso.

4. **Verificação do Comportamento do Mock:**
    - `verify(produtoService).updateProduto(id, productsDTO);`: Verifica se o método `updateProduto` foi chamado no mock `produtoService` com os argumentos especificados (`id` e `productsDTO`).

5. **Verificação de Interações Adicionais:**
    - `verifyNoMoreInteractions(produtoService);`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao tentar atualizar um produto. Ele configura o comportamento esperado para o método `updateProduto` do `produtoService`, simula uma chamada HTTP PUT para a URL `"/produtos/"`, verifica se a resposta tem um código de status 200 e, em seguida, verifica se o método `updateProduto` foi chamado no `produtoService` e se não houve mais interações inesperadas.


## Método `testeNaoEnviarRequestCasoIdSejaNull()`

Analisando o método `testeNaoEnviarRequestCasoIdSejaNull()` passo a passo:

```java
    @Test
    void testeNaoEnviarRequestCasoIdSejaNull() throws Exception {

            mockMvc.perform(put(url + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

            verifyNoInteractions(produtoService);
    }
```

1. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `put(url + "/")`: Configura a solicitação HTTP como um método PUT para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.content(json)`: Adiciona o corpo da solicitação, que é a representação JSON do objeto `productsDTO`.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isBadRequest())`: Verifica se o código de status da resposta é 400 (Bad Request). Isso assegura que a solicitação falhou porque o ID do produto estava ausente ou nulo.

2. **Verificação de Interações com o Mock:**
    - `verifyNoInteractions(produtoService);`: Verifica se não houve interações com o mock `produtoService`. Isso é usado para garantir que o serviço não foi chamado, pois a condição para realizar a solicitação não foi atendida (não há ID).

Em resumo, este método de teste verifica se a `FakeApiController` trata corretamente o caso em que a solicitação HTTP PUT para `"/produtos/"` não contém um ID de produto válido (nulo). Ele espera que a resposta tenha um código de status 400 (Bad Request) e verifica que o serviço `produtoService` não foi interagido, já que não deveria ter sido chamado quando a condição de envio do ID não foi atendida.

## Método `testeDeletarProdutosDtoComSucesso()`

Analisando o método `testeDeletarProdutosDtoComSucesso()` passo a passo:

```java
    @Test
    void testeDeletarProdutosDtoComSucesso() throws Exception {

            String nome = "Jaqueta Vermelha";

            doNothing().when(produtoService).deletarProduto(nome);

            mockMvc.perform(delete(url + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .param("nome", nome)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted()); // Vale ressaltar que o status que estou esperando é o isAccepted() de acordo como está implementado no método HTTP Delete deleteProdutos (FakeApiController).

            verify(produtoService).deletarProduto(nome);
            verifyNoMoreInteractions(produtoService);
    }
```

1. **Configuração dos Dados de Entrada:**
    - `String nome = "Jaqueta Vermelha";`: Cria uma string `nome` com um valor fictício para representar o nome do produto a ser deletado.

2. **Configuração do Comportamento do Mock:**
    - `doNothing().when(produtoService).deletarProduto(nome);`: Configura o mock `produtoService` para não fazer nada (`doNothing()`) quando o método `deletarProduto` é chamado com o argumento especificado (`nome`).

3. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `delete(url + "/")`: Configura a solicitação HTTP como um método DELETE para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.content(json)`: Adiciona o corpo da solicitação, que é a representação JSON do objeto `productsDTO`.
    - `.param("nome", nome)`: Adiciona um parâmetro à solicitação, representando o nome do produto a ser deletado.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isAccepted())`: Verifica se o código de status da resposta é 202 (Accepted). Isso assegura que a solicitação foi aceita, de acordo com o comentário no código.

4. **Verificação do Comportamento do Mock:**
    - `verify(produtoService).deletarProduto(nome);`: Verifica se o método `deletarProduto` foi chamado no mock `produtoService` com o argumento especificado (`nome`).

5. **Verificação de Interações Adicionais:**
    - `verifyNoMoreInteractions(produtoService);`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao deletar um produto. Ele configura o comportamento esperado para o método `deletarProduto` do `produtoService`, simula uma chamada HTTP DELETE para a URL `"/produtos/"`, verifica se a resposta tem um código de status 202 e, em seguida, verifica se o método `deletarProduto` foi chamado no `produtoService` e se não houve mais interações inesperadas.


## Método `testeNaoEnviarRequestDeleteCasoNomeSejaNull()`

Analisando o método `testeNaoEnviarRequestDeleteCasoNomeSejaNull()` passo a passo:

```java
    @Test
    void testeNaoEnviarRequestDeleteCasoNomeSejaNull() throws Exception {

            mockMvc.perform(delete(url + "/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

            verifyNoInteractions(produtoService);
    }
```

1. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `delete(url + "/")`: Configura a solicitação HTTP como um método DELETE para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isBadRequest())`: Verifica se o código de status da resposta é 400 (Bad Request). Isso assegura que a solicitação falhou porque o nome do produto estava ausente ou nulo.

2. **Verificação de Interações com o Mock:**
    - `verifyNoInteractions(produtoService);`: Verifica se não houve interações com o mock `produtoService`. Isso é usado para garantir que o serviço não foi chamado, pois a condição para realizar a solicitação não foi atendida (não há nome).

Em resumo, este método de teste verifica se a `FakeApiController` trata corretamente o caso em que a solicitação HTTP DELETE para `"/produtos/"` não contém um nome de produto válido (nulo). Ele espera que a resposta tenha um código de status 400 (Bad Request) e verifica que o serviço `produtoService` não foi interagido, já que não deveria ter sido chamado quando a condição de envio do nome não foi atendida.


## Método `testeBuscarProdutosDtoPorNomeComSucesso()`

Analisando o método `testeBuscarProdutosDtoPorNomeComSucesso()` passo a passo:

```java
    @Test
    void testeBuscarProdutosDtoPorNomeComSucesso() throws Exception {

            String nome = "Jaqueta Vermelha";

            when(produtoService.buscarProdutoPorNome(nome)).thenReturn(productsDTO);

            mockMvc.perform(get(url + "/" + nome)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()); // Vale ressaltar que o status que estou esperando é o isOk() de acordo como está implementado no método HTTP Get buscaProdutosPorNome (FakeApiController).

            verify(produtoService).buscarProdutoPorNome(nome);
            verifyNoMoreInteractions(produtoService);
    }
```

1. **Configuração dos Dados de Entrada:**
    - `String nome = "Jaqueta Vermelha";`: Cria uma string `nome` com um valor fictício para representar o nome do produto a ser buscado.

2. **Configuração do Comportamento do Mock:**
    - `when(produtoService.buscarProdutoPorNome(nome)).thenReturn(productsDTO);`: Configura o mock `produtoService` para retornar `productsDTO` quando o método `buscarProdutoPorNome` é chamado com o argumento especificado (`nome`).

3. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `get(url + "/" + nome)`: Configura a solicitação HTTP como um método GET para a URL `"/produtos/nome"`, onde `nome` é o valor da variável criada anteriormente.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso, de acordo com o comentário no código.

4. **Verificação do Comportamento do Mock:**
    - `verify(produtoService).buscarProdutoPorNome(nome);`: Verifica se o método `buscarProdutoPorNome` foi chamado no mock `produtoService` com o argumento especificado (`nome`).

5. **Verificação de Interações Adicionais:**
    - `verifyNoMoreInteractions(produtoService);`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao buscar um produto por nome. Ele configura o comportamento esperado para o método `buscarProdutoPorNome` do `produtoService`, simula uma chamada HTTP GET para a URL `"/produtos/nome"`, verifica se a resposta tem um código de status 200 e, em seguida, verifica se o método `buscarProdutoPorNome` foi chamado no `produtoService` e se não houve mais interações inesperadas.


## Método `testeBuscarTodosProdutosDtoComSucesso()`

Analisando o método `testeBuscarTodosProdutosDtoComSucesso()` passo a passo:

```java
    @Test
    void testeBuscarTodosProdutosDtoComSucesso() throws Exception {

            List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }
```

1. **Configuração dos Dados de Entrada:**
    - `List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());`: Cria uma lista contendo um objeto `ProductsDTO`. Isso representa a lista fictícia de produtos que o serviço `produtoService` deve retornar.

2. **Configuração do Comportamento do Mock:**
    - `when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);`: Configura o mock `produtoService` para retornar `listaProdutos` quando o método `buscarTodosProdutos` é chamado.

3. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `get(url + "/")`: Configura a solicitação HTTP como um método GET para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso.

4. **Verificação do Comportamento do Mock:**
    - `verify(produtoService).buscarTodosProdutos();`: Verifica se o método `buscarTodosProdutos` foi chamado no mock `produtoService`.

5. **Verificação de Interações Adicionais:**
    - `verifyNoMoreInteractions(produtoService);`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao buscar todos os produtos. Ele configura o comportamento esperado para o método `buscarTodosProdutos` do `produtoService`, simula uma chamada HTTP GET para a URL `"/produtos/"`, verifica se a resposta tem um código de status 200 e, em seguida, verifica se o método `buscarTodosProdutos` foi chamado no `produtoService` e se não houve mais interações inesperadas.

## Método `testeBuscarVerificandoSeExisteUmProdutoDtoComSucesso()`

Analisando o método `testeBuscarVerificandoSeExisteUmProdutoDtoComSucesso()` passo a passo:

```java
    @Test
    void testeBuscarVerificandoSeExisteUmProdutoDtoComSucesso() throws Exception {

            List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());

        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);

        mockMvc.perform(get(url + "/")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1))); // Verificando se a resposta contém a lista com um produto

        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(produtoService);
    }
```

1. **Configuração dos Dados de Entrada:**
    - `List<ProductsDTO> listaProdutos = Arrays.asList(new ProductsDTO());`: Cria uma lista contendo um objeto `ProductsDTO`. Isso representa a lista fictícia de produtos que o serviço `produtoService` deve retornar.

2. **Configuração do Comportamento do Mock:**
    - `when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutos);`: Configura o mock `produtoService` para retornar `listaProdutos` quando o método `buscarTodosProdutos` é chamado.

3. **Chamada à `mockMvc.perform(...)`:**
    - `mockMvc.perform(...)`: Inicia uma execução de uma solicitação HTTP simulada usando o `MockMvc`.
    - `get(url + "/")`: Configura a solicitação HTTP como um método GET para a URL `"/produtos/"`.
    - `.contentType(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo da solicitação como JSON.
    - `.accept(MediaType.APPLICATION_JSON)`: Define o tipo de conteúdo aceito na resposta como JSON.
    - `.andExpect(status().isOk())`: Verifica se o código de status da resposta é 200 (OK). Isso assegura que a solicitação foi tratada com sucesso.
    - `.andExpect(jsonPath("$", hasSize(1)))`: Verifica se o JSON de resposta possui um tamanho de 1, ou seja, se contém um produto. Isso utiliza a biblioteca de Hamcrest Matchers (`hasSize`) para realizar a verificação.

4. **Verificação do Comportamento do Mock:**
    - `verify(produtoService).buscarTodosProdutos();`: Verifica se o método `buscarTodosProdutos` foi chamado no mock `produtoService`.

5. **Verificação de Interações Adicionais:**
    - `verifyNoMoreInteractions(produtoService);`: Garante que não haja mais interações com o mock `produtoService` após a verificação anterior. Isso é útil para garantir que não haja chamadas adicionais não esperadas após o teste.

Em resumo, este método de teste verifica se a `FakeApiController` interage corretamente com o serviço `produtoService` ao buscar todos os produtos e, em seguida, verifica se a resposta contém pelo menos um produto. Ele configura o comportamento esperado para o método `buscarTodosProdutos` do `produtoService`, simula uma chamada HTTP GET para a URL `"/produtos/"`, verifica se a resposta tem um código de status 200, verifica se a resposta JSON possui um tamanho de 1 (pelo menos um produto) e, em seguida, verifica se o método `buscarTodosProdutos` foi chamado no `produtoService` e se não houve mais interações inesperadas.

## Resumo

1. **Anotações de Teste:**
    - `@ExtendWith(MockitoExtension.class)`: Indica que a extensão do Mockito será usada para executar os testes.
    - `@InjectMocks`: Anotação usada para injetar mocks nas propriedades da classe de teste (`FakeApiController`).
    - `@Mock`: Cria mocks para as dependências da classe (`FakeApiService` e `ProdutoService`).

2. **Inicialização e Configuração:**
    - `@BeforeEach`: Método que é executado antes de cada teste.
        - Configuração do `MockMvc` para simular chamadas HTTP.
        - Inicialização de algumas variáveis como `url`, `json` (uma representação em JSON de um objeto `ProductsDTO`), e outras.
        - Serialização de um objeto `ProductsDTO` para JSON usando o `ObjectMapper`.

3. **Testes Individuais:**
    - Cada método de teste (`@Test`) representa um cenário de teste específico.
    - Os testes usam o `MockMvc` para simular requisições HTTP à `FakeApiController` e verificam as respostas esperadas.

4. **Alguns Exemplos de Testes:**
    - `testeBuscarProdutosFakeApiESalvarComSucesso`: Testa se a chamada à `buscarProdutos()` do `fakeApiService` retorna uma lista e se a resposta HTTP é 200.
    - `testeSalvarProdutosDtoComSucesso`: Testa se a chamada à `salvarProdutoDTO(productsDTO)` do `produtoService` retorna um `ProductsDTO` e se a resposta HTTP é 200.
    - `testeNaoEnviarRequestCasoProdutoDtoSejaNull`: Testa se a tentativa de salvar um produto com DTO nulo resulta em um status de resposta 400 (Bad Request).

5. **Verificações do Mockito:**
    - `verify`: Verifica se um método de um mock foi chamado com os argumentos esperados.
    - `verifyNoMoreInteractions`: Garante que não há mais interações com os mocks além das especificadas.

Em resumo, este script de teste utiliza o Mockito e o MockMvc para verificar se as operações na `FakeApiController` interagem corretamente com os meus serviços (`FakeApiService` e `ProdutoService`) e se as respostas HTTP estão de acordo com o esperado em diferentes cenários.

---

# classe de Teste `ProdutoConverterTeste`

```java
package com.microservice.fakeapi.business.converter;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.infraestructure.entities.ProdutoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoConverterTeste {

    @InjectMocks
    ProdutoConverter converter;

    @Test
    void testeConverterParaProdutoEntityComSucesso(){

       ProductsDTO productsDTO = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntity = converter.toEntity(productsDTO);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());

        assertNotNull(produtoEntity.getId());
        assertNotNull(produtoEntity.getDataInclusao());
    }

    @Test
    void testeConverterParaProdutoEntityUpdateComSucesso(){

        ProductsDTO productsDTO = ProductsDTO.builder()
                //.nome("Jaqueta Vermelha")  não vou alterar o nome, logo não precisa passar
                //.categoria("Roupas") não vou alterar a categoria, logo não precisa passar
                .descricao("Jaqueta Vermelha com bolso e listas azuis")
                .preco(new BigDecimal(250.00))
                .build();

        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso e listas azuis")
                .preco(new BigDecimal(250.00))
                .build();

        String id = "12345";

        ProdutoEntity entity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProdutoEntity produtoEntity = converter.toEntityUpdate(entity, productsDTO, id);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());
        assertEquals(produtoEntityEsperado.getId(), produtoEntity.getId());
        assertEquals(produtoEntityEsperado.getDataInclusao(), produtoEntity.getDataInclusao());

        assertNotNull(produtoEntity.getDataAtualizacao());
    }

    @Test
    void testeConverterParaProdutoDtoComSucesso(){

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO productsDTOEsperado = ProductsDTO.builder()
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO produtodto = converter.toDTO(produtoEntity);

        assertEquals(productsDTOEsperado.getNome(), produtoEntity.getNome());
        assertEquals(productsDTOEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(productsDTOEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(productsDTOEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(productsDTOEsperado.getImagem(), produtoEntity.getImagem());

        assertNull(productsDTOEsperado.getId());
        assertNotNull(produtoEntity.getId());
    }

    @Test
    void testeConverterParaListaProdutoDtoComSucesso(){

        List<ProductsDTO> listaProdutosDto = new ArrayList<>();
        List<ProdutoEntity> listaProdutosEntity = new ArrayList<>();

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        ProductsDTO productsDTO = ProductsDTO.builder()
                .entityId("12345")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolso")
                .preco(new BigDecimal(500.00))
                .build();

        listaProdutosDto.add(productsDTO);
        listaProdutosEntity.add(produtoEntity);

        List<ProductsDTO> gerandoListaProdutoDto = converter.toListDTO(listaProdutosEntity);

        assertEquals(listaProdutosDto, gerandoListaProdutoDto);
        
    }
}
```

Analisando cada um dos métodos de teste no script `ProdutoConverterTeste`:

1. **`testeConverterParaProdutoEntityComSucesso()`**:
   - Cria um objeto `ProductsDTO` e um objeto `ProdutoEntity` esperado.
   - Chama o método `toEntity` do `ProdutoConverter` para converter o `ProductsDTO` em `ProdutoEntity`.
   - Utiliza asserções para verificar se os atributos dos objetos convertidos correspondem aos esperados.
   - Verifica se o ID e a data de inclusão no `ProdutoEntity` foram gerados.

2. **`testeConverterParaProdutoEntityUpdateComSucesso()`**:
   - Cria um objeto `ProductsDTO`, um objeto `ProdutoEntity` esperado e um objeto `ProdutoEntity` existente.
   - Chama o método `toEntityUpdate` do `ProdutoConverter` para realizar uma atualização.
   - Utiliza asserções para verificar se os atributos dos objetos convertidos correspondem aos esperados após a atualização.
   - Verifica se o ID, a data de inclusão e a data de atualização foram mantidos.

3. **`testeConverterParaProdutoDtoComSucesso()`**:
   - Cria um objeto `ProdutoEntity` e um objeto `ProductsDTO` esperado.
   - Chama o método `toDTO` do `ProdutoConverter` para converter o `ProdutoEntity` em `ProductsDTO`.
   - Utiliza asserções para verificar se os atributos dos objetos convertidos correspondem aos esperados.
   - Verifica se o ID no `ProductsDTO` é nulo, pois não deve ser transferido.

4. **`testeConverterParaListaProdutoDtoComSucesso()`**:
   - Cria uma lista de `ProductsDTO` e uma lista de `ProdutoEntity`, adiciona um elemento a cada lista.
   - Chama o método `toListDTO` do `ProdutoConverter` para converter a lista de `ProdutoEntity` em uma lista de `ProductsDTO`.
   - Utiliza uma asserção para verificar se as listas convertidas são iguais.

Em resumo, esses testes garantem que as operações de conversão no `ProdutoConverter` funcionem conforme o esperado, cobrindo cenários como criação de entidade, atualização de entidade, conversão para DTO e conversão de lista de entidades para lista de DTO. As asserções são usadas para verificar se os resultados da conversão são consistentes com as expectativas definidas nos objetos esperados.

---
# classe de Teste `FakeApiServiceTest`

1. **Anotações no início da classe:**
   ```java
   @ExtendWith(MockitoExtension.class)
   public class FakeApiServiceTest {
   ```

   - `@ExtendWith(MockitoExtension.class)`: Indica que a extensão do Mockito deve ser usada para executar os testes. Isso é específico para o JUnit 5.

2. **Declaração dos Mocks e do serviço sob teste:**
   ```java
   @InjectMocks
   FakeApiService service;

   @Mock
   FakeApiClient client;
   @Mock
   ProdutoConverter converter;
   @Mock
   ProdutoService produtoService;
   ```

   - `@InjectMocks`: Cria uma instância da classe sob teste (`FakeApiService`) e injeta os mocks nas dependências declaradas com `@Mock`.
   - `@Mock`: Cria mocks para as dependências da classe sob teste.

3. **Método `testeBuscarProdutosEGravarComSucesso()`**
```java
   @Test
    void testeBuscarProdutosEGravarComSucesso() {

        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();

        ProductsDTO produtoDTO = ProductsDTO.builder()
                .entityId("1245").nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        listaProdutosDTO.add(produtoDTO);

        ProdutoEntity produtoEntity = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        when(client.buscaListaProdutos()).thenReturn(listaProdutosDTO);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(false);
        when(converter.toEntity(produtoDTO)).thenReturn(produtoEntity);
        when(produtoService.salvarProdutos(produtoEntity)).thenReturn(produtoEntity);
        when(produtoService.buscarTodosProdutos()).thenReturn(listaProdutosDTO);

        List<ProductsDTO> listaProdutosDTORetorno = service.buscarProdutos();

        assertEquals(listaProdutosDTO, listaProdutosDTORetorno);
        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verify(converter).toEntity(produtoDTO);
        verify(produtoService).salvarProdutos(produtoEntity);
        verify(produtoService).buscarTodosProdutos();
        verifyNoMoreInteractions(client, produtoService, converter);
    }
```

   - Este método testa o cenário em que a busca de produtos é bem-sucedida, e nenhum produto com o mesmo nome já existe no banco de dados.
   - Usa o método `when` para configurar comportamentos nos mocks.
   - Usa o método `assertEquals` para verificar se o resultado do método sob teste é o esperado.
   - Usa o método `verify` para garantir que os métodos nos mocks foram chamados conforme o esperado.

4. **Método `testeBuscarProdutosENaoGravarCasoRetornoTrue()`**
```java
@Test
    void testeBuscarProdutosENaoGravarCasoRetornoTrue() {

            List<ProductsDTO> listaProdutosDTO = new ArrayList<>();

        ProductsDTO produtoDTO = ProductsDTO.builder()
        .entityId("1245")
        .nome("Jaqueta Vermelha")
        .categoria("Roupas")
        .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
        .preco(new BigDecimal(250.00))
        .build();

        listaProdutosDTO.add(produtoDTO);

        when(client.buscaListaProdutos()).thenReturn(listaProdutosDTO);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(true);

        ConflictException e = assertThrows(ConflictException.class, () -> service.buscarProdutos());
        System.out.println("Mensagem real da exceção: " + e.getMessage());

        assertThat(e.getMessage(), containsString("Produto já existente no banco de dados"));
        assertThat(e.getMessage(), containsString("Jaqueta Vermelha"));

        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verifyNoMoreInteractions(client, produtoService);
        verifyNoInteractions(converter);
    }
```

   - Testa o cenário em que a busca de produtos retorna um produto já existente no banco de dados.
   - Usa o método `assertThrows` para verificar se a exceção esperada (`ConflictException`) é lançada.
   - Usa o método `verify` para garantir que os métodos nos mocks foram chamados conforme o esperado.

5. **Método `testeGerarExceptionCasoErroAoBuscarProdutosViaClient()`**
```java
    @Test
    void testeGerarExceptionCasoErroAoBuscarProdutosViaClient() {

            when(client.buscaListaProdutos()).thenThrow(new RuntimeException("Erro ao buscar Lista de Produtos"));

            assertThrows(RuntimeException.class, () -> service.buscarProdutos());

        verify(client).buscaListaProdutos();
        verifyNoMoreInteractions(client);
        verifyNoInteractions(converter, produtoService);
        }
```

   - Testa o cenário em que ocorre uma exceção ao buscar produtos no cliente (`client`).
   - Usa o método `assertThrows` para verificar se a exceção esperada (`RuntimeException`) é lançada.
   - Usa o método `verify` para garantir que os métodos nos mocks foram chamados conforme o esperado.

Essencialmente, esses métodos de teste são usados para garantir que a classe `FakeApiService` funciona conforme esperado em diferentes cenários. Eles são uma parte importante da prática de Desenvolvimento Orientado a Testes (TDD) e ajudam a garantir que o código seja robusto e confiável.

---

# classe de Teste `FakeApiConsumerTest`

```java
package com.microservice.fakeapi.infraestructure.consumer;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.ProdutoService;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import com.microservice.fakeapi.infraestructure.message.consumer.FakeApiConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiConsumerTest {

    @InjectMocks
    FakeApiConsumer consumer;

    @Mock
    ProdutoService service;

    @Test
    void testeReceberMensagemProdutoDTOComSucesso(){
        ProductsDTO produtoDTO = ProductsDTO.builder().descricao("Jaqueta Vermelha com bolsos laterais e Listras").preco(new BigDecimal(250.00)).build();

        doNothing().when(service).salvaProdutoConsumer(produtoDTO);

        consumer.recebeProdutosDTO(produtoDTO);

        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }

    @Test
    void testeGerarExceptionCasoErroNoConsumer(){
        ProductsDTO produtoDTO = ProductsDTO.builder().descricao("Jaqueta Vermelha com bolsos laterais e Listras").preco(new BigDecimal(250.00)).build();

        doThrow(new RuntimeException("Erro ao consumir mensagem")).when(service).salvaProdutoConsumer(produtoDTO);

        BusinessException e = assertThrows(BusinessException.class, () -> consumer.recebeProdutosDTO(produtoDTO));

        assertThat(e.getMessage(), is("Erro ao consumir mensagem do kafka "));
        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }
}

```
Analisando os métodos de testes passo a passo:


1. **Método `testeReceberMensagemProdutoDTOComSucesso()`:**

   - É criado um objeto `ProductsDTO` usando o padrão de construção do `builder`. Este objeto será utilizado como entrada para o método de teste.
   - Usa o método `doNothing()` para configurar o comportamento do mock `service`. Isso significa que quando o método `salvaProdutoConsumer` do `service` for chamado com o objeto `produtoDTO`, nenhum efeito será produzido.
   - Chama o método sob teste `recebeProdutosDTO` do objeto `consumer`, passando o objeto `produtoDTO` como argumento.
   - Usa o método `verify` para verificar se o método `salvaProdutoConsumer` do mock `service` foi chamado exatamente uma vez com o argumento `produtoDTO`. Isso garante que a interação esperada ocorreu.
   - Usa o método `verifyNoMoreInteractions` para garantir que não houve mais interações com o mock `service` além daquelas verificadas. Isso é útil para assegurar que apenas as interações esperadas foram realizadas.

Em resumo, esse método de teste verifica se o consumidor (`consumer`) interage corretamente com o serviço (`service`). Ele valida se, ao receber um objeto `ProductsDTO`, o método `salvaProdutoConsumer` do serviço é chamado corretamente, e não há outras interações não esperadas com o serviço. Esse tipo de teste é importante para garantir o comportamento adequado do consumidor em relação ao serviço durante a execução.

2. **Método `testeGerarExceptionCasoErroNoConsumer()`:**

   - É criado um objeto `ProductsDTO` usando o padrão de construção do `builder`. Este objeto será utilizado como entrada para o método de teste.
   - Usa o método `doThrow` para configurar o comportamento do mock `service`. Isso significa que quando o método `salvaProdutoConsumer` do `service` for chamado com o objeto `produtoDTO`, uma exceção do tipo `RuntimeException` com a mensagem "Erro ao consumir mensagem" será lançada.
   - Usa o método `assertThrows` para verificar se a chamada do método `recebeProdutosDTO` do objeto `consumer` resulta na exceção esperada (`BusinessException`). A exceção lançada é então atribuída à variável `e`.
   - Usa o método `assertThat` para verificar se a mensagem da exceção (`e.getMessage()`) é igual a "Erro ao consumir mensagem do kafka ". Isso é útil para garantir que a exceção gerada tem a mensagem esperada.
   - Usa o método `verify` para verificar se o método `salvaProdutoConsumer` do mock `service` foi chamado exatamente uma vez com o argumento `produtoDTO`. Isso garante que a interação esperada ocorreu.
   - Usa o método `verifyNoMoreInteractions` para garantir que não houve mais interações com o mock `service` além daquelas verificadas. Isso é útil para assegurar que apenas as interações esperadas foram realizadas.

Em resumo, esse método de teste verifica se o consumidor (`consumer`) lida corretamente com uma situação em que o serviço (`service`) lança uma exceção durante a execução. Ele valida se a exceção gerada é a esperada (`BusinessException`) e se as interações com o serviço ocorrem conforme o esperado. Esse tipo de teste é importante para garantir que o consumidor se comporte corretamente em resposta a falhas no serviço.

---

# classe de Teste `FakeApiProducerTest`

```java
package com.microservice.fakeapi.infraestructure.message.producer;

import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiProducerTest {

    @InjectMocks
    FakeApiProducer producer;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testeEnviarRespostaCadastroProdutosComSucesso(){
        String mensagem = "Produto cadastro com sucesso";

        producer.enviaRespostaCadastroProdutos(mensagem);

        verify(kafkaTemplate).send(any(), messageCaptor.capture());
        assertEquals(mensagem, messageCaptor.getValue());
    }

    @Test
    void testeRetornarExceptionCasoOcorraErroNaProducaoDaMensagem(){
        doThrow(new RuntimeException("Erro ao produzir mensagem")).when(kafkaTemplate).send(any(), any());

        BusinessException e = assertThrows(BusinessException.class, () -> producer.enviaRespostaCadastroProdutos(null));

        assertThat(e.getMessage(), is("Erro ao produzir mensagem do kafka"));
        verifyNoMoreInteractions(kafkaTemplate);
    }
}
```

Analisando os métodos de testes passo a passo:

1. **Método `testeEnviarRespostaCadastroProdutosComSucesso()`:**

   - Cria uma mensagem de sucesso e a envia usando o método `enviaRespostaCadastroProdutos` do objeto `producer`. Este método é responsável por enviar a mensagem para um tópico Kafka.
   - Utiliza o método `verify` para assegurar que o método `send` do `kafkaTemplate` foi chamado. O `any()` indica que qualquer argumento pode ser passado para o método. O `messageCaptor.capture()` captura o argumento passado para o método para posterior análise.
   - Usa o método `getValue()` do `messageCaptor` para recuperar a mensagem que foi passada para o método `send` do `kafkaTemplate`. A mensagem capturada é então comparada com a mensagem original para garantir que corresponde ao esperado.

Em resumo, esse método de teste verifica se o produtor (`producer`) está enviando corretamente a mensagem esperada para o tópico Kafka usando o `kafkaTemplate`. O uso do `verify` e do `messageCaptor` ajuda a garantir que a interação com o Kafka seja realizada conforme o esperado e permite a verificação precisa dos dados enviados. Esse tipo de teste é crucial para garantir a integridade da comunicação entre o produtor e o Kafka, especialmente em casos de respostas ou eventos.

2. **Método `testeRetornarExceptionCasoOcorraErroNaProducaoDaMensagem()`:**

   - Utiliza o `doThrow` do Mockito para configurar o comportamento do `kafkaTemplate`. Neste caso, está configurado para lançar uma exceção do tipo `RuntimeException` com a mensagem "Erro ao produzir mensagem" sempre que o método `send` for chamado.
   - Chama o método `enviaRespostaCadastroProdutos` do objeto `producer` passando `null` como argumento. Este método, ao tentar produzir a mensagem no Kafka, deve lançar a exceção configurada anteriormente.
   - Utiliza o método `assertThat` para verificar se a exceção capturada (`BusinessException`) possui a mensagem esperada, que é "Erro ao produzir mensagem do kafka".
   - Usa o `verifyNoMoreInteractions` para garantir que não ocorram mais interações com o `kafkaTemplate` após a exceção ser lançada. Isso é importante para assegurar que o método `send` seja chamado apenas uma vez.

Em resumo, este teste verifica se o produtor (`producer`) trata corretamente a exceção lançada durante a produção da mensagem no Kafka, e se não há interações adicionais com o `kafkaTemplate` após a exceção. Esse tipo de teste é valioso para garantir que o sistema reaja adequadamente a cenários de falha durante a comunicação com o Kafka.
---

# classe de Teste `ProdutoServiceTest`

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @InjectMocks
    ProdutoService produtoService;
    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    ProdutoConverter produtoConverter;
    @Mock
    FakeApiProducer fakeApiProducer;

    @Test
    void testSalvarProduto(){

        ProdutoEntity entityToSave = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configurando o comportamento dos mocks
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(entityToSave);

        // Execução do método que iremos testar
        ProdutoEntity savedEntity = produtoService.salvarProdutos(entityToSave);

        verify(produtoRepository, times(1)).save(any(ProdutoEntity.class)); // Verifica se o método save foi chamado uma vez
        assertNotNull(savedEntity);
        assertEquals("Jaqueta Vermelha", savedEntity.getNome()); // Verifica se o nome foi salvo corretamente

    }

    @Test
    void testSalvarProdutoDto(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productDtoToSaved.getNome())).thenReturn(false); // Produto não existe
        when(produtoConverter.toEntity(productDtoToSaved)).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia
        when(produtoConverter.toDTO(any(ProdutoEntity.class))).thenReturn(productDtoToSaved); // Pode retornar o mesmo DTO passado como argumento
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia

        // Execução do método que queremos testar
        ProductsDTO savedDto = produtoService.salvarProdutoDTO(productDtoToSaved);

        // Verificações
        verify(produtoConverter, times(1)).toEntity(productDtoToSaved); // Verifica se o método toEntity foi chamado uma vez
        verify(produtoConverter, times(1)).toDTO(any(ProdutoEntity.class)); // Verifica se o método toDTO foi chamado uma vez
        verify(produtoRepository, times(1)).save(any(ProdutoEntity.class)); // Verifica se o método save foi chamado uma vez

        assertNotNull(savedDto); // Verifica se o DTO retornado não é nulo
        assertEquals(productDtoToSaved, savedDto); // Verifica se o DTO retornado é o mesmo que foi passado como argumento
    }

    @Test
    void testSalvarProdutoDtoComProdutoExistente(){
        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productDtoToSaved.getNome())).thenReturn(true); // Produto já existente

        // Execução do método que vai ser testado
        assertThrows(ConflictException.class, () -> produtoService.salvarProdutoDTO(productDtoToSaved));

        // Verificações
        verify(produtoConverter, never()).toEntity(any()); // Verifica se o método toEntity não foi chamado
        verify(produtoRepository, never()).save(any()); // Verifica se o método save não foi chamado
    }

    @Test
    void testBuscarProdutoPorNome(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoRepository.findByNome(productDtoToSaved.getNome())).thenReturn(new ProdutoEntity()); // Pode retornar uma entidade vazia
        when(produtoConverter.toDTO(any(ProdutoEntity.class))).thenReturn(productDtoToSaved); // Pode retornar o mesmo DTO passado como argumento

        // Execução do método que vai ser testado
        ProductsDTO foundDto = produtoService.buscarProdutoPorNome(productDtoToSaved.getNome());

        // Verificações
        verify(produtoRepository, times(1)).findByNome(productDtoToSaved.getNome()); // Verifica se o método findByNome foi chamado uma vez
        verify(produtoConverter, times(1)).toDTO(any(ProdutoEntity.class)); // Verifica se o método toDTO foi chamado uma vez

        assertNotNull(foundDto); // Verifica se o DTO retornado não é nulo
        assertNotNull(productDtoToSaved.getNome()); // verifica que o nome não pode ser nulo
        assertEquals(productDtoToSaved, foundDto); // Verifica se o DTO retornado é o mesmo que foi passado como argumento
    }

    @Test
    void testBuscarProdutoPorNomeComProdutoNaoEncontrado(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoRepository.findByNome(productDtoToSaved.getNome())).thenReturn(null); // Produto não encontrado

        // Execução do método que vai testar a verificação da exceção
        assertThrows(UnprocessableEntityException.class, () -> produtoService.buscarProdutoPorNome(productDtoToSaved.getNome()));

        // Verificações
        verify(produtoRepository, times(1)).findByNome(productDtoToSaved.getNome()); // Verifica se o método findByNome foi chamado uma vez
        verify(produtoConverter, never()).toDTO(any(ProdutoEntity.class)); // Não deve chamar o método toDTO

    }

    @Test
    void testBuscarTodosProdutos(){

        // Criação de um ProductsDTO para ser salvo
        ProductsDTO productDtoToSaved = ProductsDTO.builder()
                .entityId("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Criação de uma lista de entidades simuladas
        List<ProdutoEntity> listaProdutosEntity = Arrays.asList(new ProdutoEntity());

        // Criação de uma lista de DTOs simulada
        List<ProductsDTO> listaProdutoDto = Arrays.asList(productDtoToSaved);

        // Configuração do comportamento dos mocks
        when(produtoRepository.findAll()).thenReturn(listaProdutosEntity); // Pode retornar uma lista de entidades simuladas
        when(produtoConverter.toListDTO(listaProdutosEntity)).thenReturn(listaProdutoDto);

        // Execução do método que vai ser testado
        List<ProductsDTO> foundDtoList = produtoService.buscarTodosProdutos();

        // Verificações
        verify(produtoRepository, times(1)).findAll(); // Verifica se o método findAll foi chamado uma vez
        verify(produtoConverter, times(1)).toListDTO(listaProdutosEntity); // Verifica se o método toListDTO foi chamado uma vez

        assertNotNull(foundDtoList);  // Verifica se a lista de DTOs retornada não é nula
        assertEquals(listaProdutoDto, foundDtoList); // Verifica se a lista de DTOs retornada é a mesma que foi simulada

    }

  @Test
    void testBuscarTodosProdutosComExcecao(){

        // Configuração do comportamento dos mocks para simular uma exceção
        when(produtoRepository.findAll()).thenThrow(new RuntimeException("Erro ao buscar produtos"));

        // Execução do método que vai testar e verificar a exceção
        assertThrows(BusinessException.class, () -> produtoService.buscarTodosProdutos());

        // Verificações
        verify(produtoRepository, times(1)).findAll(); // Verifica se o método findAll foi chamado uma vez
        verify(produtoConverter, never()).toListDTO(anyList()); // Não deve chamar o método toListDTO

    }

    @Test
    void testDeletarProduto(){

        // Criação de um ProductsDTO para ser salvo
        ProdutoEntity productEntityToSaved = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productEntityToSaved.getNome())).thenReturn(true);

        // Execução do método que queremos testar
        assertDoesNotThrow(() -> produtoService.deletarProduto(productEntityToSaved.getNome()));

        // Verificações
        verify(produtoRepository, times(1)).deleteByNome(productEntityToSaved.getNome()); // Verifica se o método deleteByNome foi chamado uma vez

    }

    @Test
    void testDeletarProdutoComProdutoInexistente() {

        // Criação de um ProductsDTO para ser salvo
        ProdutoEntity productEntityToSaved = ProdutoEntity.builder()
                .id("1245")
                .nome("Jaqueta Vermelha")
                .categoria("Roupas")
                .descricao("Jaqueta Vermelha com bolsos laterais e Listras")
                .preco(new BigDecimal(250.00))
                .build();

        // Configuração do comportamento dos mocks
        when(produtoService.existsPorNome(productEntityToSaved.getNome())).thenReturn(false);

        // Execução do método que vai testar e verificar da exceção
        UnprocessableEntityException excecao = assertThrows(UnprocessableEntityException.class, () -> produtoService.deletarProduto(productEntityToSaved.getNome()));

        // Verificações
        verify(produtoRepository, never()).deleteByNome(productEntityToSaved.getNome()); // Não deve chamar o método deleteByNome

        // Verifica se a mensagem da exceção é a esperada
        assertEquals("Não foi possível deletar o produto, pois não existe produto com o nome: " + productEntityToSaved.getNome(), excecao.getMessage());

    }

}
```

1. **Configuração e Inicialização:**
    - `@ExtendWith(MockitoExtension.class)`: Esta anotação é usada para integrar o Mockito com JUnit 5, permitindo o uso de mocks e stubs durante os testes.
    - `@InjectMocks`: Indica que o Mockito deve injetar mocks nas propriedades anotadas com esta anotação na classe de teste (`ProdutoService`).
    - `@Mock`: Cria mocks para as dependências da classe de teste (`ProdutoRepository`, `ProdutoConverter` e `FakeApiProducer`).

2. **Teste `testSalvarProduto`:**
    - Cria uma instância de `ProdutoEntity` para testar o método `salvarProdutos` de `ProdutoService`.
    - Configura o comportamento esperado do mock `produtoRepository` para o método `save`.
    - Executa o método a ser testado e verifica se o método `save` foi chamado corretamente e se o resultado é o esperado.

3. **Teste `testSalvarProdutoDto`:**
    - Cria uma instância de `ProductsDTO` para testar o método `salvarProdutoDTO` de `ProdutoService`.
    - Configura o comportamento esperado dos mocks para os métodos chamados internamente pelo método testado.
    - Executa o método a ser testado e verifica se os métodos foram chamados corretamente e se o resultado é o esperado.

4. **Teste `testSalvarProdutoDtoComProdutoExistente`:**
    - Testa o cenário em que um produto com o mesmo nome já existe.
    - Configura o comportamento esperado do mock `produtoService` para o método `existsPorNome`.
    - Verifica se uma exceção do tipo `ConflictException` é lançada.

5. **Teste `testBuscarProdutoPorNome`:**
    - Cria uma instância de `ProductsDTO` para testar o método `buscarProdutoPorNome` de `ProdutoService`.
    - Configura o comportamento esperado dos mocks para os métodos chamados internamente pelo método testado.
    - Executa o método a ser testado e verifica se os métodos foram chamados corretamente e se o resultado é o esperado.

6. **Teste `testBuscarProdutoPorNomeComProdutoNaoEncontrado`:**
    - Testa o cenário em que um produto com o nome fornecido não é encontrado.
    - Configura o comportamento esperado do mock `produtoRepository`.
    - Verifica se uma exceção do tipo `UnprocessableEntityException` é lançada.

7. **Teste `testBuscarTodosProdutos`:**
    - Cria uma instância de `ProductsDTO` para testar o método `buscarTodosProdutos` de `ProdutoService`.
    - Configura o comportamento esperado dos mocks para os métodos chamados internamente pelo método testado.
    - Executa o método a ser testado e verifica se os métodos foram chamados corretamente e se o resultado é o esperado.

8. **Teste `testBuscarTodosProdutosComExcecao`:**
    - Testa o cenário em que ocorre uma exceção ao buscar todos os produtos.
    - Configura o comportamento esperado do mock `produtoRepository`.
    - Verifica se uma exceção do tipo `BusinessException` é lançada.

9. **Teste `testDeletarProduto`:**
    - Testa o método `deletarProduto` de `ProdutoService` quando o produto existe.
    - Configura o comportamento esperado do mock `produtoService`.
    - Verifica se o método `deleteByNome` foi chamado corretamente.

10. **Teste `testDeletarProdutoComProdutoInexistente`:**
- Testa o método `deletarProduto` de `ProdutoService` quando o produto não existe.
- Configura o comportamento esperado do mock `produtoService`.
- Verifica se uma exceção do tipo `UnprocessableEntityException` é lançada, e se a mensagem da exceção é a esperada.


---
# Autor
## Feito por: `Daniel Penelva de Andrade`