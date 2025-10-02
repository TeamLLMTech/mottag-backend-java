# API Mottag

API desenvolvida em Java 21 com Spring Boot para o projeto Mottag da empresa Mottu.

## Descrição
Esta API gerencia motos e pátios, permitindo operações de cadastro, consulta, atualização e remoção (CRUD) de ambos os recursos. Utiliza Spring Boot, Spring Data JPA, validação, cache e documentação automática com OpenAPI/Swagger.

## Vídeo de Apresentação

[Vídeo no YouTube](https://youtu.be/WDUmfSJpdx8)

## Sobre nós
Desenvolvido para o Challenge FIAP 2025 por:

Grupo LLM
- Gabriel Marques de Lima Sousa - RM 554889
- Leonardo Matheus Teixeira - RM 556629
- Leonardo Menezes Parpinelli Ribas - RM 557908

## Funcionalidades
- Cadastro, consulta, atualização e remoção de motos
- Cadastro, consulta, atualização e remoção de pátios
- Paginação e validação de dados
- Documentação automática via Swagger
- Banco de dados em memória H2 para testes

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Validation
- Spring Cache
- H2 Database
- OpenAPI (Swagger)
- JUnit 5
- Thymeleaf

## Como executar

1. Certifique-se de ter o Java 21 instalado e configurado.
2. No terminal, navegue até a pasta `api` e execute:

```powershell
gradlew.bat bootRun
```

A aplicação estará disponível em `http://localhost:8080`.

### Alternativas para execução

- **IntelliJ IDEA**: Abra o projeto e execute a classe `ApiApplication` como uma aplicação Java.
- **JAR**: Compile o projeto e execute o JAR gerado:

```powershell
gradlew.bat bootJar
java -jar build/libs/api-0.0.1-SNAPSHOT.jar
```

## Documentação da API
Acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto
- `controller/`: Endpoints REST
- `service/`: Regras de negócio
- `model/`: Entidades JPA
- `repository/`: Repositórios JPA
- `dto/`: Objetos de transferência de dados
- `exception/`: Tratamento de exceções
- `config/`: Configurações gerais
- `security/`: Configurações de segurança

## Roadmap
- [x] Versão inicial da API
- [x] Swagger
- [x] Validation
- [x] Global Exception Handler
- [x] H2 Database
- [x] Cache
- [x] Conexão com banco de dados externo
- [x] Thymeleaf
- [X] Autenticação e autorização
- [ ] Conexão com IOT
- [ ] Integração com serviços externos
- [ ] Integração com IA