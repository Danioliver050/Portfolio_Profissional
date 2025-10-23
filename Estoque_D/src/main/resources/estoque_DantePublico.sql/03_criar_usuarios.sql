
-- Criação de usuários
USE estoque_Dante;

-- FUNCIONÁRIO 1
CREATE USER IF NOT EXISTS '*SEU USUARIO'@'localhost' IDENTIFIED BY 'SENHA';

GRANT ALL PRIVILEGES ON estoque_Dante.* TO '*SEU USUARIO'@'localhost';


-- FUNCIONÁRIO 2
CREATE USER IF NOT EXISTS '*SEU USUARIO'@'localhost' IDENTIFIED BY '*SENHA';
GRANT ALL PRIVILEGES ON estoque_Dante.* TO '*SEU USUARIO'@'localhost';

-- FUNCIONÁRIO 3
CREATE USER IF NOT EXISTS 'SEU USUARIO'@'localhost' IDENTIFIED BY 'SENHA';
GRANT SELECT ON estoque_Dante.produtos TO 'SEU USUARIO'@'localhost';
GRANT SELECT ON estoque_Dante.movimentacoes_Entrada_Saida TO 'SEU USUARIO'@'localhost'; 
GRANT SELECT ON estoque_Dante.vw_produtos_alerta TO 'SEU USUARIO'@'localhost';

-- FUNCIONARIO 4
CREATE USER IF NOT EXISTS '*SEU USUARIO'@'localhost' IDENTIFIED BY 'SENHA';
GRANT SELECT, INSERT, UPDATE, DELETE ON estoque_Dante.produtos TO '*SEU USUARIO'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON estoque_Dante.movimentacoes_Entrada_Saida TO 'SEU USUARIO'@'Localhost';
GRANT SELECT ON estoque_Dante.vw_produtos_alerta TO 'SEU USUARIO'@'localhost';

FLUSH PRIVILEGES;