package com.microservice.fakeapi.infraestructure.message.consumer;

import com.microservice.fakeapi.apiv1.dto.ProductsDTO;
import com.microservice.fakeapi.business.service.ProdutoService;
import com.microservice.fakeapi.infraestructure.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FakeApiConsumer {

    private final ProdutoService produtoService;

    @KafkaListener(topics = "${topico.fake-api.consumer.nome}", groupId = "${topico.fake-api.consumer.group-id}")
    public void consumerProducerProdutos(ProductsDTO productsDTO) {
        try {
            produtoService.salvarProdutoDTO(productsDTO);
        } catch (Exception exception) {
            throw new BusinessException("Erro ao consumir mensagem kafka " + exception);
        }
    }
}
