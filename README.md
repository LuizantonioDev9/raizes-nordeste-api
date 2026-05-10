# Raízes do Nordeste — API Back-end

## Descrição

A Raízes do Nordeste API é uma aplicação Back-end REST desenvolvida para o Projeto Multidisciplinar da trilha Back-End. O projeto foi baseado no estudo de caso da rede Raízes do Nordeste, uma franquia de lanchonetes nordestinas em expansão, com múltiplas unidades, diferentes canais de atendimento, controle de estoque local, pedidos, fidelização, segurança e rastreabilidade operacional.

A API permite gerenciar clientes, funcionários, unidades, produtos, estoque, cupons, pedidos, pagamento mock, atualização de status e programa de fidelidade. O sistema também registra o canal de origem do pedido, como APP, WEB, TOTEM ou BALCAO, permitindo acompanhar e organizar os pedidos conforme o canal utilizado.

O projeto utiliza autenticação com JWT, controle de acesso por perfis, persistência em banco de dados PostgreSQL, documentação com Swagger/OpenAPI e testes organizados em uma collection Postman. O pagamento foi implementado de forma simulada, representando o retorno de um serviço externo sem depender de um gateway real.

---

## 1. Requisitos

Para executar o projeto localmente, é necessário ter instalado:

- Java JDK 23
- Maven
- Git
- Docker ou Docker Desktop
- Postman ou Insomnia

O projeto foi desenvolvido e testado com:

- Java: OpenJDK 23.0.2
- Banco de dados: PostgreSQL
- API local: `http://localhost:8090`
- Banco local via Docker: porta `5433`

Principais dependências utilizadas:

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL Driver
- Hibernate
- Lombok
- Swagger/OpenAPI

---

## 2. Variáveis de ambiente

Na raiz do projeto, crie um arquivo `.env` com base no arquivo `.env.example`.

Exemplo de `.env.example`:

```env
DB_URL=jdbc:postgresql://localhost:5433/meubanco
DB_USERNAME=usuario_do_banco
DB_PASSWORD=senha_do_banco
DB_DRIVER=org.postgresql.Driver

JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

FLYWAY_ENABLED=false
FLYWAY_LOCATIONS=classpath:db/migration

JWT_SECRET=sua_chave_secreta_jwt_com_no_minimo_32_caracteres
JWT_EXPIRATION=86400000

SERVER_PORT=8090
```

No arquivo `.env`, substitua os valores de exemplo pelos dados do ambiente local.

O arquivo `.env` não deve ser enviado ao GitHub, pois pode conter dados sensíveis. O arquivo `.env.example` pode ser versionado no repositório como modelo de configuração.

---

## 3. Instalação das dependências

Clone o repositório:

```bash
git clone https://github.com/LuizantonioDev9/raizes-nordeste-api.git
cd raizes-nordeste-api
```

Instale as dependências:

```bash
mvn clean install
```

No Windows, também pode ser usado:

```bash
mvnw.cmd clean install
```

---

## 4. Banco de dados

O projeto utiliza PostgreSQL via Docker.

No ambiente local, o container usado foi:

```text
postgres-dev
```

Mapeamento de portas:

```text
5433:5432
```

Para criar o container PostgreSQL, caso ele ainda não exista:

```bash
docker run --name postgres-dev -e POSTGRES_DB=meubanco -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 -p 5433:5432 -d postgres
```

Para verificar se o container está rodando:

```bash
docker ps
```

Para iniciar o container, caso ele já exista e esteja parado:

```bash
docker start postgres-dev
```

Para parar o container:

```bash
docker stop postgres-dev
```

Nesta versão do projeto, a estrutura do banco é criada e atualizada pelo Hibernate/JPA com:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

O projeto possui configuração prevista para Flyway, porém não contém migrations versionadas nesta entrega.

Como melhoria futura, as alterações estruturais do banco poderão ser controladas por scripts SQL em:

```text
src/main/resources/db/migration
```

Não há seed automatizado nesta versão. Os dados de teste podem ser cadastrados pela API usando o Postman ou Swagger.

---

## 5. Como iniciar a API

Com o banco PostgreSQL em execução, inicie a API com:

```bash
mvnw.cmd spring-boot:run
```

Ou execute a classe principal do projeto pela IDE.

Após iniciar, a API estará disponível em:

```text
http://localhost:8090
```

---

## 6. Swagger/OpenAPI

Com a API em execução, acesse a documentação pelo navegador:

```text
http://localhost:8090/swagger-ui/index.html
```

A especificação OpenAPI pode ser acessada em:

```text
http://localhost:8090/v3/api-docs
```

---

## 7. Testes com Postman

Os testes manuais foram organizados em uma collection Postman exportada no repositório.

A pasta `postman/` contém os arquivos:

```text
postman/
├── raizes-do-nordeste-api.postman_collection.json
└── raizes-do-nordeste-api.postman_environment.json
```

- `raizes-do-nordeste-api.postman_collection.json`: contém as requisições organizadas por módulo.
- `raizes-do-nordeste-api.postman_environment.json`: contém as variáveis utilizadas nos testes, como `base_url`, `token` e IDs reaproveitados nas requisições.

O environment foi exportado junto com a collection para facilitar a execução dos testes, evitando a necessidade de preencher manualmente a URL base da API, token JWT e identificadores utilizados nos cenários.

Para executar:

1. Importar a collection no Postman.
2. Importar o environment no Postman.
3. Selecionar o environment importado.
4. Verificar se a variável `base_url` está configurada como:

```text
http://localhost:8090
```

5. Realizar login em `/auth/login`.
6. Copiar ou salvar o token JWT retornado na variável `token`.
7. Usar o token nas próximas requisições no header `Authorization`.

Formato do header:

```text
Bearer {{token}}
```

Exemplo de uso de variável na URL:

```text
{{base_url}}/pedidos
```

Ordem recomendada para execução dos testes:

1. Auth
2. Clientes
3. Funcionários
4. Unidades
5. Produtos
6. Estoque
7. Cupons
8. Pedidos
9. Pagamentos
10. Fidelidade
11. Cenários de erro

A collection contém cenários positivos e negativos, incluindo login válido, acesso sem token, acesso sem permissão, criação de pedido, estoque insuficiente, pagamento mock aprovado, pagamento mock recusado e logs/auditoria.
