# Apache Kafka

**Apache Kafka:**

O Apache Kafka é uma plataforma de streaming de dados de código aberto que foi desenvolvida pela Apache Software Foundation. Ele é projetado para lidar com o fluxo massivo de dados em tempo real, sendo altamente escalável e durável. O sistema foi inicialmente desenvolvido pelo LinkedIn e, mais tarde, aberto para a comunidade de código aberto.

A arquitetura do Kafka é baseada em um modelo de publish-subscribe, onde os produtores enviam mensagens para tópicos e os consumidores se inscrevem para receber essas mensagens. O Kafka é especialmente adequado para situações em que é necessário processar grandes volumes de dados em tempo real.

Principais conceitos e componentes do Kafka:

1. **Produtores (Producers):** São responsáveis por enviar mensagens para os tópicos do Kafka. Os produtores publicam registros em tópicos.

2. **Tópicos (Topics):** São canais de comunicação aos quais os produtores enviam mensagens e dos quais os consumidores leem mensagens. Os tópicos organizam as mensagens em categorias.

3. **Consumidores (Consumers):** São os aplicativos ou processos que se inscrevem nos tópicos e processam as mensagens. Os consumidores consomem registros de tópicos.

4. **ZooKeeper:** O Kafka depende do Apache ZooKeeper para coordenação e gerenciamento de configurações. O ZooKeeper mantém o estado do cluster Kafka e coordena a eleição de líderes para as partições.

5. **Partições (Partitions):** Os tópicos podem ser divididos em partições para permitir a distribuição de carga e escalabilidade. Cada partição é ordenada e pode ser replicada para garantir a durabilidade e a tolerância a falhas.

6. **Brokers:** São os servidores Kafka que armazenam os dados. Cada broker é responsável por uma parte das partições e pode se tornar líder de uma partição específica.

O Kafka é amplamente utilizado em cenários como análise em tempo real, processamento de eventos, integração de sistemas distribuídos e log de registros. Sua arquitetura distribuída e capacidade de lidar com grandes volumes de dados em tempo real o tornam uma escolha popular para empresas que precisam de uma plataforma robusta para streaming de dados.

# Comunicação Assíncrona e Síncrona

A comunicação síncrona e assíncrona referem-se a duas abordagens diferentes na transmissão de dados entre sistemas, processos ou componentes de software. Principais diferenças entre ambas:

## **Comunicação Síncrona:**

Na comunicação síncrona, o remetente e o destinatário estão sincronizados no tempo. Isso significa que o remetente envia uma mensagem e espera pela resposta imediata do destinatário antes de continuar sua execução. Durante esse período de espera, o remetente fica bloqueado ou ocioso.

Principais características da comunicação síncrona:

1. **Bloqueio:** O remetente espera pela resposta do destinatário, bloqueando-se durante esse tempo.

2. **Tempo Real:** Geralmente, a comunicação síncrona é usada quando se precisa de uma resposta imediata.

3. **Exemplo:** Uma chamada de função em programação, onde o chamador aguarda a conclusão da função antes de continuar.

## **Comunicação Assíncrona:**

Na comunicação assíncrona, o remetente envia uma mensagem sem esperar uma resposta imediata do destinatário. O remetente não bloqueia sua execução, permitindo que continue suas atividades enquanto espera pela resposta. O destinatário pode responder quando estiver pronto.

Principais características da comunicação assíncrona:

1. **Não Bloqueante:** O remetente não espera ativamente pela resposta do destinatário e continua suas operações.

2. **Tempo de Resposta Variável:** A resposta pode não ser imediata, e o remetente precisa estar preparado para lidar com isso.

3. **Exemplo:** Comunicação entre processos em sistemas distribuídos, chamadas de API assíncronas em programação web.

Em resumo, na comunicação síncrona, o remetente aguarda ativamente uma resposta imediata, enquanto na comunicação assíncrona, o remetente não bloqueia sua execução e pode continuar suas atividades, independentemente de quando a resposta será recebida. A escolha entre essas abordagens depende dos requisitos específicos do sistema ou da aplicação em questão.

# Criando Zookeeper e Kafka com Docker

Vale ressaltar que um arquivo de composição do Docker, geralmente chamado de `docker-compose.yml`. Ele é usado para definir e configurar vários serviços, redes e volumes em um único arquivo, facilitando a implantação e gerenciamento de aplicativos compostos por vários contêineres.

Meu arquivo Docker `docker-compose-bitnami.yml`:

```yaml
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

- `version: "3"`: Especifica a versão da sintaxe do Docker Compose utilizada neste arquivo.

- `services:`: Define os serviços (contêineres) que serão executados.

    - **Zookeeper Service:**
        - `container_name`: Define o nome do contêiner como "zookeeper".
        - `image`: Especifica a imagem Docker a ser usada para este serviço, no caso, "bitnami/zookeeper:latest".
        - `networks`: Conecta o contêiner à rede chamada "kafka-net".
        - `ports`: Mapeia a porta 2181 do host para a porta 2181 do contêiner.
        - `environment`: Configura variáveis de ambiente para o serviço, neste caso, permite login anônimo no Zookeeper.

    - **Kafka Service:**
        - `container_name`: Define o nome do contêiner como "kafka".
        - `image`: Especifica a imagem Docker a ser usada para este serviço, no caso, "bitnami/kafka:latest".
        - `networks`: Conecta o contêiner à rede chamada "kafka-net".
        - `ports`: Mapeia a porta 9092 do host para a porta 9092 do contêiner.
        - `environment`: Configura várias variáveis de ambiente para o serviço Kafka, incluindo o ID do broker, a conexão com o Zookeeper, configurações de rede e outras configurações específicas do Kafka.
        - `depends_on`: Indica que o serviço Kafka depende do serviço Zookeeper, garantindo que o Zookeeper esteja pronto antes que o Kafka seja iniciado.

- `networks:`: Define uma rede chamada "kafka-net" com o driver de bridge. Esta rede é usada para conectar os contêineres do Zookeeper e Kafka, permitindo que eles se comuniquem.
- `Comandos para:`
    - Criação da Imagem no docker-compose: **`docker-compose -f docker-compose-bitnami.yml up -d`**

Este arquivo Docker Compose vai criar e gerenciar rapidamente um ambiente de desenvolvimento ou teste com o Apache Kafka e o Apache Zookeeper.

# Autor
## Feito por: `Daniel Penelva de Andrade`