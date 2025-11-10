# API Mottag

API desenvolvida em **Java 21 com Spring Boot** para o projeto **Mottag**, criado para a empresa **Mottu** — referência no aluguel de motocicletas no Brasil.

---

## 📘 Introdução

O **Projeto Mottag** tem como objetivo aprimorar a **gestão e localização das motos nos pátios da Mottu**, utilizando tecnologia **BLE (Bluetooth Low Energy)** para rastreamento interno inteligente.

A solução visa eliminar processos manuais e imprecisos, reduzir perdas de ativos e aumentar a eficiência operacional, integrando-se futuramente ao ecossistema digital da Mottu.

---

## 👥 Equipe de Desenvolvimento

Projeto desenvolvido para o **Challenge FIAP 2025** por:

**Grupo LLM**

* Gabriel Marques de Lima Sousa – RM 554889
* Leonardo Matheus Teixeira – RM 556629
* Leonardo Menezes Parpinelli Ribas – RM 557908

---

## 🚩 Problema Identificado

A Mottu enfrenta dificuldades na localização e controle das motos dentro dos pátios devido ao uso de registros manuais e rastreadores GPS com baixa precisão em ambientes internos. Isso gera:

* Risco de **perda de ativos** por falta de controle no pátio.
* **Localização demorada** de motos, podendo levar dias.
* **Inventários manuais** e demorados.
* Falta de **visibilidade de ativos parados** ou com problemas.
* **Imprecisão** do GPS em áreas cobertas.

> Como os operadores já utilizam smartphones corporativos, a solução Mottag aproveita essa infraestrutura existente sem exigir novos dispositivos móveis.

---

## 💡 Solução Proposta

O **Mottag** utiliza **tags BLE** acopladas às motos enquanto estão nos pátios. As tags são monitoradas por **antenas ESP32**, permitindo que o sistema identifique a posição aproximada das motos e dos operadores.

### Funcionamento:

1. A moto recebe uma **tag BLE** ao entrar no pátio.
2. As **antenas BLE** instaladas detectam os sinais das tags e enviam os dados à API.
3. O operador visualiza no aplicativo um **mapa digital interativo**, atualizado em até **30 segundos**.
4. O operador pode acionar uma tag para fazê-la **piscar luz e emitir som**, facilitando a localização física.

> Fora do pátio, o rastreamento continua sendo feito pelo GPS já existente nos veículos.

---

## ⚙️ Detalhes Técnicos

* **Tag BLE:**

  * Versão simples (beacon básico).
  * Versão com sinal luminoso e sonoro.

* **Antenas ESP32:**

  * Varredura contínua dos sinais BLE.
  * Distribuição estratégica para cobertura total do ambiente.

* **Posição dos operadores:**

  * O próprio smartphone atua como emissor BLE.

---

## 🧠 Funcionalidades Futuras

* **Dashboard administrativo:** gestão de mapa, dispositivos e usuários.
* **Chatbot inteligente:** suporte operacional no aplicativo.
* **Inteligência Artificial:** análise preditiva e insights de operação.
* **Controle RFID:** prevenção de saídas indevidas com integração RFID.

---

## 🎯 Resultados Esperados

* Redução de **perdas e extravios**.
* **Localização em minutos** (antes: até dois dias).
* **Inventários automáticos** e confiáveis.
* Operação **orientada por dados em near real time**.
* Experiência aprimorada para os operadores.
* **Escalabilidade** para diferentes tamanhos de pátios.

---

## 🧩 Funcionalidades da API

* CRUD completo para **motos** e **pátios**.
* **Paginação** e **validação** de dados.
* **Cache** para otimização de consultas.
* **Documentação automática** com Swagger (OpenAPI).
* **Banco de dados H2** em memória para testes.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.4.5**
* **Spring Data JPA**
* **Spring Validation**
* **Spring Cache**
* **H2 Database**
* **OpenAPI / Swagger**
* **JUnit 5**
* **Thymeleaf**

---

## ▶️ Como Executar

1. Certifique-se de ter o **Java 21** instalado.
2. No terminal, navegue até a pasta `api` e execute:

```bash
gradlew.bat bootRun
```

A aplicação estará disponível em **[http://localhost:8080](http://localhost:8080)**.

### Alternativas

* **IntelliJ IDEA:** Execute a classe `ApiApplication` diretamente.
* **JAR:**

```bash
gradlew.bat bootJar
java -jar build/libs/api-0.0.1-SNAPSHOT.jar
```

---

## 📚 Documentação da API

Acesse a documentação interativa em:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🗂️ Estrutura do Projeto

```
controller/   -> Endpoints REST
service/      -> Regras de negócio
model/        -> Entidades JPA
repository/   -> Repositórios JPA
dto/          -> Objetos de transferência de dados
exception/    -> Tratamento de exceções
config/       -> Configurações gerais
security/     -> Configurações de segurança
```