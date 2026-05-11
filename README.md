# Raízes do Nordeste — API Back-end

## Descrição

A Raízes do Nordeste API é uma aplicação Back-end REST desenvolvida para o Projeto Multidisciplinar da trilha Back-End. O projeto foi baseado no estudo de caso da rede Raízes do Nordeste, uma franquia de lanchonetes nordestinas em expansão, com múltiplas unidades, diferentes canais de atendimento, controle de estoque local, pedidos, fidelização, segurança e rastreabilidade operacional.

A API permite gerenciar clientes, funcionários, unidades, produtos, estoque, cupons, pedidos, pagamento mock, atualização de status e programa de fidelidade. O sistema também registra o canal de origem do pedido, como APP, WEB, TOTEM ou BALCAO, permitindo acompanhar e organizar os pedidos conforme o canal utilizado.

O projeto utiliza autenticação com JWT, controle de acesso por perfis, persistência em banco de dados PostgreSQL, documentação com Swagger/OpenAPI e testes organizados em uma collection Postman. O pagamento foi implementado de forma simulada, representando o retorno de um serviço externo sem depender de um gateway real.

---

## Evidências e links da entrega

- Repositório GitHub: `https://github.com/LuizantonioDev9/raizes-nordeste-api`
- Swagger local: `http://localhost:8090/swagger-ui/index.html`
- Especificação OpenAPI: `http://localhost:8090/v3/api-docs`
- Collection Postman: `postman/Raizes do Nordeste API.postman_collection.json`
- Environment Postman: `postman/raizes-do-nordeste.postman_environment.json`
- Deploy: não aplicado nesta versão; execução local.

---

## 1. Requisitos

Para executar o projeto localmente, é necessário ter instalado:

- Java JDK 23
- Maven ou Maven Wrapper
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

Exemplo de configuração usada no ambiente local:

```env
DB_URL=jdbc:postgresql://localhost:5433/meubanco
DB_USERNAME=admin
DB_PASSWORD=123456
DB_DRIVER=org.postgresql.Driver

JPA_DDL_AUTO=update
JPA_SHOW_SQL=true

FLYWAY_ENABLED=false
FLYWAY_LOCATIONS=classpath:db/migration

JWT_SECRET=Ksj@82kLmPq9Xz!2Hd8QwErT6YuIop3A
JWT_EXPIRATION=86400000

SERVER_PORT=8090
```

O arquivo `.env` não deve ser enviado ao GitHub, pois pode conter dados sensíveis. O arquivo `.env.example` pode ser versionado no repositório como modelo de configuração.

Observação: em alguns ambientes, o Spring Boot/IntelliJ pode não ler automaticamente o arquivo `.env`. Nesse caso, configure as variáveis em:

```text
Run/Debug Configurations > DemofinalApplication > Environment variables
```

Exemplo para colar no campo de variáveis de ambiente do IntelliJ:

```env
DB_URL=jdbc:postgresql://localhost:5433/meubanco;DB_USERNAME=admin;DB_PASSWORD=123456;DB_DRIVER=org.postgresql.Driver;JPA_DDL_AUTO=update;JPA_SHOW_SQL=true;FLYWAY_ENABLED=false;FLYWAY_LOCATIONS=classpath:db/migration;JWT_SECRET=Ksj@82kLmPq9Xz!2Hd8QwErT6YuIop3A;JWT_EXPIRATION=86400000;SERVER_PORT=8090
```

---

## 3. Instalação das dependências

Clone o repositório:

```bash
git clone https://github.com/LuizantonioDev9/raizes-nordeste-api.git
cd raizes-nordeste-api
```

Instale as dependências:

```bash
mvnw.cmd clean install -DskipTests
```

Ou, caso o Maven esteja configurado globalmente:

```bash
mvn clean install -DskipTests
```

O parâmetro `-DskipTests` evita a execução dos testes automáticos durante a instalação das dependências. Os testes funcionais da API foram organizados para execução manual via Postman e Swagger.

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

Para testar a conexão diretamente com o banco:

```bash
docker exec -it postgres-dev psql -U admin -d meubanco
```

Para sair do console do PostgreSQL:

```sql
\q
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

O projeto possui seed automático de dados iniciais para facilitar os testes locais. Ao subir a aplicação com um banco novo, são criados um administrador inicial, uma unidade, produtos de exemplo, estoque inicial e um cupom de teste.

---

## 5. Dados iniciais de teste

Ao executar a aplicação em um banco novo, o sistema cria automaticamente alguns dados iniciais para facilitar os testes locais da API.

Esses dados são criados por uma classe de inicialização executada na subida da aplicação. O objetivo é evitar que o avaliador precise cadastrar manualmente um usuário administrador, unidade, produtos, estoque e cupom antes de testar os principais fluxos.

Dados criados automaticamente:

- usuário administrador inicial;
- unidade inicial;
- produtos de exemplo;
- estoque inicial dos produtos na unidade inicial;
- cupom percentual de teste.

Credenciais do administrador inicial:

```text
E-mail: admin@teste.com
Senha: 123456
```

Dados de exemplo criados:

```text
Unidade:
- Unidade Inicial

Produtos:
- Carne de Sol com Macaxeira
- Cuscuz Recheado
- Suco de Cajá

