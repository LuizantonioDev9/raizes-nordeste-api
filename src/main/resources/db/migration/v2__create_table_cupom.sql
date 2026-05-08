CREATE TABLE cupom (
                       id UUID PRIMARY KEY,
                       codigo VARCHAR(50),
                       tipo_desconto VARCHAR(20),
                       valor NUMERIC,
                       validade TIMESTAMP

);