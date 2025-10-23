
-- Views.
USE estoque_Dante;

-- Exibição dos produtos com validade em um intervalo de 10 dias
CREATE OR REPLACE VIEW vw_produtos_alerta AS
SELECT 
  id, nome, codigo_barras, validade,
  DATEDIFF(validade, CURDATE()) AS dias_restantes
FROM produtos
WHERE validade BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 10 DAY)
ORDER BY validade ASC;

-- Visão do estoque
CREATE OR REPLACE VIEW vw_estoque_resumo AS
SELECT
  COUNT(*) AS total_produtos,
  SUM(quantidade) AS total_unidades,
  SUM(preco * quantidade) AS valor_total_estoque
FROM produtos;

-- Produtos que ja venceram
CREATE OR REPLACE VIEW vw_produtos_vencidos AS
SELECT
  id, nome, marca, codigo_barras, validade, quantidade
FROM produtos
WHERE validade < CURDATE()
ORDER BY validade ASC;

-- movimentações feitas com mais detalhes
CREATE OR REPLACE VIEW vw_movimentacoes_detalhadas AS
SELECT
  m.id AS id_movimentacao,
  p.nome AS produto,
  m.tipo,
  m.quantidade,
  m.data_movimentacao,
  m.observacao,
  p.marca,
  p.codigo_barras
FROM movimentacoes_Entrada_Saida m
JOIN produtos p ON m.produto_id = p.id
ORDER BY m.data_movimentacao DESC;
