# Dependência `implementation 'com.sun.mail:jakarta.mail:2.0.1'`
A dependência `implementation 'com.sun.mail:jakarta.mail:2.0.1'` refere-se à biblioteca Jakarta Mail (anteriormente conhecida como JavaMail), que é uma API Java para enviar e receber e-mails. Informações importantes sobre essa dependência:

## Jakarta Mail (JavaMail)

- **O que é?**: Jakarta Mail é uma API Java que fornece capacidades para enviar, receber e manipular e-mails usando o protocolo SMTP (Simple Mail Transfer Protocol), além de outros protocolos relacionados, como POP3 (Post Office Protocol) e IMAP (Internet Message Access Protocol).

- **Funcionalidades**:
    - **Envio de E-mails**: Permite que os desenvolvedores enviem e-mails programaticamente através de um servidor SMTP.
    - **Recebimento de E-mails**: Oferece suporte para receber e-mails de uma caixa de correio remota usando os protocolos POP3 ou IMAP.
    - **Manipulação de E-mails**: Permite manipular e-mails recebidos, como ler, excluir e mover mensagens entre pastas.
    - **Construção de E-mails MIME**: Suporta a criação de mensagens de e-mail MIME (Multipurpose Internet Mail Extensions), que podem incluir texto, HTML, imagens embutidas e anexos.

- **Origem**: A API Jakarta Mail foi originalmente desenvolvida como parte do Java EE (Enterprise Edition) e é mantida como um projeto de código aberto pela Fundação Apache.

- **Compatibilidade**: Embora anteriormente conhecida como JavaMail, a API foi renomeada para Jakarta Mail devido a questões de direitos autorais e marca registrada. Portanto, a dependência é agora referenciada como `com.sun.mail:jakarta.mail`.

- **Versão 2.0.1**: A versão 2.0.1 é uma versão específica da biblioteca Jakarta Mail. Cada versão pode trazer correções de bugs, melhorias de desempenho e possivelmente novos recursos.

## Uso da Dependência

Para usar as funcionalidades fornecidas pela Jakarta Mail em seu projeto Java, você precisa incluir a dependência `com.sun.mail:jakarta.mail` em seu arquivo `build.gradle` ou `pom.xml`, dependendo do sistema de gerenciamento de dependências que você está usando.

## Considerações

- Certifique-se de fornecer as configurações corretas do servidor SMTP, bem como as credenciais de autenticação do remetente ao usar a Jakarta Mail para enviar e-mails.
- Sempre gerencie corretamente exceções que possam ocorrer durante o envio de e-mails para garantir um comportamento adequado do aplicativo.
- Consulte a documentação oficial da Jakarta Mail para obter informações detalhadas sobre como usar suas funcionalidades e exemplos adicionais de código.


# Configuração de Envio de E-mail (application-dev.yaml)

## Descrição
Esta configuração define os parâmetros necessários para enviar e-mails através de um servidor SMTP do Gmail.

```yaml
spring:
  mail:
    host: smtp.gmail.com                             # O endereço do servidor SMTP do Gmail, que é smtp.gmail.com.
    port: 587                                        # A porta utilizada para comunicação com o servidor SMTP do Gmail. No caso do Gmail, a porta padrão é 587
    username: testeaplicacao14@gmail.com             # O nome de usuário da conta do Gmail que será usada para enviar os e-mails.
    password: zznq kvkd ugsa kjdj                    # A senha da conta do Gmail correspondente ao nome de usuário fornecido.
    protocol: smtp                                   # O protocolo a ser usado para comunicação com o servidor SMTP, que é smtp (Simple Mail Transfer Protocol).
    properties:                                      # Propriedades adicionais específicas para a configuração do envio de e-mails.
      mail:                                          # Propriedades relacionadas ao envio de e-mails.
        smtp:                                        # Propriedades específicas do protocolo SMTP.
          socketFactory:                             # Configurações relacionadas ao uso de um SocketFactory para criar sockets SSL.
            port: 465                                # A porta a ser usada para comunicação segura. No caso do Gmail, a porta SSL é 465.
            class: javax.net.ssl.SSLSocketFactory    # A classe a ser usada para criar o SSLSocketFactory.
            fallback: false                          # Se true, o socket criado tentará usar primeiro a porta especificada para SSL e, se não for possível, tentará usar a porta padrão. No entanto, o false está configurado, o que significa que não haverá fallback.
          auth: true                                 # Se true, autenticação será realizada ao se conectar ao servidor SMTP.
          starttls:                                  # Configurações relacionadas ao STARTTLS, que é um comando usado para iniciar uma sessão TLS segura.
            enable: true                             # Se true, o cliente tentará iniciar uma sessão TLS ao se conectar ao servidor SMTP.
          connectiontimeout: 5000                    # O tempo limite (em milissegundos) para estabelecer uma conexão com o servidor SMTP. No exemplo, é 5000 milissegundos (ou seja, 5 segundos).
          timeout: 3000                              # O tempo limite (em milissegundos) para leitura de dados do servidor SMTP. No exemplo, é 3000 milissegundos (ou seja, 3 segundos).
          writetimeout: 5000

send:                                               # É uma seção ou chave que agrupa configurações relacionadas ao envio de e-mails.
  mail:                                             # Especifica-se o conteúdo do e-mail.
    to: testeaplicacao14@gmail.com                  # O endereço de e-mail do destinatário do e-mail. No exemplo fornecido, é testeaplicacao14@gmail.com. Isso significa que o e-mail será enviado para este endereço.
    from: testeaplicacao14@gmail.com                # O tempo limite (em milissegundos) para escrever dados para o servidor SMTP. No exemplo, é 5000 milissegundos (ou seja, 5 segundos).
```

