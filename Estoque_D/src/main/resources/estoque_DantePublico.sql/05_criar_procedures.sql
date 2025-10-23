
-- Procedures.
USE estoque_Dante;
DELIMITER $$

CREATE PROCEDURE sp_entrada_produto(
    IN p_codigo_barras VARCHAR(64),
    IN p_quantidade INT,
    IN p_observacao TEXT
)
BEGIN
    DECLARE v_produto_id INT;

    -- Procura o produto pelo código de barras
    SELECT id INTO v_produto_id
    FROM produtos
    WHERE codigo_barras = p_codigo_barras;

    -- Identifica que o produto existe ou não existe
    IF v_produto_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Produto não encontrado.';
    ELSE
        -- Atualiza o estoque e a data da última movimentação
        UPDATE produtos
        SET quantidade = quantidade + p_quantidade,
            data_ultima_movimentacao = NOW()
        WHERE id = v_produto_id;

        -- Registra a movimentação
        INSERT INTO movimentacoes_Entrada_Saida (produto_id, tipo, quantidade, observacao)
        VALUES (v_produto_id, 'ENTRADA', p_quantidade, p_observacao);
    END IF;
END $$

DELIMITER ;
-- Exemplo de uso: CALL sp_entrada_produto('123456789', 10, 'Reabastecimento de estoque');

DELIMITER $$

CREATE PROCEDURE sp_saida_produto(
    IN p_codigo_barras VARCHAR(64),
    IN p_quantidade INT,
    IN p_observacao TEXT
)
BEGIN
    DECLARE v_produto_id INT;
    DECLARE v_quantidade_atual INT;

    -- Procura o produto e a quantidade atual
    SELECT id, quantidade INTO v_produto_id, v_quantidade_atual
    FROM produtos
    WHERE codigo_barras = p_codigo_barras;

    -- Identifica se o produtos existe
    IF v_produto_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Produto não encontrado.';
    ELSEIF v_quantidade_atual < p_quantidade THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Quantidade insuficiente em estoque.';
    ELSE
        -- Atualiza o estoque
        UPDATE produtos
        SET quantidade = quantidade - p_quantidade,
            data_ultima_movimentacao = NOW()
        WHERE id = v_produto_id;

        -- Registra a movimentação
        INSERT INTO movimentacoes_Entrada_Saida (produto_id, tipo, quantidade, observacao)
        VALUES (v_produto_id, 'SAIDA', p_quantidade, p_observacao);
    END IF;
END $$

DELIMITER ;
-- Exemplo de uso CALL sp_saida_produto('123456789', 3, 'Venda efetuada no balcão');

DELIMITER $$

CREATE PROCEDURE sp_excluir_produto(
    IN p_codigo_barras VARCHAR(64)
)
BEGIN
    DECLARE v_produto_id INT;

    SELECT id INTO v_produto_id
    FROM produtos
    WHERE codigo_barras = p_codigo_barras;

    IF v_produto_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Produto não encontrado.';
    ELSE
        DELETE FROM produtos WHERE id = v_produto_id;
    END IF;
END $$

DELIMITER ;
-- Exemplo de uso CALL sp_excluir_produto('987654321');

DELIMITER $$

CREATE PROCEDURE sp_relatorio_estoque()
BEGIN
    SELECT 
        COUNT(*) AS total_produtos,
        SUM(quantidade) AS total_itens_em_estoque,
        MIN(data_cadastro) AS produto_mais_antigo,
        MAX(data_cadastro) AS produto_mais_recente
    FROM produtos;

      -- seleciona os produtos e faz um relatório baseado e exibe quais produtos vencem no perído de até 10 dias

    SELECT 
        nome,
        codigo_barras,
        validade,
        DATEDIFF(validade, CURDATE()) AS dias_restantes
    FROM produtos
    WHERE validade < DATE_ADD(CURDATE(), INTERVAL 10 DAY)
    ORDER BY validade ASC;
END $$

DELIMITER ;
-- Exemplo de uso: CALL sp_relatorio_estoque();
