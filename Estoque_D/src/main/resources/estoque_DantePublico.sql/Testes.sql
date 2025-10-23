USE estoque_Dante;
-- TESTES DAS VIEWS:


-- Teste 1: Produtos próximos do vencimento
SELECT * FROM vw_produtos_alerta;

-- Teste 2: Resumo do estoque
SELECT * FROM vw_estoque_resumo;

-- Teste 3: Produtos vencidos
SELECT * FROM vw_produtos_vencidos;

-- Teste 4: Movimentações detalhadas
SELECT * FROM vw_movimentacoes_detalhadas;


-- TESTE DAS PROCEDURES:


USE estoque_Dante;

-- Teste entrada de produto
CALL sp_entrada_produto('10987654321', 5, 'Nova remessa'); 

-- Teste saída de produto
CALL sp_saida_produto('123456789', 2, 'Venda loja física');

-- Teste consulta de produto
CALL sp_consulta_produto('10987654321');

-- Teste relatório de estoque
CALL sp_relatorio_estoque();

-- TESTE DOS TRIGGERS

USE estoque_Dante;

-- Inserção de teste para disparar trigger de entrada
INSERT INTO movimentacoes_Entrada_Saida(produto_id, tipo, quantidade, observacao)
VALUES ((SELECT id FROM produtos WHERE codigo_barras='123456789'), 'ENTRADA', 3, 'Teste trigger');

-- Atualização de produto para disparar trigger de atualização de quantidade
UPDATE produtos
SET quantidade = quantidade + 2
WHERE codigo_barras = '987654321';

-- Verifique se o campo 'data_ultima_movimentacao' foi atualizado
SELECT id, nome, quantidade, data_ultima_movimentacao FROM produtos;