## Configurações

- `mail.host`: O endereço do servidor SMTP do Gmail, que é `smtp.gmail.com`.

- `mail.port`: A porta utilizada para comunicação com o servidor SMTP do Gmail. No caso do Gmail, a porta padrão é `587`.

- `mail.username`: O nome de usuário da conta do Gmail que será usada para enviar os e-mails. No exemplo fornecido, é `testeaplicacao14@gmail.com`.

- `mail.password`: A senha da conta do Gmail correspondente ao nome de usuário fornecido. No exemplo, a senha está representada como uma sequência de caracteres `zznq kvkd ugsa kjdj`. Geralmente, a senha seria uma string real, mas é comum para configurações de exemplo substituir a senha real por uma string genérica.

- `mail.protocol`: O protocolo a ser usado para comunicação com o servidor SMTP, que é `smtp` (Simple Mail Transfer Protocol).

- `mail.properties`: Propriedades adicionais específicas para a configuração do envio de e-mails.

    - `mail.smtp`: Propriedades específicas do protocolo SMTP.

        - `mail.smtp.socketFactory`: Configurações relacionadas ao uso de um `SocketFactory` para criar sockets SSL.

            - `mail.smtp.socketFactory.port`: A porta a ser usada para comunicação segura. No caso do Gmail, a porta SSL é `465`.

            - `mail.smtp.socketFactory.class`: A classe a ser usada para criar o `SSLSocketFactory`.

            - `mail.smtp.socketFactory.fallback`: Se `true`, o socket criado tentará usar primeiro a porta especificada para SSL e, se não for possível, tentará usar a porta padrão. No entanto, no exemplo, `false` está configurado, o que significa que não haverá fallback.

        - `mail.smtp.auth`: Se `true`, autenticação será realizada ao se conectar ao servidor SMTP.

        - `mail.smtp.starttls.enable`: Se `true`, o cliente tentará iniciar uma sessão TLS ao se conectar ao servidor SMTP.

        - `mail.smtp.connectiontimeout`: O tempo limite (em milissegundos) para estabelecer uma conexão com o servidor SMTP. No exemplo, é `5000` milissegundos (ou seja, 5 segundos).

        - `mail.smtp.timeout`: O tempo limite (em milissegundos) para leitura de dados do servidor SMTP. No exemplo, é `3000` milissegundos (ou seja, 3 segundos).

        - `mail.smtp.writetimeout`: O tempo limite (em milissegundos) para escrever dados para o servidor SMTP. No exemplo, é `5000` milissegundos (ou seja, 5 segundos).

## Uso
Esta configuração deve ser fornecida ao sistema de envio de e-mails, seja por meio de uma configuração de propriedades, arquivo de configuração YAML, ou outra forma de configuração suportada pelo sistema.


# Classe `MimeMessage` (Classe Utilizada)

A classe `MimeMessage` faz parte da API JavaMail e é usada para representar e-mails MIME (Multipurpose Internet Mail Extensions). E-mails MIME são aqueles que podem incluir não apenas texto simples, mas também conteúdo rico, como anexos, imagens embutidas e formatos de texto HTML.

Explicação detalhada sobre a classe `MimeMessage`:

### Função Principal
A classe `MimeMessage` é usada para criar e manipular mensagens de e-mail no formato MIME. Ela permite que os desenvolvedores configurem o remetente, destinatário, assunto, corpo e outras partes do e-mail, como anexos e cabeçalhos personalizados.

### Características Principais

