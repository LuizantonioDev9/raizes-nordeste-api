ALTER TABLE funcionario DROP CONSTRAINT IF EXISTS funcionario_perfil_check;

ALTER TABLE funcionario
    ADD CONSTRAINT funcionario_perfil_check
        CHECK (perfil IN ('FUNCIONARIO', 'ADMIN'));