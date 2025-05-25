# Comunicação entre Microserviços via Mensageria

## Sobre o Projeto

Este projeto foi desenvolvido como estudo prático sobre comunicação entre microserviços utilizando mensageria assíncrona. O sistema implementa um fluxo de cadastro de usuários com envio automático de e-mail de boas-vindas, demonstrando conceitos fundamentais de arquitetura distribuída.

### Objetivo

Compreender e implementar padrões de comunicação entre serviços independentes através de mensageria de comando, explorando os benefícios do desacoplamento e da comunicação assíncrona.

## Arquitetura

O projeto é composto por **dois microserviços independentes** que se comunicam através de um **message broker**:

### **ms-user** (Serviço de Usuários)

- **Responsabilidade**: Gerenciamento dos usuários
- **Funcionalidades**:
    - Cadastro de novos usuários
    - Validação de dados
    - Persistência no banco de dados
    - Publicação de eventos para o RabbitMQ

### **ms-email** (Serviço de E-mail)

- **Responsabilidade**: Processamento e envio de e-mails
- **Funcionalidades**:
    - Consumo das mensagens da fila
    - Envio de e-mails de boas-vindas
    - Log de e-mails enviados

### **Message Broker**

- **RabbitMQ**: Gerenciamento da fila de mensagens entre os serviços
- **Cloud AMQP**: Hospedagem do RabbitMQ na nuvem

---

![image]()

## Fluxo de Funcionamento

1. [Cliente] → POST /users → [ms-user]
2. [ms-user] → Salva usuário → [PostgreSQL]
3. [ms-user] → Publica mensagem → [RabbitMQ Queue]
4. [RabbitMQ] → Entrega mensagem → [ms-email]
5. [ms-email] → Processa mensagem → [SMTP Gmail]
6. [ms-email] → Registra envio → [PostgreSQL]

![image.png]()

## Tecnologias utilizadas

### **Backend**

- **Java 17** - Linguagem de programação
- **Maven** - Gerenciador de dependências

### **Frameworks**

- **Spring Boot** - Framework principal
- **Spring Web** - API REST
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **Spring AMQP** - Integração com RabbitMQ
- **Spring Mail** - Envio de e-mails

### **Infraestrutura**

- **PostgreSQL** - Banco de dados relacional
- **RabbitMQ** - Message broker
- **Cloud AMQP** - RabbitMQ como serviço
- **Gmail SMTP** - Servidor de e-mail

## Como Executar

### Pré-requisitos

- Java 11 ou superior
- Maven 3.6+
- Conta Gmail com senha de app configurada
- Conta Cloud AMQP (ou RabbitMQ local)

1. **Clone o repositório**

```sql
git clone https://github.com/LucasRibasCardoso/demo-microservices.git
```

1. **Configure as variáveis de ambiente**

Para o **ms-user** (application.properties):

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.rabbitmq.addresses=${RABBITMQ_URL}
```

Para o **ms-email** (application.properties):

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.rabbitmq.addresses=${RABBITMQ_URL}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_KEY}
```

**Defina as seguintes variáveis de ambiente:**

```bash
# Banco de dados
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

# RabbitMQ
RABBITMQ_URL=amqp://sua_url_cloudamqp

# E-mail (apenas para ms-email)
MAIL_USERNAME=seu_email@gmail.com
MAIL_KEY=sua_senha_de_app_gmail
```

1. Execute os serviços

```bash
# Terminal 1 - ms-user
cd ms-user
mvn spring-boot:run

# Terminal 2 - ms-email
cd ms-email
mvn spring-boot:run
```

## Testando aplicação

Pode ser utilizado o *Postman* para enviar a requisição.

**POST** `localhost:8081/users`

```json
{
    "name": "testeUsername",   // seu nome
    "email": "teste@gmail.com" // seu email
}
```