1. **Construção de Mensagens**: A classe `MimeMessage` permite a construção de mensagens de e-mail a partir do zero, ou seja, você pode criar uma nova mensagem e configurar todos os seus detalhes, como destinatário, remetente, assunto e corpo.

2. **Extensibilidade**: Além de suportar o texto simples, a classe `MimeMessage` permite a inclusão de conteúdo rico em e-mails, como HTML, imagens embutidas e anexos. Isso é possível através da adição de partes MIME à mensagem principal.

3. **Configuração de Cabeçalhos**: Você pode configurar cabeçalhos de e-mail padrão, como `From`, `To`, `Subject`, `Date`, etc., bem como adicionar cabeçalhos personalizados, se necessário.

4. **Manipulação de Anexos**: A classe `MimeMessage` suporta a adição de anexos a e-mails, permitindo que você inclua arquivos como parte da mensagem.

### Uso Comum

1. **Criação de E-mails**: Você pode criar uma nova instância de `MimeMessage` e configurar os detalhes do e-mail, como remetente, destinatário, assunto e corpo.

2. **Adição de Anexos**: Se necessário, você pode adicionar anexos à mensagem usando a API JavaMail.

3. **Envio de E-mails**: Depois de configurar a mensagem, ela pode ser enviada usando um `Transport` fornecido pela API JavaMail.

### Exemplo de Uso Básico
Aqui está um exemplo simplificado de como usar a classe `MimeMessage` para criar e-mails:

```java
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Criação de uma nova mensagem de e-mail
MimeMessage message = new MimeMessage(session);

// Configuração do remetente e destinatário
message.setFrom(new InternetAddress("meuemail@gmail.com"));
message.addRecipient(Message.RecipientType.TO, new InternetAddress("destinatario@example.com"));

// Configuração do assunto e corpo da mensagem
message.setSubject("Assunto do E-mail");
message.setText("Conteúdo do E-mail");

// Envio da mensagem
Transport.send(message);
```

### Considerações de Segurança
Ao usar a classe `MimeMessage`, é importante considerar questões de segurança, como proteção contra ataques de injeção de conteúdo malicioso (por exemplo, XSS em e-mails HTML) e garantir que apenas conteúdo seguro e confiável seja incluído nos e-mails. Além disso, é importante proteger informações confidenciais, como senhas e tokens de acesso, que podem estar presentes no corpo do e-mail ou em anexos.


# Classe `MimeMessageHelpe` (Classe Utilizada)

A classe `MimeMessageHelper` é parte da biblioteca JavaMail e é usada para simplificar a criação de mensagens de e-mail MIME (Multipurpose Internet Mail Extensions). Ela fornece métodos convenientes para configurar os detalhes da mensagem de e-mail, como destinatário, remetente, assunto, corpo e anexos. Informações importantes sobre essa classe:

## Função Principal
A classe `MimeMessageHelper` é usada para ajudar na construção de mensagens de e-mail MIME de forma mais conveniente e eficiente. Ela oferece métodos para configurar vários aspectos da mensagem de e-mail, como destinatário, remetente, assunto, corpo e anexos.

## Características Principais

1. **Construção Simplificada**: A classe `MimeMessageHelper` simplifica a criação de mensagens de e-mail, oferecendo métodos para configurar os detalhes da mensagem de forma clara e intuitiva.

2. **Suporte a Conteúdo Rico**: Além de suportar texto simples, a classe `MimeMessageHelper` permite a inclusão de conteúdo rico em e-mails, como HTML, imagens embutidas e anexos.

3. **Manipulação de Anexos**: A classe `MimeMessageHelper` facilita a adição de anexos a e-mails, fornecendo métodos para anexar arquivos à mensagem.

4. **Codificação de Caracteres**: Ela lida automaticamente com a codificação de caracteres para garantir que o conteúdo do e-mail seja corretamente interpretado pelos clientes de e-mail.

## Uso Comum

1. **Criação de Mensagens de E-mail**: Você pode usar a classe `MimeMessageHelper` para criar uma nova instância e configurar todos os detalhes da mensagem de e-mail, como remetente, destinatário, assunto, corpo e anexos.

2. **Adição de Anexos**: Se necessário, você pode adicionar anexos à mensagem de e-mail usando os métodos fornecidos pela classe `MimeMessageHelper`.

3. **Codificação de Texto**: A classe `MimeMessageHelper` lida automaticamente com a codificação de caracteres para garantir que o texto do e-mail seja corretamente interpretado pelos clientes de e-mail.

## Exemplo de Uso

Exemplo simplificado de como pode usar a classe `MimeMessageHelper` para criar uma mensagem de e-mail:

