
USE estoque_Dante;

CREATE TABLE IF NOT EXISTS produtos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  codigo_barras VARCHAR(64) UNIQUE NOT NULL,
  nome VARCHAR(255) NOT NULL,
  marca VARCHAR(100),
  lote VARCHAR(100),
  validade DATE,
  preco DECIMAL(10,2) NOT NULL,
  quantidade INT DEFAULT 0,
  data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
  data_ultima_movimentacao DATETIME
);


CREATE TABLE IF NOT EXISTS movimentacoes_Entrada_Saida (
  id INT AUTO_INCREMENT PRIMARY KEY,
  produto_id INT NOT NULL,
  tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
  quantidade INT NOT NULL,
  data_movimentacao DATETIME DEFAULT CURRENT_TIMESTAMP,
  observacao TEXT,
  FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE
);


CREATE OR REPLACE VIEW vw_produtos_alerta AS
SELECT 
  id, nome, codigo_barras, validade,
  DATEDIFF(validade, CURDATE()) AS dias_restantes
FROM produtos
WHERE validade BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 10 DAY)
ORDER BY validade ASC;

CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  usuario VARCHAR(50) UNIQUE NOT NULL,
  senha VARCHAR(255) NOT NULL,
  cargo ENUM('admin', 'logistica', 'marketing', 'socio') NOT NULL,
  data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO usuarios (nome, usuario, senha, cargo)
VALUES
('joaozinho', 'joaozinho1', 'Jo123!', 'marketing'),
('mariazinha', 'mariazinha2', 'Mari123!', 'logistica');