Estoque:
- 20 unidades de cada produto na Unidade Inicial

Cupom:
- Código: PROMO10
- Tipo: PERCENTUAL
- Desconto: 10%
- Validade: 30 dias a partir da criação
```

Esses dados são criados apenas caso ainda não existam no banco. Dessa forma, ao reiniciar a aplicação, o sistema evita cadastrar unidades, produtos, estoque ou cupons duplicados.

Para isso, o seed verifica a existência de registros pelo nome ou código antes de criar novos dados, por exemplo:

```text
findByNomeIgnoreCase
findByCodigo
existsByProdutoIdAndUnidadeId
```

Com esses dados iniciais, o fluxo básico de teste fica mais simples:

1. Subir o PostgreSQL com Docker;
2. Iniciar a API;
3. Fazer login com o administrador inicial;
4. Usar o token JWT para testar rotas protegidas;
5. Criar pedidos usando os produtos e estoques já cadastrados;
6. Aplicar o cupom `PROMO10`;
7. Executar o pagamento mock;
8. Atualizar o status do pedido.

---

## 6. Como iniciar a API

Antes de iniciar a aplicação, verifique se o container PostgreSQL está rodando:

```bash
docker ps
```

Com o banco PostgreSQL em execução, inicie a API com:

```bash
mvnw.cmd spring-boot:run
```

Ou execute a classe principal do projeto pela IDE:

```text
DemofinalApplication
```

Após iniciar, a API estará disponível em:

```text
http://localhost:8090
```

---

## 7. Swagger/OpenAPI

Com a API em execução, acesse a documentação pelo navegador:

```text
http://localhost:8090/swagger-ui/index.html
```

A especificação OpenAPI pode ser acessada em:

```text
http://localhost:8090/v3/api-docs
```

A documentação Swagger/OpenAPI é gerada automaticamente a partir dos endpoints implementados no projeto. Os principais exemplos de request, response e códigos de status estão descritos no documento técnico e também foram validados pela collection Postman.

---

## 8. Testes com Postman

Os testes manuais foram organizados em uma collection Postman exportada no repositório.

A pasta `postman/` contém os arquivos:

```text
postman/
├── Raizes do Nordeste API.postman_collection.json
└── raizes-do-nordeste.postman_environment.json
```

- `Raizes do Nordeste API.postman_collection.json`: contém as requisições organizadas por módulo.
- `raizes-do-nordeste.postman_environment.json`: contém as variáveis utilizadas nos testes, como `base_url`, `token` e IDs reaproveitados nas requisições.

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

Caso o Postman não esteja disponível no computador, os principais endpoints também podem ser testados pelo Swagger em:

```text
http://localhost:8090/swagger-ui/index.html
```

---

## 9. Fluxo básico recomendado para teste

Após subir a aplicação em um banco novo, o fluxo recomendado para teste é:

1. Verificar se o Docker/PostgreSQL está rodando;
2. Iniciar a API;
3. Acessar o Swagger/OpenAPI;
4. Fazer login com o administrador inicial:

```json
{
  "email": "admin@teste.com",
  "senha": "123456"
}
```

5. Copiar o token JWT retornado;
6. Usar o token no header `Authorization`;
7. Testar as rotas protegidas;
8. Criar pedido usando a unidade, produtos e estoque iniciais;
9. Aplicar cupom `PROMO10`, se desejado;
10. Executar pagamento mock;
11. Atualizar status do pedido.

---

## 10. Observações de execução

Durante o teste em outro computador, foram observados alguns pontos importantes para execução local do projeto:

- O projeto utiliza Java JDK 23. Caso o IntelliJ não reconheça os imports do Spring, configure o SDK do projeto para JDK 23 e recarregue o Maven.
- Após instalar o Docker Desktop, pode ser necessário abrir o Docker manualmente e aguardar o Docker Engine iniciar antes de executar o container PostgreSQL.
- Caso o Docker apresente erro de inicialização, pode ser necessário atualizar o WSL com:

```bash
wsl --update
```

- Caso o Maven apresente erro de conexão, como `Connection reset`, verifique a internet, proxy do IntelliJ ou recarregue o projeto Maven.
- Se o comando `mvn clean install` falhar ao apagar a pasta `target`, pare processos em execução, feche janelas que estejam usando a pasta do projeto e remova a pasta `target` manualmente.
- O comando `mvn clean install` executa os testes automáticos. Como o teste padrão sobe o contexto da aplicação, ele pode falhar se o PostgreSQL não estiver rodando.
- Para apenas compilar e instalar as dependências, use:

```bash
mvnw.cmd clean install -DskipTests
```

- Caso apareça o erro `Connection to localhost:5433 refused`, significa que o container PostgreSQL não está rodando ou a porta `5433` não está disponível.
- Caso apareça erro de senha do banco, confira as variáveis de ambiente `DB_USERNAME` e `DB_PASSWORD`.
- O arquivo `.env` pode não ser lido automaticamente pelo IntelliJ. Se isso ocorrer, configure as variáveis em `Run/Debug Configurations > Environment variables`.

---

## 11. Status do projeto

Projeto acadêmico em versão funcional para execução local, documentação com Swagger/OpenAPI, seed automático de dados iniciais e testes manuais via Postman.