```java
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

// Criação de uma nova mensagem de e-mail MIME
MimeMessage message = javaMailSender.createMimeMessage();

try {
    // Inicialização de MimeMessageHelper para facilitar a construção da mensagem
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

    // Configuração do remetente, destinatário, assunto e corpo da mensagem
    mimeMessageHelper.setFrom("remetente@example.com");
    mimeMessageHelper.setTo("destinatario@example.com");
    mimeMessageHelper.setSubject("Assunto do E-mail");
    mimeMessageHelper.setText("Conteúdo do E-mail");

    // Anexando um arquivo à mensagem de e-mail
    mimeMessageHelper.addAttachment("arquivo.txt", new File("caminho/do/arquivo.txt"));

    // Envio da mensagem
    javaMailSender.send(message);
} catch (MessagingException e) {
    e.printStackTrace();
}
```

### Considerações

- Certifique-se de configurar corretamente o remetente, destinatário, assunto e corpo da mensagem de e-mail antes de enviar a mensagem.
- Gerencie corretamente exceções que possam ocorrer durante a criação ou envio da mensagem de e-mail para garantir um comportamento adequado do aplicativo.
- Consulte a documentação oficial da API JavaMail para obter informações detalhadas sobre como usar a classe `MimeMessageHelper` e exemplos adicionais de código.


# Serviço de E-mail (EmsilService)

## Descrição
Este script implementa um serviço de e-mail para enviar notificações de erros em um sistema. Ele utiliza a biblioteca JavaMailSender para enviar e-mails através de um servidor SMTP configurado.

```java
package com.microservice.fakeapi.business.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${send.mail.from}")
    private String mailFrom;

    @Value("${send.mail.to}")
    private String mailTo;

    public void enviaEmailExcecao(Exception e) { // Método para enviar e-mail em caso de exceção
        try {
            final MimeMessage message = javaMailSender.createMimeMessage(); // Cria uma nova mensagem de e-mail MIME
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name()); // Inicializa um MimeMessageHelper para facilitar a construção da mensagem

            mimeMessageHelper.setFrom(new InternetAddress(mailFrom)); // Define o remetente do e-mail
            mimeMessageHelper.setTo(InternetAddress.parse(mailTo)); // Define o destinatário do e-mail
            mimeMessageHelper.setSubject("Notificação de erro no sistema"); // Define o assunto do e-mail
            mimeMessageHelper.setText("Ocorreu um erro no sistema" + "\n" + e.getMessage() + "\n" + LocalDateTime.now()); // Define o corpo do e-mail, incluindo a mensagem de exceção e a data e hora atual

            javaMailSender.send(message); // Envia a mensagem de e-mail usando o JavaMailSender

        } catch (MessagingException ex) { // Captura exceções relacionadas ao envio de e-mail
            ex.printStackTrace(); // Imprime o rastreamento da pilha da exceção
        }
    }
}
```

## Anotações
- `@Service`: Esta anotação marca a classe como um serviço, permitindo que seja detectada automaticamente pelo mecanismo de varredura de componentes do Spring.
- `@RequiredArgsConstructor`: Esta anotação do Lombok gera um construtor que injeta automaticamente os campos marcados como `final` pelo construtor.

## Atributos
- `javaMailSender`: Uma instância de `JavaMailSender`, responsável por enviar e-mails.
- `mailFrom`: O endereço de e-mail do remetente do e-mail. Este valor é injetado a partir das propriedades do aplicativo.
- `mailTo`: O endereço de e-mail do destinatário do e-mail. Este valor também é injetado a partir das propriedades do aplicativo.

## Métodos
- `enviaEmailExcecao(Exception e)`: Este método é responsável por enviar um e-mail de notificação quando uma exceção é capturada. Ele recebe a exceção como parâmetro.
    - **Funcionamento**:
        - Cria uma instância de `MimeMessage`.
        - Inicializa um `MimeMessageHelper` para facilitar a construção do e-mail.
        - Define o remetente e o destinatário do e-mail.
        - Define o assunto do e-mail como "Notificação de erro no sistema".
        - Define o corpo do e-mail com uma mensagem genérica informando que ocorreu um erro no sistema, incluindo a mensagem de exceção e a data e hora atual.
        - Envia o e-mail usando `javaMailSender`.
    - **Tratamento de Exceções**:
        - Qualquer exceção do tipo `MessagingException` que ocorra durante o envio do e-mail é capturada e impressa no console.

## Uso
Para usar este serviço, é necessário configurar corretamente as propriedades `send.mail.from` e `send.mail.to` com os endereços de e-mail do remetente e do destinatário, respectivamente, no arquivo de propriedades do aplicativo.

