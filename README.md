# Api de Agendamento

[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)


**API de Agendamento** desenvolvida para gerenciar a oferta e o agendamento de serviços, controlando todo o ciclo de vida dos agendamentos (confirmação, cancelamento, rejeição e reagendamento), com regras de negócio bem definidas e um core de domínio rico.

## Regras de negócios 

- Appointment
    - Todo agendamento é criado com status PENDING
    - Um agendamento pode ser:
        - Confirmado apenas se estiver pendente
        - Cancelado apenas se estiver confirmado
        - Rejeitado apenas se estiver pendente e com motivo obrigatório
        - Reagendado apenas se estiver confirmado
    - Transições inválidas de status lançam exceções de domínio

- Offered Service
    - Um serviço só pode ser criado se possuir:
        - Nome
        - Descrição
        - Pelo menos um horário disponível
        - Pelo menos um dia da semana disponível

    - Um serviço só pode ser agendado se:
        - A data/hora não estiver no passado
        - O serviço estiver disponível no dia da semana
        - O horário estiver dentro dos horários ofertados

- Regra de Reagendamento 
    - Um agendamento confirmado só pode ser reagendado se faltarem no mínimo 4 dias para a data original
    - O agendamento original passa para RESCHEDULED
    - Um novo agendamento é criado com status PENDING
---

## Arquitetura Usada
```
src/
├── main/
│   ├── java/
│   │   └── com.scheduling.api/
│   │       ├── controller/
│   │       ├── domain/
│   │       │   ├── dvo/
│   │       │   ├── enumerates/
│   │       │   ├── exceptions/
│   │       │   │   ├── appointment/
│   │       │   │   ├── offeredservice/
│   │       │   │   └── DomainException.java
│   │       ├── dto/
│   │       │   ├── appointment/
│   │       │   └── offeredservice/
│   │       ├── infra/
│   │       │   ├── config/
│   │       │   ├── errors/
│   │       │   │   ├── bussines/
│   │       │   │   ├── ErrorHandler.java
│   │       │   │   └── HttpBaseException.java
│   │       │   └── providers
│   │       ├── repositories
│   │       └── service/
│   │           └── impl/
│   └── resources/
│       └── application.yaml
└── test/
```

---

## Tecnologias Utilizadas
- **Java 21**
- **H2 Database**
- **Maven** – Gerenciador de dependências
- **Spring Boot 3**
    - Spring Data JPA
- **Swagger/OpenAPI** – Documentação da API
- **JUnit5**

---

##  Conceitos Aplicados

- **SOLID**
- **Modelo de Domínio Rico**
- **Regras de negócio no core da aplicação**
- **Testes unitários** 
- **TDD**
---

## Como Rodar o Projeto

1. Clone o repositório e entre no diretório
```bash
 git clone https://github.com/j0n4t45d3v/scheduling-api.git
 cd scheduling-api/
```
2. Execute o comando
```bash
mvn spring-boot:run
```
> Após iniciar, acesse a documentação da API via Swagger: [localhost:8080](http:localhost:8080/api)
---

## Funcionalidades
- [x] Cadastro de novos serviços
- [x] Agendamento de serviços
- [x] Reagendamento de serviços com pelo menos 4 dias de antecedência
- [x] Funcionalidades de confirmar agendamento, cancelar um agendamento confirmado e rejeitar um agendamento pendente
- [ ] Listagem de serviços disponíveis
- [ ] Listagem de agendamentos

---

## Autor
Desenvolvido por [@Jonatas Rocha](https://github.com/j0n4t45d3v)
