CREATE TABLE cliente (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100),
                         email VARCHAR(100) UNIQUE,
                         senha VARCHAR(255),
                         telefone VARCHAR(20)
);