## Notas
- Certifique-se de que as dependências necessárias, como `JavaMailSender`, estejam corretamente configuradas no projeto.
- Verifique se as configurações do servidor SMTP estão corretas para garantir que os e-mails sejam entregues corretamente.


# Anotação `@NotificacaoErro`

## Descrição
Esta anotação é utilizada para marcar métodos ou classes que requerem notificação de erros. Ao marcar um método ou uma classe com `@NotificacaoErro` está indicando que deseja receber notificações em caso de ocorrência de erros durante a execução.

```java
package com.microservice.fakeapi.infraestructure.config.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NotificacaoErro {
}
```

## Características Principais

1. **Identificação de Pontos de Notificação**: A anotação `@NotificacaoErro` pode ser aplicada a métodos e classes para identificar os pontos no código onde as notificações de erro são desejadas.

2. **Runtime Retention**: A anotação é retida em tempo de execução, o que significa que ela estará disponível durante a execução do programa, permitindo que as informações sobre as notificações de erro sejam acessadas e processadas dinamicamente.

## Uso
Para utilizar esta anotação, basta aplicá-la a métodos ou classes onde você deseja receber notificações de erros.

## Considerações
- A anotação `@NotificacaoErro` serve como um marcador para indicar pontos no código onde notificações de erro são necessárias. A implementação da lógica de notificação de erro deve ser feita separadamente.
- Esta anotação pode ser útil em sistemas onde é necessário monitorar determinados pontos no código e ser notificado sobre erros específicos que ocorrem durante a execução.


# Aspecto de Notificação de Erro (NotificaçãoErroAspect)

## Descrição
Este aspecto (`NotificacaoErroAspect`) é responsável por interceptar exceções lançadas em métodos ou classes marcadas com a anotação `@NotificacaoErro` e enviar notificações de erro por e-mail utilizando o serviço de e-mail fornecido (`EmailService`).

```java
package com.microservice.fakeapi.infraestructure.config.error;

import com.microservice.fakeapi.business.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class NotificacaoErroAspect {

    private EmailService emailService;

    @Pointcut("@within(com.microservice.fakeapi.infraestructure.config.error.NotificacaoErro) || @annotation(com.microservice.fakeapi.infraestructure.config.error.NotificacaoErro)")
    public void notificacaoErroPointCut() {};

    @AfterThrowing(pointcut = "notificacaoErroPointCut()", throwing = "e")
    public void notificacaoErro(final Exception e){
        emailService.enviaEmailExcecao(e);
    }
}
```

## Características Principais

1. **Interceptação de Exceções**: O aspecto `NotificacaoErroAspect` intercepta exceções lançadas em métodos ou classes marcadas com a anotação `@NotificacaoErro`.

2. **Envio de Notificações por E-mail**: Quando uma exceção é interceptada, o aspecto chama o método `enviaEmailExcecao` do serviço de e-mail (`EmailService`) para enviar uma notificação por e-mail sobre a exceção ocorrida.

## Anotações Utilizadas

- `@Component`: Esta anotação marca a classe como um componente gerenciado pelo Spring, permitindo que seja detectada automaticamente pelo mecanismo de varredura de componentes.

- `@Aspect`: Esta anotação marca a classe como um aspecto, indicando que ela contém métodos de corte e conselhos que serão aplicados a outros componentes.

- `@RequiredArgsConstructor`: Esta anotação do Lombok gera um construtor que injeta automaticamente os campos marcados como `final` pelo construtor.

## Métodos Principais

1. **`notificacaoErroPointCut()`**: Este método é um ponto de corte que define os locais no código onde o aspecto deve ser aplicado. Ele seleciona todos os métodos ou classes que são anotados com `@NotificacaoErro`.

2. **`notificacaoErro(Exception e)`**: Este método é um conselho do tipo `@AfterThrowing`, que é executado após uma exceção ser lançada em qualquer ponto de corte definido pelo método `notificacaoErroPointCut()`. Ele chama o método `enviaEmailExcecao` do serviço de e-mail para enviar uma notificação sobre a exceção ocorrida.

## Uso
Para usar este aspecto, basta adicionar a anotação `@NotificacaoErro` aos métodos ou classes onde você deseja receber notificações de erros por e-mail.

## Considerações
- Esta abordagem pode ser útil para lidar com exceções em uma aplicação e garantir que a equipe seja notificada imediatamente sobre erros críticos que ocorram em produção.


---
# Autor
## Feito por: `Daniel Penelva de Andrade`