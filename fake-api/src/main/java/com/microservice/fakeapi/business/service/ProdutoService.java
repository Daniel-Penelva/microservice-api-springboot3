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

    // Salva os produtos da API Externa no Banco de dados
    public ProdutoEntity salvarProdutos(ProdutoEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao salvar produtos" + e);
        }
    }

    // Salvar ProdutoDTO
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

    // Salvar Produto Consumer
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

    // Buscar produto por nome
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

    // Buscar todos os produtos DTOs
    public List<ProductsDTO> buscarTodosProdutos() {
        try {
            return converter.toListDTO(repository.findAll());
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar todos os produto por {}", e);
        }
    }

    // Deletar produto por nome
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

    // Verifica se já existe o nome do produto
    public Boolean existsPorNome(String nome) {
        try {
            return repository.existsByNome(nome);
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao buscar produto por nome = %s ", nome) + e);
        }
    }

    // Atualizar Produto
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
