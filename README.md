# Projeto Sage

O SAGE é um serviço de backend desenvolvido em Java utilizando o framework Quarkus. Ele fornece funcionalidades para gerenciamento de eventos, geração de QR Codes, autenticação de usuários e geração de relatórios em formato Excel.

## Funcionalidades

- **Gerenciamento de Eventos**: Criação, atualização e exclusão de eventos.
- **Geração de QR Codes**: Geração de QR Codes para eventos e armazenamento no Firestore.
- **Autenticação de Usuários**: Autenticação de usuários utilizando Firebase Authentication.
- **Geração de Relatórios**: Geração de relatórios de participantes de eventos em formato Excel.
- **Listagem de Avaliações**: Listagem de avaliações de eventos com a nota e comentário de cada participante.
## Tecnologias Utilizadas

- **Java**
- **Quarkus**
- **Firebase**
- **Firestore**
- **Jakarta EE**
- **Apache POI**: Para manipulação de arquivos Excel.
- **OkHttp**: Para requisições HTTP.
- **Jsoup**: Para parsing de HTML.
- **Maven**: Para gerenciamento de dependências.

## Estrutura do Projeto

- `src/main/java/sage`: Contém as classes principais do projeto.
    - `controller`: Contém as controllers dos serviços com retornos de sucesso ou erros. 
    - `services`: Contém os serviços para gerenciamento de eventos, QR Codes, autenticação e relatórios.
    - `models`: Contém as classes de modelo utilizadas no projeto.
    - `FirebaseInitialize`: Classe que inicializa o banco de dados, realizar as configurações do banco por ela.
- `src/main/resources`: Contém arquivos de configuração.
    - `application.properties`: Configurações do Quarkus e Firebase.

## Configuração

### Pré-requisitos

- **Java 11** ou superior
- **Maven**
- **Firebase**: Conta configurada com Firestore e Firebase Authentication.

### Configuração do Firebase

1. Adicione o arquivo `serviceAccountKey.json` na pasta `src/main/resources`.
2. Configure o Firebase no arquivo `application.properties`:

```ini
quarkus.http.cors=true
quarkus.http.cors.origins=https://app.poa.ifrs.edu.br
quarkus.http.cors.methods=GET,POST,PUT,DELETE
quarkus.jackson.date-format=dd/MM/yyyy
smallrye.jwt.sign.key.location=privateKey.pem
smallrye.jwt.verify.key.location=publicKey.pem
mp.jwt.verify.issuer=sage-app
mp.jwt.verify.clock.skew=60
quarkus.swagger-ui.enable=true
quarkus.swagger-ui.always-include=true
quarkus.smallrye-jwt.enabled=true
```

## Geração de chaves públicas e privadas

### Para criar uma chave privada
openssl genrsa -out rsaPrivateKey.pem 2048

### Converter a chave privada para o formato PKCS#8
openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out privateKey.pem

### Para criar uma chave pública
openssl rsa -pubout -in rsaPrivateKey.pem -out publicKey.pem

Depois de gerar as chaves, devemos indicar a chave privada por meio da propriedade `smallrye.jwt.sign.key.location` no arquivo de `application.properties`, veja o exemplo abaixo:

smallrye.jwt.sign.key.location=privateKey.pem

## Para executar o projeto e adicionar o banco de dados FireStore, siga a documentação abaixo
- [Documentação do Firebase](https://firebase.google.com/docs)
- [Documentação do Android](https://developer.android.com/docs)
- [Documentação do Quarkus](https://quarkus.io/guides/getting-started)
- [Documentação do Maven](https://maven.apache.org/guides/index.html)
- [Documentação do Java](https://docs.oracle.com/en/java/)
- [Documentação do Jakarta EE](https://jakarta.ee/specifications/platform/8/)
- [Documentação do Jsoup](https://jsoup.org/apidocs/)
- [Documentação do OkHttp](https://square.github.io/okhttp/)
- [Documentação local do projeto](https://github.com/LeonardoSilvaFreitas/sageBack/tree/final/docs)
- [Documentação do projeto - página](https://leonardosilvafreitas.github.io/sageBack/)