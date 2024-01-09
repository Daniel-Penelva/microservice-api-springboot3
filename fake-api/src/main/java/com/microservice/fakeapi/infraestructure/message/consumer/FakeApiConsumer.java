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
